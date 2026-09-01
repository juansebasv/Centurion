# syntax=docker/dockerfile:1
# ============================================================================
#  Centurion / NodePoller  -  imagen de la aplicacion
#    stage "build"    : Maven 3.9 + JDK 8  ->  target/NodePoller.war
#    stage "runtime"  : Apache TomEE 1.7.5 (web profile) + JRE 8
#  El servidor destino del proyecto NetBeans original es TomEE 1.7.x
#  (OpenEJB 4.7 + MyFaces 2.1 + EclipseLink 2.5.2 + mysql-connector 5.1).
# ============================================================================

# --------------------------- stage: build -----------------------------------
FROM maven:3.9-eclipse-temurin-8 AS build
WORKDIR /src

# 1) dependencias (capa cacheable): copia solo el POM y resuelve el arbol
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline || true

# 2) fuentes + build del WAR
COPY src   ./src
COPY web   ./web
COPY libs  ./libs
RUN mvn -B -q -DskipTests clean package \
 && cp target/NodePoller.war /NodePoller.war \
 && jar -tf /NodePoller.war | grep -E 'WEB-INF/lib/|WEB-INF/classes/co/com/claro/opticalws/' | sort

# --------------------------- stage: runtime --------------------------------
FROM eclipse-temurin:8-jre AS runtime

ENV TOMEE_VERSION=1.7.5 \
    CATALINA_HOME=/opt/tomee \
    PATH=/opt/tomee/bin:$PATH

RUN apt-get update \
 && apt-get install -y --no-install-recommends curl ca-certificates gettext-base \
 && rm -rf /var/lib/apt/lists/* \
 && curl -fsSL -o /tmp/tomee.tar.gz \
      "https://archive.apache.org/dist/tomee/tomee-${TOMEE_VERSION}/apache-tomee-${TOMEE_VERSION}-webprofile.tar.gz" \
 && mkdir -p /opt/tomee \
 && tar xzf /tmp/tomee.tar.gz -C /opt/tomee --strip-components=1 \
 && rm /tmp/tomee.tar.gz \
 && rm -rf /opt/tomee/webapps/ROOT /opt/tomee/webapps/docs \
           /opt/tomee/webapps/examples /opt/tomee/webapps/tomee \
 && curl -fsSL -o /opt/tomee/lib/mysql-connector-java-5.1.49.jar \
      https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.49/mysql-connector-java-5.1.49.jar

COPY docker/tomee.xml.tpl  /opt/tomee/conf/tomee.xml.tpl
COPY docker/entrypoint.sh  /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh

COPY --from=build /NodePoller.war /opt/tomee/webapps/NodePoller.war

EXPOSE 8080
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
