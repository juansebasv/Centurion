<?xml version="1.0" encoding="UTF-8"?>
<!--
  Plantilla del datasource de TomEE. El entrypoint (docker/entrypoint.sh)
  reemplaza ${DB_URL} / ${DB_USER} / ${DB_PASSWORD} con envsubst y escribe
  el resultado en $CATALINA_HOME/conf/tomee.xml.

  El id DEBE ser "jdbc/controlNode": es el <jta-data-source> declarado en
  src/conf/persistence.xml. TomEE deriva solo el datasource no-JTA para EclipseLink.
-->
<tomee>
  <Resource id="jdbc/controlNode" type="javax.sql.DataSource">
    JdbcDriver      com.mysql.jdbc.Driver
    JdbcUrl         ${DB_URL}
    UserName        ${DB_USER}
    Password        ${DB_PASSWORD}
    JtaManaged      true
    InitialSize     0
    MaxActive       20
    MaxWait         30000
    ValidationQuery SELECT 1
    TestOnBorrow    true
  </Resource>
</tomee>
