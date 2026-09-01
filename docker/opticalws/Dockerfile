# Mock del WebService SOAP OpticalWS (niveles en tiempo real)
FROM python:3.12-alpine
RUN pip install --no-cache-dir "pymysql==1.1.1"
WORKDIR /app
COPY server.py OpticalWS.wsdl ./
# El puerto real lo define PORT (81). Comparte el network namespace del contenedor
# 'app' via docker-compose, por eso la app lo alcanza como http://localhost:81
CMD ["python", "-u", "server.py"]
