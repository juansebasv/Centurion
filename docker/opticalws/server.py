#!/usr/bin/env python3
"""
Mock del WebService SOAP 'OpticalWS' que la app Centurion consume en
    http://localhost:81/OpticalWSrv/services/OpticalWS

Implementa:
  - GET  ...?wsdl                -> devuelve el WSDL (OpticalWS.wsdl)
  - POST ...                     -> operaciones getLevels(node) y getPosition(node)

getLevels devuelve una cadena separada por '|' con >=13 campos; la app
(Nodes.findNodeInRealTime) toma con regex los grupos 10..13:
    grupo10 = fwdpwr   grupo11 = fwdinrf   grupo12 = retpwr   grupo13 = retarop

Los valores salen de la BD (ultimo optical_slotdata del nodo). Si la BD no
esta disponible se generan valores sinteticos deterministas por nombre de nodo,
de modo que la operacion NUNCA lanza error.
"""
import os, re, sys, json, random, hashlib, datetime
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

try:
    import pymysql
except Exception:
    pymysql = None

PORT   = int(os.environ.get("PORT", "81"))
WSDL   = open(os.path.join(os.path.dirname(__file__), "OpticalWS.wsdl"), "r", encoding="utf-8").read()
DB     = dict(
    host=os.environ.get("DB_HOST", "db"),
    port=int(os.environ.get("DB_PORT", "3306")),
    user=os.environ.get("DB_USER", "centurion"),
    password=os.environ.get("DB_PASSWORD", "centurion"),
    database=os.environ.get("DB_NAME", "controlnode"),
    connect_timeout=3,
)
NS = "http://opticalws.claro.com.co"


def log(*a):
    print("[opticalws]", *a, file=sys.stderr, flush=True)


# --------------------------------------------------------------------------- #
#  Origen de datos
# --------------------------------------------------------------------------- #
def db_node_levels(node):
    """(state_id, fwdpwr, fwdinrf, retpwr, retarop) desde la BD, o None."""
    if not pymysql:
        return None
    try:
        cx = pymysql.connect(**DB)
    except Exception as e:
        log("sin BD (%s) -> valores sinteticos" % e.__class__.__name__)
        return None
    try:
        with cx.cursor() as c:
            c.execute("SELECT state_id FROM optical_node WHERE name=%s", (node,))
            row = c.fetchone()
            if not row:
                return None
            state = row[0]
            c.execute("""SELECT sd.outputpower, sd.inrf
                           FROM optical_node n
                           JOIN optical_slot s      ON n.fwdslot_id = s.id
                           JOIN optical_slotdata sd ON sd.slot_id   = s.id
                          WHERE n.name=%s ORDER BY sd.created DESC LIMIT 1""", (node,))
            f = c.fetchone() or (None, None)
            c.execute("""SELECT sd.power1, sd.arop
                           FROM optical_node n
                           JOIN optical_slot s      ON n.retslot_id = s.id
                           JOIN optical_slotdata sd ON sd.slot_id   = s.id
                          WHERE n.name=%s ORDER BY sd.created DESC LIMIT 1""", (node,))
            r = c.fetchone() or (None, None)
    finally:
        cx.close()

    if state == 2:                      # DOWN  -> perdida de senal
        return (state, "LOS", "LOS", "LOS", "LOS")

    jit = lambda v, lo, hi: round(
        (float(v) if v is not None else random.uniform(lo, hi)) + random.uniform(-0.12, 0.12), 2)
    return (state,
            jit(f[0], 2.8, 3.4),
            jit(f[1], 11.5, 12.8),
            jit(r[0], -19.0, -17.0),
            jit(r[1], -21.0, -19.0))


def synth_levels(node):
    rnd = random.Random(int(hashlib.md5(node.encode()).hexdigest(), 16))
    return (1,
            round(rnd.uniform(2.8, 3.4), 2),
            round(rnd.uniform(11.5, 12.8), 2),
            round(rnd.uniform(-19.0, -17.0), 2),
            round(rnd.uniform(-21.0, -19.0), 2))


def levels_string(node):
    node = (node or "").strip()
    data = db_node_levels(node) or synth_levels(node)
    state, fwdpwr, fwdinrf, retpwr, retarop = data
    online = state != 2
    ts = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    fields = [
        node,                                   # 1
        "ONLINE" if online else "OFFLINE",      # 2
        ts,                                     # 3
        "%.1f" % random.uniform(38, 52),        # 4  temp C
        "%.2f" % random.uniform(3.2, 3.4),      # 5  Vcc
        "1", "1",                               # 6,7 idx fwd / ret
        "OK" if online else "FAIL",             # 8  tx
        "OK" if online else "FAIL",             # 9  rx
        str(fwdpwr),                            # 10 -> fwdpwr
        str(fwdinrf),                           # 11 -> fwdinrf
        str(retpwr),                            # 12 -> retpwr
        str(retarop),                           # 13 -> retarop
    ]
    return "|".join(fields) + "|"


def position_string(node):
    rnd = random.Random(int(hashlib.md5((node or "").encode()).hexdigest(), 16))
    lat = 4.0 + rnd.uniform(0, 7)
    lon = -76.0 + rnd.uniform(0, 4)
    return "%s|%.5f|%.5f|" % ((node or "").strip(), lat, lon)


# --------------------------------------------------------------------------- #
#  SOAP
# --------------------------------------------------------------------------- #
def esc(s):
    return (s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"))


def soap_response(op, payload):
    return (
        '<?xml version="1.0" encoding="UTF-8"?>'
        '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">'
        '<soapenv:Body>'
        '<ns:{op}Response xmlns:ns="{ns}">'
        '<ns:{op}Return>{payload}</ns:{op}Return>'
        '</ns:{op}Response>'
        '</soapenv:Body></soapenv:Envelope>'
    ).format(op=op, ns=NS, payload=esc(payload))


def soap_fault(msg):
    return (
        '<?xml version="1.0" encoding="UTF-8"?>'
        '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">'
        '<soapenv:Body><soapenv:Fault>'
        '<faultcode>soapenv:Server</faultcode><faultstring>{}</faultstring>'
        '</soapenv:Fault></soapenv:Body></soapenv:Envelope>'
    ).format(esc(msg))


_NODE_RE = re.compile(r"<(?:[\w.-]+:)?node>\s*([^<]*?)\s*</(?:[\w.-]+:)?node>", re.I)


class Handler(BaseHTTPRequestHandler):
    protocol_version = "HTTP/1.1"

    def _send(self, body, code=200, ctype="text/xml; charset=utf-8"):
        b = body.encode("utf-8")
        self.send_response(code)
        self.send_header("Content-Type", ctype)
        self.send_header("Content-Length", str(len(b)))
        self.end_headers()
        self.wfile.write(b)

    def log_message(self, fmt, *args):
        log(self.address_string(), fmt % args)

    def do_GET(self):
        if "wsdl" in self.path.lower() or "xsd" in self.path.lower():
            self._send(WSDL)
        else:
            self._send("OpticalWS mock up. Use ?wsdl or POST SOAP.", ctype="text/plain; charset=utf-8")

    def do_POST(self):
        n = int(self.headers.get("Content-Length", 0) or 0)
        body = self.rfile.read(n).decode("utf-8", "replace") if n else ""
        m = _NODE_RE.search(body)
        node = m.group(1) if m else ""
        try:
            if "getPosition" in body:
                out = soap_response("getPosition", position_string(node))
                log("getPosition(%r)" % node)
            else:
                s = levels_string(node)
                out = soap_response("getLevels", s)
                log("getLevels(%r) -> %s" % (node, s))
            self._send(out)
        except Exception as e:                       # nunca deberia pasar
            log("ERROR", repr(e))
            self._send(soap_fault(str(e)), code=500)


if __name__ == "__main__":
    log("driver pymysql:", "si" if pymysql else "NO (solo sinteticos)")
    srv = ThreadingHTTPServer(("0.0.0.0", PORT), Handler)
    log("escuchando en 0.0.0.0:%d" % PORT)
    srv.serve_forever()
