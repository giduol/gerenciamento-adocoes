# ---------- Build ----------
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B package -DskipTests

# ---------- Runtime ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# JVM ajustada para caber em um container de 256 MB:
#  heap 80 MB + metaspace ~110 MB + code cache 24 MB + threads/nativo
ENV JAVA_TOOL_OPTIONS="-Xms32m -Xmx80m \
 -XX:MaxMetaspaceSize=110m \
 -XX:ReservedCodeCacheSize=24m \
 -XX:MaxDirectMemorySize=10m \
 -Xss256k \
 -XX:+UseSerialGC \
 -XX:TieredStopAtLevel=1 \
 -XX:CICompilerCount=1 \
 -XX:+ExitOnOutOfMemoryError"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
