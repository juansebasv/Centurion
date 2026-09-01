#!/bin/sh
# ============================================================================
#  Entrypoint del contenedor "app" (TomEE).
#  Renderiza conf/tomee.xml a partir de la plantilla + variables de entorno
#  (DB_URL / DB_USER / DB_PASSWORD, definidas en config/centurion.env) y arranca.
# ============================================================================
set -eu

: "${DB_URL:?falta DB_URL}"
: "${DB_USER:?falta DB_USER}"
: "${DB_PASSWORD:?falta DB_PASSWORD}"

TPL="$CATALINA_HOME/conf/tomee.xml.tpl"
OUT="$CATALINA_HOME/conf/tomee.xml"

envsubst '${DB_URL} ${DB_USER} ${DB_PASSWORD}' < "$TPL" > "$OUT"

echo "[entrypoint] datasource jdbc/controlNode -> $DB_URL (user=$DB_USER)"
exec "$CATALINA_HOME/bin/catalina.sh" run
