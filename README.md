# 🚀 Docker from Scratch: Containerize Your First Project 🚀

¡Bienvenido al taller "Docker from Scratch"! Esta guía es tu punto de partida para aprender a contenerizar aplicaciones.
Aquí encontrarás toda la teoría, ejemplos y buenas prácticas que veremos durante la sesión.

El objetivo es que al
finalizar, no solo entiendas qué es Docker, sino que seas capaz de crear tus propias imágenes, gestionar aplicaciones
multi-contenedor y comprender cómo se integra en un flujo de trabajo de desarrollo moderno.
---

## 🧰 Requisitos Técnicos Previos

- [Java 21+](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- [Docker Desktop instalado (Windows/Mac/Linux)](https://www.docker.com/products/docker-desktop/)
- Conocimiento básico de la línea de comandos (cd, ls, mkdir, etc.).
- Un editor de texto como `VS Code`, `Intellij`, `subline`, etc.

---

## 📝 Contenido del Taller

#### [Introducción a la Contenerización y Docker](#part-1)

#### [Fundamentos de Docker](#part-2)

#### [Creación de Imágenes con Dockerfile](#part-3)

#### [Gestión de Volúmenes y Redes](#part-4)

#### [Orquestación con Docker Compose](#part-5)

#### [Buenas Prácticas y Tips](#part-6)

#### [BONUS: Docker en un Flujo de CI/CD Real](#part-7)

#### [Recursos para Continuar Aprendiendo](#part-8)

---

A continuación se detalla el contenido de cada parte. Las instrucciones prácticas están incluidas directamente en este
documento.

<h2 id="part-1">🔹 Introducción a la Contenerización y Docker</h2>

### ¿Qué es la contenerización?

Es una tecnología que permite empaquetar una aplicación junto con todas sus dependencias (
librerías, binarios, archivos de configuración) en una unidad ligera y portable llamada contenedor. Esto asegura que la
aplicación se ejecute de manera consistente sin importar el entorno.

### Problemas del mundo real que resuelve Docker

* Elimina el clásico problema de "en mi máquina funciona", proporcionando un
  entorno aislado y 100% reproducible.
* Simplifica la configuración de entornos de desarrollo.
* Facilita la creación de pipelines de Integración Continua y Despliegue Continuo (CI/CD).
* Mejora la portabilidad de las aplicaciones entre diferentes servidores y nubes.

### Comparación: Docker vs Máquinas Virtuales (VMs)

* **VMs:** Emulan un hardware completo, incluyendo un sistema operativo invitado. Son pesadas, lentas para iniciar y
  consumen muchos recursos.
* **Docker:** Los contenedores comparten el kernel del sistema operativo del host. Son extremadamente ligeros, inician
  en
  segundos y son mucho más eficientes en el uso de recursos.

### Casos de uso en la industria

Desarrollo, testing, despliegue de microservicios, herramientas DevOps, entornos de aprendizaje y sandboxing.

---

<h2 id="part-2">🔹 Fundamentos de Docker</h2>

### Arquitectura de Docker

* **Docker Engine:** El "corazón" de Docker. Es un servicio (demonio) que se ejecuta en segundo plano para crear y
  administrar contenedores.
* **Docker CLI:** La herramienta de línea de comandos (docker) que usamos para interactuar con el Docker Engine.
* **Docker Hub:** Un registro público (como GitHub, pero para imágenes) donde puedes encontrar y compartir imágenes de
  Docker.

### Conceptos Clave

* **Imagen:** Una plantilla inmutable de solo lectura que contiene el código de la aplicación, librerías y dependencias
  necesarias para ejecutar un contenedor.
* **Contenedor:** Una instancia en ejecución de una imagen. Es el entorno aislado donde vive y se ejecuta tu aplicación.
* **Volumen:** Un mecanismo para persistir datos más allá del ciclo de vida de un contenedor. Ideal para bases de datos,
  logs o archivos generados por el usuario.
* **Red:** Permite la comunicación entre contenedores o entre un contenedor y el mundo exterior.

### Primeros Comandos

```dockerfile
# Descarga y ejecuta la imagen "hello-world" en un nuevo contenedor
docker run hello-world

# Lista los contenedores que están en ejecución
docker ps

# Lista todos los contenedores (en ejecución y detenidos)
docker ps -a

# Detiene un contenedor en ejecución (usa el ID o el nombre del contenedor)
docker stop <container_id_or_name>

# Elimina un contenedor detenido
docker rm <container_id_or_name>

# Lista todas las imágenes que tienes descargadas localmente
docker images
```

<h2 id="part-3">🔹 Creación de Imágenes con Dockerfile</h2>

### ¿Qué es un `Dockerfile`?

Es un archivo de texto sin extensión llamado `Dockerfile` que contiene un conjunto de instrucciones paso a paso para
construir una imagen de Docker.

### Instrucciones Clave

* `FROM`: Define la imagen base sobre la que se construirá la nueva imagen (ej. FROM node:18-alpine).
* `WORKDIR`: Establece el directorio de trabajo para las instrucciones siguientes.
* `COPY`: Copia archivos y directorios desde el host al sistema de archivos del contenedor.
* `RUN`: Ejecuta un comando durante el proceso de construcción de la imagen (ej. RUN npm install).
* `EXPOSE`: Documenta el puerto en el que la aplicación escuchará dentro del contenedor.
* `CMD`: Define el comando por defecto que se ejecutará cuando se inicie un contenedor a partir de la imagen.

### Ejemplo de `Dockerfile` para una app Java (Javalin)

Para este ejemplo, vamos a contenerizar una aplicación web muy ligera creada con **Javalin**. Este micro-framework es
ideal para principiantes porque requiere una configuración mínima y nos permite enfocarnos directamente en el proceso de
Docker.

### Estructura del Proyecto

```text
.
├── pom.xml
├── src
│   └── main
│       └── java
│           └── dev
│               └── fredpena
│                   └── App.java
└── Dockerfile
```

### Código de la Aplicación

#### Dependencias (`pom.xml`)

El `pom.xml `es mucho más simple. Solo necesitamos la dependencia de Javalin y un plugin de Maven (`maven-shade-plugin`)
para empaquetar nuestra aplicación en un único `.jar` ejecutable.

```xml

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>dev.fredpena</groupId>
    <artifactId>docker-javalin-app</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>docker-javalin-app</name>
    <url>http://maven.apache.org</url>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>io.javalin</groupId>
            <artifactId>javalin</artifactId>
            <version>6.7.0</version>
        </dependency>
    </dependencies>


    <build>
        <finalName>${project.name}</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.6.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <transformer
                                        implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>dev.fredpena.App</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>

```

#### Código de la aplicación (**App.java**)

¡Toda nuestra aplicación cabe en un solo archivo! Este código inicia un servidor web en el puerto 7070 y responde con un
simple "Hola Mundo" en la ruta raíz.

```java
package dev.fredpena;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        var app = Javalin.create()
                .get("/", ctx -> ctx.html("¡Hola Mundo desde Javalin y Docker!"))
                .start(7070);

        System.out.println("Servidor Javalin corriendo en http://localhost:7070");
    }
}

```

#### Creación del `Dockerfile` (Build Multi-etapa)

Usaremos la misma estrategia de `build multi-etapa` que es una de las mejores prácticas. La técnica es universal para
cualquier proyecto Java compilado.

```dockerfile
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
    
```

### Comando para construir la imagen

```shell
# Construye la imagen a partir del Dockerfile en el directorio actual (.)
# y le asigna el nombre (tag) "mi-app-javalin:1.0"
docker build --no-cache -t mi-app-javalin:1.0 .  
    
```

### Comando para ejecutar el contenedor

Una vez construida la imagen, la ejecutamos mapeando el puerto `7070` del contenedor al `7171` de nuestra máquina.

```shell
docker run -d -p 7171:7070 --name mi-aplicacion-javalin mi-app-javalin:1.0
```

¡Listo! Ahora puedes abrir tu navegador en http://localhost:7171 y verás tu aplicación Javalin corriendo felizmente
dentro de un contenedor Docker.

<h2 id="part-4">🔹 Gestión de Volúmenes y Redes</h2>

### Persistencia de Datos con Volúmenes

Sin volúmenes, cualquier dato que tu aplicación escriba dentro del contenedor se perderá cuando el contenedor sea
eliminado. Los volúmenes resuelven esto.

```shell
# Crea un volumen llamado "datos-db"
docker volume create datos-db

# Inicia un contenedor de postgres y monta el volumen en la ruta donde guarda los datos
docker run -d --name mi-postgres \
  -e POSTGRES_PASSWORD=mysecretpassword \
  -v datos-db:/var/lib/postgresql/data \
  postgres:15

```

### Redes entre Contenedores

```shell
# 1. Crea una nueva red
docker network create mi-red

# 2. Inicia el contenedor de la base de datos en esa red
docker run -d --name mi-postgres --network mi-red -e POSTGRES_PASSWORD=mysecretpassword postgres:15

# 3. Inicia el contenedor de tu aplicación en la misma red
# Ahora, desde la app, puedes conectar a la base de datos usando el host "mi-postgres"
docker run -d --name mi-app --network mi-red -p 8080:3000 mi-app:1.0

```

<h2 id="part-5">🔹 Orquestación con Docker Compose</h2>

### ¿Qué es y por qué usarlo?

Docker Compose es una herramienta para definir y ejecutar aplicaciones multi-contenedor. Usa un archivo YAML (
`docker-compose.yml`) para configurar todos los servicios, redes y volúmenes de tu aplicación. Es la forma estándar de
gestionar entornos de desarrollo locales.

### Sintaxis básica de `docker-compose.yml`

```yml
services:
  # Servicio de la aplicación
  app:
    build: . # Construye la imagen desde el Dockerfile en el directorio actual
    ports:
      - "8080:3000" # Mapea el puerto 8080 del host al 3000 del contenedor
    volumes:
      - .:/app # Monta el código local para desarrollo en vivo
    networks:
      - mi-red
    depends_on:
      - db # Espera a que el servicio 'db' inicie primero

  # Servicio de la base de datos
  db:
    image: postgres:15 # Usa una imagen pública de Docker Hub
    environment:
      - POSTGRES_PASSWORD=mysecretpassword
    volumes:
      - datos-db:/var/lib/postgresql/data # Usa un volumen para persistir los datos
    networks:
      - mi-red

# Definición de redes y volúmenes
networks:
  mi-red:
volumes:
  datos-db:

```

### Comandos Esenciales

```shell
# Levanta todos los servicios definidos en el archivo (en segundo plano)
docker-compose up -d

# Detiene y elimina los contenedores, redes y volúmenes
docker-compose down

# Muestra los logs de todos los servicios
docker-compose logs

# Muestra los logs de un servicio específico en tiempo real
docker-compose logs -f app
```

<h2 id="part-6">🔹 Buenas Prácticas y Tips</h2>

### Optimización de Imágenes

* Usa imágenes base oficiales y pequeñas (ej. `alpine`).
* Utiliza un archivo `.dockerignore` para excluir archivos innecesarios (como `node_modules`, `.git`, etc.) del contexto
  de build.
* Combina múltiples comandos `RUN` en uno solo usando && para reducir el número de capas.

### Estructura de Proyectos

Mantén tu `Dockerfile` y `docker-compose.yml` en la raíz del proyecto.

### Publicar en Docker Hub

```shell
# 1. Inicia sesión en tu cuenta de Docker Hub
docker login

# 2. Etiqueta tu imagen con tu nombre de usuario
# docker tag <imagen_local> <usuario>/<repositorio>:<tag>
docker tag mi-app:1.0 tu-usuario/mi-app:1.0

# 3. Sube la imagen
docker push tu-usuario/mi-app:1.0
```

<h2 id="part-7">🔹 BONUS: Docker en un Flujo de CI/CD Real</h2>

La teoría es útil, pero ver Docker en acción en un flujo de trabajo automatizado es donde realmente brilla. A
continuación, analizamos un pipeline de **Integración Continua y Despliegue Continuo (CI/CD)** real usando **GitHub
Actions**.

Este pipeline se activa automáticamente cada vez que se suben cambios a la rama `main`, y se encarga de

1. Construir el proyecto Java.
2. Crear una imagen Docker.
3. Publicarla en un registro (Docker Hub).
4. Desplegar la nueva versión en un servidor.

```yml
# .github/workflows/deploy.yml
on:
  push:
    branches: [ main ] # Se activa con cada push a la rama 'main'
  pull_request:
    branches: [ main ] # Se activa con cada push a la rama 'main'

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest # El trabajo se ejecuta en una máquina virtual de Ubuntu

    steps:
      # 1. Clona el código del repositorio
      - name: Checkout repository
        uses: actions/checkout@v4

      # 2. Inicia sesión en Docker Hub de forma segura usando secrets
      - name: Login to Docker Hub
        run: echo "${{ secrets.DOCKER_PASSWORD }}" | docker login -u "${{ secrets.DOCKER_USERNAME }}" --password-stdin

      # 3. Configura el entorno de construcción (Java 21 y Maven)
      - name: Set up Temurin JDK 21
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'
          cache: maven

      # 4. Instala dependencias locales del proyecto (si las hubiera)
      # - name: Instalar dependencias locales
      #   run: mvn install:install-file ...

      # 6. Nos aseguramos que maven este instalado
      - name: Verify Maven installation
        run: mvn -version

      # 7. Obtiene el nombre del proyecto como esta en el pom.xml
      - name: Get product name and version
        run: |
          echo "NAME=$(mvn help:evaluate -q -DforceStdout -D"expression=project.name")" >> $GITHUB_ENV

      # 8. Construye el proyecto Java con Maven, saltando los tests
      - name: Build Java project with Maven
        run: |
          mvn dependency:go-offline -Pproduction
          mvn clean package -DskipTests -Pproduction

      # 9. ¡La magia de Docker! Construye la imagen y la sube a Docker Hub
      - name: Build and push Docker image
        run: |
          docker build -t fredpena/${NAME}:lastest .
          docker push fredpena/${NAME}:lastest

      # 10. Se conecta al servidor de despliegue vía SSH
      - name: SSH into the server and deploy
        uses: appleboy/ssh-action@v1.0.3
        with:
          host: ${{ secrets.SERVER_HOST }}
          username: ${{ secrets.SERVER_USER }}
          key: ${{ secrets.SSH_PRIVATE_KEY }}
          port: 22
          script: |
            pwd
            cd /usr/local/app
            docker compose pull
            docker compose up -d

```

Este ejemplo práctico demuestra cómo los comandos que aprendimos (`docker build`, `docker push`, `docker compose`) son
los pilares de la automatización moderna.

<h2 id="part-7">🔹 Recursos para Continuar Aprendiendo</h2>

* [Play with Docker](https://www.docker.com/play-with-docker/): Un playground online y gratuito para experimentar con
  Docker en tu navegador.
* [Documentación Oficial de Docker](https://docs.docker.com/): La fuente de verdad para todo lo relacionado con Docker.
* [Awesome Docker](https://github.com/veggiemonk/awesome-docker): Un listado curado en GitHub con cientos de recursos,
  tutoriales y herramientas.
* [AwesomeDocker Compose](https://awesome-docker-compose.com/): Una plataforma ideal para descubrir e implementar
  fácilmente más de 700 aplicaciones populares alojadas en servidores propios.

**_¡Gracias por participar en el taller! Esperamos que te sea de gran utilidad._**

