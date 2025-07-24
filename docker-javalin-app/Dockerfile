# ---- Etapa 1: Build ----
# Usamos una imagen de Maven con JDK 21 para compilar nuestro proyecto.
# La nombramos 'build' para poder referenciarla después.
FROM maven:3.9-eclipse-temurin-21 AS build

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos solo el pom.xml para aprovechar el cache de capas de Docker.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente
COPY src ./src

# Compilamos el proyecto y creamos el .jar ejecutable
RUN mvn package -DskipTests

# ---- Etapa 2: Run ----
# Usamos una imagen base de solo Java Runtime (JRE), que es mucho más ligera.
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiamos el .jar compilado desde la etapa 'build' a la nueva imagen.
# El plugin de shade genera dos jars, copiamos el que tiene las dependencias.
COPY --from=build /app/target/docker-javalin-app.jar app.jar

# Exponemos el puerto 7070, que es el que usa nuestra app Javalin.
EXPOSE 7070

# Definimos el comando para ejecutar la aplicación.
ENTRYPOINT ["java", "-jar", "app.jar"]