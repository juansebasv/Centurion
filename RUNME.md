# RUNME — Ejecutar Centurión paso a paso

Guía detallada para levantar el proyecto **desde cero** en una máquina local usando Docker.
Para la visión de arquitectura y stack, ver [README.md](README.md).

---

## 0. Requisitos

| Requisito | Cómo comprobar | Notas |
|-----------|----------------|-------|
| Docker Engine ≥ 20.10 | `docker --version` | Con permiso para el usuario actual (`docker ps` sin sudo) |
| Docker Compose v2 | `docker compose version` | Es el plugin `docker compose` (con espacio), no `docker-compose` |
| Puertos libres | `8080` (app) y `3307` (MySQL) | Si están ocupados, ver el paso 7 |
| Salida a Internet | — | El build descarga TomEE 1.7.5, dependencias Maven y el conector MySQL |
| Espacio en disco | ~1.5 GB | Imágenes de TomEE, MySQL, Maven y Python |

> **No** necesitas Java, Maven ni NetBeans instalados: todo el build ocurre dentro de contenedores.

---

## 1. Obtener el código

```bash
git clone <URL-del-repo> Centurion
cd Centurion
```

Verifica que tienes los archivos clave:

```bash
ls docker-compose.yml pom.xml config/centurion.env db/init/ docker/
```

---

## 2. (Opcional) Revisar / ajustar la configuración

Toda la parametrización está en **un solo archivo**:

```bash
cat config/centurion.env
```

```ini
MYSQL_ROOT_PASSWORD=rootpw
MYSQL_DATABASE=controlnode
MYSQL_USER=centurion
MYSQL_PASSWORD=centurion

DB_URL=jdbc:mysql://db:3306/controlnode?useSSL=false
DB_USER=centurion
DB_PASSWORD=centurion

DB_HOST=db
DB_PORT=3306
DB_NAME=controlnode
OPTICALWS_PORT=81
```

Para un uso local normal **no hay que cambiar nada**. Si cambias `MYSQL_USER` /
`MYSQL_PASSWORD`, actualiza también `DB_USER` / `DB_PASSWORD` (y `DB_URL` si cambias el
esquema).

---

## 3. Construir y levantar todo

```bash
docker compose up -d --build
```

Esto hace, en orden:

1. **build `app`** — etapa Maven (`maven:3.9-eclipse-temurin-8`):
   `wsimport` genera el cliente SOAP → `mvn package` compila y arma `NodePoller.war`.
   Etapa runtime: descarga **Apache TomEE 1.7.5** + `mysql-connector-java 5.1.49`.
2. **build `opticalws`** — imagen Python con el mock SOAP.
3. **run `db`** — MySQL 5.7; en el **primer** arranque crea el datadir y ejecuta
   `db/init/01-schema.sql` y `02-seed.sql`.
4. **run `app`** — espera a que `db` esté *healthy*, renderiza `conf/tomee.xml` a partir de
   `config/centurion.env` y arranca TomEE, que despliega `NodePoller.war`.
5. **run `opticalws`** — comparte la red de `app` y escucha en `localhost:81`.

⏱ El primer `up` tarda varios minutos (descargas + init de MySQL). Los siguientes son rápidos.

---

## 4. Esperar a que esté listo

### 4.1 Estado de los contenedores

```bash
docker compose ps
```

Objetivo:

```
NAME                  STATUS
centurion-db          Up (healthy)
centurion-app         Up
centurion-opticalws   Up
```

Si `centurion-db` aparece como `health: starting` durante un rato es **normal** (está
cargando la semilla). `app` no arranca hasta que `db` pase a `healthy`.

### 4.2 Seguir el arranque de la app

```bash
docker compose logs -f app
```

Señales de éxito (Ctrl-C para salir del follow):

```
[entrypoint] datasource jdbc/controlNode -> jdbc:mysql://db:3306/controlnode?useSSL=false (user=centurion)
INFO: ... _NodePollerPU login successful
INFO: Jndi(name=global/NodePoller/NodesBoundary!co.com.claro.nodepoller.boundary.NodesBoundary) --> Ejb(...)
INFO: Deployed Application(path=/opt/tomee/webapps/NodePoller)
INFO: Server startup in NNNN ms
```

No debe aparecer ninguna línea `SEVERE`.

### 4.3 Comprobar por HTTP

```bash
curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8080/NodePoller/faces/index.xhtml
# -> 200
```

---

## 5. Abrir la aplicación

Navegador → **http://localhost:8080/NodePoller/**

Deberías ver:

- Cabecera con el logo y un contador regresivo (auto-refresh cada 180 s).
- Tabla **Alarmas Registradas** con datos (24 alarmas, paginadas de a 6).
- Buscador **Buscar Nodo** y botón **Niveles**.

### Recorrido de verificación funcional

| Paso | Entrada | Resultado esperado |
|------|---------|--------------------|
| 1. Buscar Nodo | `NODOCAL001` → botón 🔍 | Ficha con Ciudad = CALI, Plataforma = AURORA, icono de estado **verde**; tablas de historial Forward/Retorno con filas |
| 2. Buscar Nodo | `NODOCAL002` → botón 🔍 | Icono de estado **rojo**; historial con valores degradados |
| 3. Niveles | `NODOCAL001` → botón **Niveles** | Diálogo con `fwdpwr`, `fwdinrf`, `retpwr`, `retarop` numéricos |
| 4. Niveles | `NODOCAL002` → botón **Niveles** | Diálogo con `LOS / LOS / LOS / LOS` |
| 5. Exportar | icono Excel en cualquier tabla | Descarga `.xls` |

Nodos disponibles: `NODO{BOG,MED,CAL,BAQ,CTG,BUC,CUC,PEI,MZL,IBG}{001,002}`.

---

## 6. Inspeccionar la base de datos (opcional)

Desde el host (requiere cliente `mysql`):

```bash
mysql -h127.0.0.1 -P3307 -ucenturion -pcenturion controlnode
```

O dentro del contenedor:

```bash
docker compose exec db mysql -ucenturion -pcenturion controlnode

# ejemplos
SELECT state_id, COUNT(*) FROM optical_node GROUP BY state_id;      -- 1=UP, 2=DOWN, 3=UNMANAGED
SELECT COUNT(*) FROM optical_nodealarm WHERE ack = 2;               -- alarmas visibles
SELECT city_name, COUNT(*) FROM optical_nodealarm GROUP BY city_name;
```

Probar el mock SOAP directamente (desde dentro de `app`, que es quien lo ve como `localhost:81`):

```bash
docker compose exec app sh -lc \
 'curl -s -X POST http://localhost:81/OpticalWSrv/services/OpticalWS \
   -H "Content-Type: text/xml" \
   -d "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body>\
<getLevels xmlns=\"http://opticalws.claro.com.co\"><node>NODOMED001</node></getLevels>\
</s:Body></s:Envelope>"'
```

---

## 7. Cambiar puertos (si 8080 o 3307 están ocupados)

Edita `docker-compose.yml`:

```yaml
  db:
    ports: ["3308:3306"]     # host 3308
  app:
    ports: ["9090:8080"]     # host 9090  -> http://localhost:9090/NodePoller/
```

Luego:

```bash
docker compose up -d
```

---

## 8. Ciclo de desarrollo (cambios en el código)

```bash
# tras editar src/java, web/, src/conf/persistence.xml, pom.xml ...
docker compose up -d --build app
docker compose logs -f app
```

Cambios sólo en la semilla o el esquema (`db/init/*.sql`) — hay que recrear la BD:

```bash
docker compose down -v
docker compose up -d
```

Cambios en `config/centurion.env`:

```bash
docker compose up -d --force-recreate
```

Cambios en el mock (`docker/opticalws/`):

```bash
docker compose up -d --build opticalws
```

---

## 9. Parar y limpiar

```bash
docker compose stop                 # pausa (conserva contenedores y datos)
docker compose down                 # elimina contenedores + red (conserva el volumen de datos)
docker compose down -v              # + elimina el volumen MySQL (la semilla se recargará)
docker compose down --rmi local -v  # + elimina también las imágenes construidas
```

---

## 10. Checklist de "todo OK"

- [ ] `docker compose ps` → `db (healthy)`, `app (Up)`, `opticalws (Up)`
- [ ] `docker compose logs app` → sin `SEVERE`, con `Server startup in ...` y `_NodePollerPU login successful`
- [ ] `http://localhost:8080/NodePoller/` responde `200` y muestra la tabla de alarmas
- [ ] Buscar `NODOCAL001` → icono verde · `NODOCAL002` → icono rojo
- [ ] Niveles de `NODOCAL001` → números · de `NODOCAL002` → `LOS`
