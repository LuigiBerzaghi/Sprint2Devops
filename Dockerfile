# ===== Stage 1: Build =====
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia POM e resolve dependências em cache
COPY trackyard/pom.xml ./
RUN mvn -B -q -e -DskipTests dependency:go-offline

# Copia o código e compila
COPY trackyard/src ./src
RUN mvn -B -q -e -DskipTests package

# Descobre o JAR gerado (spring-boot repackage cria um "…-SNAPSHOT.jar")
RUN ls -lah target && \
    JAR_FILE=$(ls target/*SNAPSHOT.jar 2>/dev/null || ls target/*.jar | head -n1) && \
    echo "$JAR_FILE" > /jar-path

# ===== Stage 2: Runtime =====
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o JAR do stage de build
COPY --from=build /jar-path /jar-path
RUN cp $(cat /jar-path) /app/app.jar

# Saúde (opcional) - Spring Boot actuator (se ativado)
# HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost:8080/actuator/health || exit 1

# Porta padrão do Spring Boot
ENV SERVER_PORT=8080
EXPOSE 8080

# Variáveis de DB - cobrindo AMBOS os padrões vistos no seu README
# (alguns trechos usam DB_URL/DB_USER/DB_PASS, outros SPRING_DATASOURCE_*)
ENV DB_URL=""
ENV DB_USER=""
ENV DB_PASS=""
ENV SPRING_DATASOURCE_URL=""
ENV SPRING_DATASOURCE_USERNAME=""
ENV SPRING_DATASOURCE_PASSWORD=""

# Mapeia DB_* -> SPRING_DATASOURCE_* se necessário
ENTRYPOINT ["sh", "-c", "\
  if [ -n \"$DB_URL\" ]; then export SPRING_DATASOURCE_URL=\"$DB_URL\"; fi && \
  if [ -n \"$DB_USER\" ]; then export SPRING_DATASOURCE_USERNAME=\"$DB_USER\"; fi && \
  if [ -n \"$DB_PASS\" ]; then export SPRING_DATASOURCE_PASSWORD=\"$DB_PASS\"; fi && \
  java -jar /app/app.jar \
    --server.port=${SERVER_PORT} \
    --spring.datasource.url=${SPRING_DATASOURCE_URL} \
    --spring.datasource.username=${SPRING_DATASOURCE_USERNAME} \
    --spring.datasource.password=${SPRING_DATASOURCE_PASSWORD} \
"]
