# 🚀 Docker from Scratch: Containerize Your First Project 🚀

¡Bienvenido al taller "Docker from Scratch"! Esta guía es tu punto de partida para aprender a contenerizar aplicaciones.
Aquí encontrarás toda la teoría, ejemplos y buenas prácticas que veremos durante la sesión.

El objetivo es que al
finalizar, no solo entiendas qué es Docker, sino que seas capaz de crear tus propias imágenes, gestionar aplicaciones
multi-contenedor y comprender cómo se integra en un flujo de trabajo de desarrollo moderno.
---

## 🧰 Requisitos Técnicos Previos

* [Java 21+](https://adoptium.net/)
* [Maven 3.9+](https://maven.apache.org/)
* [Docker Desktop instalado (Windows/Mac/Linux)](https://www.docker.com/products/docker-desktop/)
* Conocimiento básico de la línea de comandos (`cd`, `ls`, etc.).
* Un editor de texto como `VS Code`, `Intellij`, etc.
* **Clonar este repositorio** para tener todos los archivos de ejemplo listos.

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
* **Docker Desktop:** Una aplicación para Windows y macOS que empaqueta el Docker Engine, la CLI y otras herramientas (
  como
  Docker Compose) en una sola instalación fácil de usar. Proporciona una interfaz gráfica para gestionar contenedores,
  imágenes y volúmenes, simplificando enormemente el desarrollo local.
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

```shell
# Descarga y ejecuta la imagen "hello-world" en un nuevo contenedor
docker run hello-world

# un contenedor alpine y lo dejaremos corriendo en segundo plano. El comando sleep 3600 simplemente lo mantiene "vivo" durante una hora para que podamos conectarnos.
docker run -d --name mi-contenedor-prueba alpine sleep 3600

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

### Interactuando con un Contenedor: El Poder de `docker exec`

Una de las capacidades más potentes de Docker es que cada contenedor es un entorno Linux aislado. A veces, necesitas "
entrar" en ese entorno para depurar un problema, verificar que un archivo se copió correctamente o simplemente explorar
su contenido. Para esto, usamos el comando `docker exec`.

Este comando nos permite ejecutar un proceso (como una terminal) dentro de un contenedor que **ya está en funcionamiento
**.

### Ejercicio Práctico: Conectándonos a la Terminal de Contenedor Genérico

Para este ejercicio, en lugar de usar nuestra aplicación (que construiremos más adelante), usaremos una imagen oficial y
muy ligera llamada `alpine`. Esto nos permite practicar el comando `exec` de forma rápida y sin dependencias.

Vamos a entrar a la terminal de nuestra aplicación Javalin para verla "desde adentro".

#### Paso 1: Iniciar un contenedor de prueba

Vamos a iniciar un contenedor alpine y lo dejaremos corriendo en segundo plano. El comando `sleep 3600` simplemente lo
mantiene "vivo" durante una hora para que podamos conectarnos.

```shell
docker run -d --name mi-contenedor-prueba alpine sleep 3600
```

#### Paso 2: Conectarse a la terminal del contenedor

Ahora, ejecuta el siguiente comando para abrir una sesión de shell dentro del contenedor que acabamos de crear:

```shell
docker exec -it mi-contenedor-prueba sh
```

##### Análisis del comando:

* `exec`: Le dice a Docker que queremos ejecutar un comando en un contenedor existente.
* `-it`: Es la combinación de `-i` (interactivo) y `-t` (tty), que nos da una sesión de terminal interactiva.
* `mi-contenedor-prueba`: Es el nombre que le dimos a nuestro contenedor.
* `sh`: Es el comando que queremos ejecutar. Como nuestra imagen base es alpine, usamos `sh` (el shell de Alpine). En
  imágenes basadas en Ubuntu, lo común sería usar `bash`.

#### Paso 3: Explora dentro del contenedor

Una vez que ejecutes el comando, notarás que el prompt de tu terminal cambia a algo como `/ #`. ¡Felicitaciones, estás
dentro del contenedor!

Ahora puedes usar comandos de Linux para explorar:

```shell
# Lista los archivos en el directorio raíz
ls -l

# Muestra información sobre el sistema operativo (verás que es Alpine)
cat /etc/os-release

# Muestra los procesos que se están ejecutando (verás el proceso "sleep")
ps aux

# Para salir de la terminal del contenedor y volver a tu máquina, simplemente escribe:
exit
```

#### Paso 4: Limpieza (¡Importante!)

Cuando termines de explorar, no olvides detener y eliminar el contenedor de prueba para mantener tu sistema limpio.

```shell
# Detiene la ejecución de un contenedor que está corriendo
docker stop mi-contenedor-prueba

# Elimina un contenedor que está detenido.
docker rm mi-contenedor-prueba


# Fuerza la eliminación de un contenedor, aunque esté en ejecución.
# Equivalente a:
# docker stop + docker rm en un solo paso.

# Advertencia:
# Úsalo con cuidado, ya que termina el contenedor abruptamente y lo elimina sin posibilidad de recuperación.
docker rm -f mi-contenedor-prueba
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
# Crea un volumen nombrado llamado "datos-db"
docker volume create datos-db

# Muestra los subcomandos disponibles para trabajar con volúmenes
docker volume

# Inicia un contenedor de PostgreSQL usando el volumen nombrado
docker run -d --name mi-postgres \
  -e POSTGRES_PASSWORD=mysecretpassword \
  -v datos-db:/var/lib/postgresql/data \
  postgres:15

# Elimina el contenedor (los datos persisten en el volumen)
docker rm -f mi-postgres

# Inicia otro contenedor pero montando una carpeta local del host
docker run -d --name mi-postgres \
  -e POSTGRES_PASSWORD=mysecretpassword \
  -v ./datos-db:/var/lib/postgresql/data \
  postgres:15

# Elimina nuevamente el contenedor (los datos quedan en ./datos-db)
docker rm -f mi-postgres

# Elimina el volumen nombrado (borra permanentemente los datos)
docker volume rm datos-db

```

### Resumen rápido: tipos de volumen

| Montaje            | ¿Quién lo gestiona? | Persistencia | Visible desde el host |
|--------------------|---------------------|--------------|-----------------------|
| `datos-db:/ruta`   | Docker              | ✅ Sí         | ❌ No directamente     |
| `./datos-db:/ruta` | Usuario (host)      | ✅ Sí         | ✅ Sí                  |

> **Notas:**
> - Los volúmenes nombrados (`datos-db`) son gestionados automáticamente por Docker. Son ideales para producción.
> - Las rutas locales (`./datos-db`) permiten acceder directamente a los archivos desde el sistema host. Útiles para
    desarrollo o depuración.

> **Notas (para uso con Docker Compose):**
> - Los volúmenes nombrados (`datos-db`) **se eliminan** si usas `docker compose down -v`. Aunque son ideales para
    producción, hay que tener cuidado con el parámetro `-v`.
> - Las rutas locales (`./datos-db`) **no se eliminan** al hacer `docker compose down -v`, ya que pertenecen al sistema
    de archivos del host. Son útiles para desarrollo o cuando se desea persistencia externa manual.

### Redes entre Contenedores

La teoría dice que las redes permiten que los contenedores se comuniquen. ¡Vamos a demostrarlo! Haremos que nuestra
aplicación Javalin se conecte a una base de datos PostgreSQL.

Veremos qué pasa cuando intentamos conectarlos sin una red compartida y cómo una red resuelve el problema "mágicamente".

> Antes de avanzar vamos a actulizar nuestra aplicación

#### Paso 1: Actualizar el `pom.xml`

Primero, necesitamos añadir el driver de PostgreSQL para que nuestra aplicación Java pueda conectarse a la base de
datos.

Añade la siguiente dependencia dentro de la sección <dependencies> de tu archivo pom.xml:

```xml

<dependencies>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.7</version>
    </dependency>

    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>2.0.16</version>
    </dependency>
</dependencies>
```

#### Paso 2: Actualizar la Aplicación Java (App.java)

Ahora, modificaremos App.java para añadir el nuevo endpoint `/datos` que se conectará a la base de datos. Este código
está
diseñado para ser simple y auto-contenido, como pediste.

Reemplaza el contenido de tu App.java con este:

```java
package dev.fredpena;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class App {

    // Inicializar el logger
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        var app = Javalin.create()
                .get("/", ctx -> {
                    log.info("Acceso a la ruta /");
                    ctx.html("¡Hola Mundo desde Javalin y Docker!");
                })
                .get("/datos", ctx -> {
                    log.info("Acceso a la ruta /datos");

                    StringBuilder responseHtml = new StringBuilder();
                    responseHtml.append("Registros desde PostgreSQL:");

                    String dbHost = System.getenv().getOrDefault("DB_HOST", "localhost");
                    String dbUrl = "jdbc:postgresql://" + dbHost + ":5432/postgres";
                    String user = "postgres";
                    String password = "mysecretpassword";

                    try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
                         Statement stmt = conn.createStatement()) {

                        log.info("Conexión establecida con PostgreSQL en {}", dbUrl);

                        stmt.execute("CREATE TABLE IF NOT EXISTS taller_docker (id SERIAL PRIMARY KEY, nombre VARCHAR(50))");
                        stmt.execute("INSERT INTO taller_docker (nombre) SELECT 'Contenedor' WHERE NOT EXISTS (SELECT 1 FROM taller_docker WHERE nombre = 'Contenedor')");
                        stmt.execute("INSERT INTO taller_docker (nombre) SELECT 'Imagen' WHERE NOT EXISTS (SELECT 1 FROM taller_docker WHERE nombre = 'Imagen')");
                        stmt.execute("INSERT INTO taller_docker (nombre) SELECT 'Volumen' WHERE NOT EXISTS (SELECT 1 FROM taller_docker WHERE nombre = 'Volumen')");

                        ResultSet rs = stmt.executeQuery("SELECT nombre FROM taller_docker ORDER BY id");

                        responseHtml.append("<ul>");
                        while (rs.next()) {
                            responseHtml.append("<li>").append(rs.getString("nombre")).append("</li>");
                        }
                        responseHtml.append("</ul>");
                        responseHtml.append("<p style='color:green;'>¡Conexión exitosa!</p>");

                        log.info("Datos cargados correctamente desde la base de datos");

                    } catch (Exception e) {
                        log.error("Error al conectar a la base de datos: {}", e.getMessage(), e);
                        responseHtml.append("<p style='color:red;'>Error al conectar a la base de datos: ")
                                .append(e.getMessage())
                                .append("</p>");
                    }

                    ctx.html(responseHtml.toString());
                })
                .start(7070);

        log.info("Servidor Javalin corriendo en http://localhost:7070");
    }
}

```

#### Paso 3: Reconstruir la Imagen Docker

Como hemos cambiado el código y las dependencias, necesitamos reconstruir nuestra imagen Docker para que incluya estos
cambios.

Ejecuta el siguiente comando en tu terminal, en la raíz del proyecto:

```shell
docker build --no-cache -t mi-app-javalin:1.0 .
```

### Escenario 1: Falla de Conexión (Sin Red Compartida)

Primero, vamos a lanzar los dos contenedores por separado. Por defecto, Docker los pone en redes aisladas y no pueden "
verse" entre sí.

#### Inicia el contenedor de PostgreSQL

```shell
docker run -d --name mi-postgres-demo -e POSTGRES_PASSWORD=mysecretpassword postgres:15
```

#### Inicia el contenedor de nuestra aplicación Javalin

Le pasamos una variable de entorno -`e DB_HOST=mi-postgres-demo` para decirle a nuestra app que intente conectarse al
host llamado `mi-postgres-demo`.

```shell
docker run -d -p 7171:7070 --name mi-app-demo -e DB_HOST=mi-postgres-demo mi-app-javalin:1.0
```

#### Comprueba el resultado

Abre tu navegador y ve a `http://localhost:7171/datos`.

Verás un mensaje de error. La aplicación no puede resolver el nombre `mi-postgres-demo` porque, desde su perspectiva, no
existe.

Puedes confirmar el error viendo los logs del contenedor de la aplicación:

```shell
docker logs -f mi-app-demo
```

Verás una excepción de Java (`UnknownHostException` o similar).

#### Limpieza

Detén y elimina los contenedores para preparar el siguiente escenario.

```shell
docker rm -f mi-app-demo mi-postgres-demo
```

### Escenario 2: Conexión Exitosa (Con Red Compartida)

Ahora, vamos a hacer lo mismo, pero creando primero una red virtual para que ambos contenedores vivan en ella.

#### Crea una red Docker

```shell
docker network create mi-red-demo
```

#### Inicia el contenedor de PostgreSQL dentro de la red

Usamos el flag `--network` para asignarlo a la red que acabamos de crear.

```shell
docker run -d --name mi-postgres-demo --network mi-red-demo -e POSTGRES_PASSWORD=mysecretpassword postgres:15
```

#### Inicia el contenedor de la aplicación en la misma red

También lo conectamos a mi-red-demo.

```shell
docker run -d -p 7171:7070 --name mi-app-demo --network mi-red-demo -e DB_HOST=mi-postgres-demo mi-app-javalin:1.0
```

#### Comprueba el resultado

Refresca tu navegador en `http://localhost:7171/datos`.

¡Ahora funciona! Verás la lista de registros extraídos de la base de datos. Al estar en la misma red, Docker proporciona
un DNS interno que permite a los contenedores comunicarse usando sus nombres como si fueran nombres de host.

#### Limpieza final

Cuando termines, puedes eliminar los contenedores y la red.

````shell
docker rm -f mi-app-demo mi-postgres-demo
docker network rm mi-red-demo
````

```shell
# Muestra los subcomandos disponibles para trabajar con networks
docker network

```

<h2 id="part-5">🔹 Orquestación con Docker Compose</h2>

### ¿Qué es y por qué usarlo?

Hasta ahora, hemos manejado contenedores, redes y volúmenes con comandos individuales. Esto funciona, pero se vuelve
tedioso y propenso a errores cuando una aplicación tiene múltiples servicios (como nuestra app y su base de datos).

**Docker Compose** es la solución. Es una herramienta que nos permite definir y gestionar una aplicación
multi-contenedor
completa usando un único archivo de configuración: `docker-compose.yml.`

Con un solo comando, Docker Compose puede:

* Construir las imágenes necesarias.
* Crear las redes.
* Crear los volúmenes.
* Iniciar todos los contenedores en el orden correcto.
* Conectarlos entre sí.

### Sintaxis básica de `docker-compose.yml`

```yml
version: '3.8'  # Versión del esquema de Compose | No es necesario en las versiones nuevas de docker

services: # Sección donde se definen los servicios (contenedores)

  nombre-servicio: # Ejemplo: web, db, backend, etc.
    image: nombre:tag   # Imagen que se usará (puede ser oficial o personalizada)
    build: .            # Alternativa: construir la imagen desde un Dockerfile local
    ports:
      - "host:contenedor"  # Ej: "8080:80" para mapear puertos
    volumes:
      - origen:destino     # Para persistencia o compartir archivos
    environment:
      - CLAVE=valor        # Variables de entorno
    depends_on:
      - otro-servicio      # Define dependencias entre servicios
    networks:
      - red-personalizada  # (opcional) red personalizada para comunicación interna

volumes: # (opcional) definición de volúmenes nombrados
  nombre-volumen: { }

networks: # (opcional) definición de redes personalizadas
  red-personalizada:
    driver: bridge

```

#### Notas claves para principiantes:

* `services`: define cada contenedor como un servicio.
* `image`: usa una imagen existente (por ejemplo, `nginx:alpine`).
* `build`: compila desde un `Dockerfile` en la carpeta indicada.
* `volumes`: te permite persistir datos (como bases de datos).
* `depends_on`: asegura el orden de arranque, pero **no espera a que el servicio esté listo** (solo arrancado).
* `networks`: permite aislar o conectar servicios entre sí.

### Ejercicio Práctico: Levantando toda la aplicación con un solo comando

Vamos a usar un archivo `docker-compose.yml` para lanzar nuestra aplicación Javalin y la base de datos PostgreSQL de
forma
coordinada.

#### Paso 1: Crea el archivo `docker-compose.yml`

Crea un archivo llamado `docker-compose.yml` en la raíz de tu proyecto y pega el siguiente contenido:

```yml
# docker-compose.yml
services:
  app:
    build: .
    container_name: mi-app-compose
    ports:
      - "7171:7070"
    environment:
      - DB_HOST=db
    networks:
      - taller-net
    depends_on:
      db:
        condition: service_healthy

  db:
    image: postgres:15-alpine
    container_name: mi-postgres-compose
    environment:
      - POSTGRES_PASSWORD=mysecretpassword
    volumes:
      - db-data:/var/lib/postgresql/data
      #- ./db-data:/var/lib/postgresql/data
    networks:
      - taller-net
    healthcheck:
      test: [ "CMD-SHELL", "pg_isready -U postgres" ]
      interval: 10s
      timeout: 5s
      retries: 5

networks:
  taller-net:
    driver: bridge

volumes:
  db-data:

```

#### Paso 2: Comandos Esenciales de Docker Compose

Abre tu terminal en la raíz del proyecto y prueba los siguientes comandos:

**Para levantar toda la aplicación:**

Este comando leerá el `docker-compose.yml`, construirá la imagen de la app (si es necesario) y levantará ambos servicios
en segundo
plano (`-d`).

```shell
docker compose up -d --build
```

**Para ver los logs de todos los servicios:**

Puedes ver lo que está pasando en ambos contenedores en tiempo real.

```shell
docker compose logs -f
```

**Para detener y eliminar todo:**

Este comando detiene y elimina los contenedores y la red creados por Compose. Si además quieres eliminar los volúmenes (
¡cuidado, esto borra los datos de la base de datos!), añade el flag `-v`.

```shell
docker compose down
```

#### Paso 3: Comprueba el resultado

Con la aplicación corriendo (`docker compose up -d`), abre tu navegador y visita:

* `http://localhost:7171/`: Deberías ver el saludo "Hola Mundo desde Javalin y Docker".
* `http://localhost:7171/datos`: Deberías ver la lista de registros extraídos de la base de datos.

_**Has orquestado una aplicación multi-contenedor completa con un solo archivo y un solo comando, aplicando todos los
conceptos aprendidos en el taller.**_

<h2 id="part-6">🔹 Buenas Prácticas y Tips</h2>

### Optimización de Imágenes

* Usa imágenes base oficiales y pequeñas (ej. `alpine`).
* Utiliza un archivo `.dockerignore` para excluir archivos innecesarios (como `node_modules`, `.git`, etc.) del contexto
  de build.
* Combina múltiples comandos `RUN` en uno solo usando && para reducir el número de capas.

```text
# .dockerignore

# Archivos de Git
.git
.gitignore

# Archivos de build de Maven/Gradle
target/
build/

# Logs y archivos de IDE
*.log
.idea/
*.iml

```

### Estructura de Proyectos

Mantén tu `Dockerfile` y `docker-compose.yml` en la raíz del proyecto.

### Publicar en Docker Hub

Una vez que tu imagen está lista, puedes compartirla con el mundo (o con tus servidores) publicándola en Docker Hub.
Este proceso tiene tres pasos principales: iniciar sesión, construir/etiquetar y subir.

#### Paso 1: Iniciar Sesión

Primero, autentícate con tu cuenta de Docker Hub en la terminal.

```shell
docker login
```

#### Paso 2: Construir y Etiquetar la Imagen

Aquí tienes dos opciones, dependiendo de tu hardware y el destino de la imagen.

**Opción A: Construcción Estándar (Para tu arquitectura local)** Este comando construye la imagen para la misma
arquitectura de tu máquina (ej. Intel/AMD o Mac
M1/M2). Es la forma más simple y directa.

```shell
# Construye y etiqueta la imagen directamente con el nombre final: <usuario>/<repositorio>:<tag>
docker build --no-cache -t fredpena/docker-javalin-app:1.0 .
```

**Opción B: Construcción Multi-Plataforma (Recomendado para compartir)** Si estás en una Mac con chip M1/M2/M3 (
arquitectura
`arm64`) y quieres que tu imagen funcione en servidores estándar (que usan `linux/amd64`), necesitas usar
`docker buildx`.

```shell
# Este comando crea una imagen compatible específicamente con servidores linux/amd64
docker buildx build --no-cache --platform linux/amd64 -o type=docker -t fredpena/docker-javalin-app:1.0 --load .
```

> **Nota:** El flag `--load` carga la imagen resultante en tu Docker local para que puedas probarla. Si quieres
> construir y subirla en un solo paso, puedes usar `--push` en lugar de `--load`.

> **¿Y el flag `docker tag`?** El flag `docker tag` es útil si construiste la imagen con un nombre local primero (ej.
`mi-app-javalin:1.0`) y ahora quieres darle el nombre correcto para subirla a Docker Hub.

```shell
# Si construiste con "mi-app-javalin:1.0", puedes renombrarla así:
docker tag mi-app-javalin:1.0 fredpena/docker-javalin-app:1.0
```

#### Paso 3: Subir la Imagen

Una vez que la imagen está construida y correctamente etiquetada con tu usuario, súbela a Docker Hub.

```shell
docker push fredpena/docker-javalin-app:1.0
```

### Escenario de Despliegue: Usando la Imagen de Docker Hub

Ahora que tu imagen está en Docker Hub, cualquiera (o cualquier servidor) puede usarla sin necesidad de tener el código
fuente. Vamos a usar nuestro archivo `docker-compose.prod.yml` para demostrarlo.

Este archivo está diseñado para "producción", ya que no construye nada (`build`), sino que descarga la imagen
directamente (`image`).

#### Paso 1: Asegúrate de tener el archivo `docker-compose.prod.yml`

Este archivo simula un entorno de despliegue. La diferencia clave está en el servicio `app`, que ahora usa `image`: para
descargar la imagen pre-construida.

##### Diferencia clave entre `docker-compose.yml` y `docker-compose.prod.yml`

| Archivo                   | Propósito                         | Imagen de la app                    |
|---------------------------|-----------------------------------|-------------------------------------|
| `docker-compose.yml`      | Uso local y desarrollo            | Construye localmente con `build: .` |
| `docker-compose.prod.yml` | Despliegue en producción o remoto | Usa una imagen pública con `image:` |

**Resumen**:

- `build: .` se utiliza cuando deseas construir la imagen de Docker localmente a partir del `Dockerfile`.
- `image: fredpena/docker-javalin-app:1.0` se utiliza cuando ya tienes la imagen construida y publicada en Docker Hub (
  ideal para producción).

#### Paso 2: Levanta la aplicación usando el archivo de producción

Para indicarle a Docker Compose que use un archivo diferente al predeterminado, usamos el flag -f.

```shell
# Este comando descargará la imagen desde Docker Hub y levantará los servicios
docker compose -f docker-compose.prod.yml up -d
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

<h2 id="part-8">🔹 Recursos para Continuar Aprendiendo</h2>

* [Play with Docker](https://www.docker.com/play-with-docker/): Un playground online y gratuito para experimentar con
  Docker en tu navegador.
* [Documentación Oficial de Docker](https://docs.docker.com/): La fuente de verdad para todo lo relacionado con Docker.
* [Awesome Docker](https://github.com/veggiemonk/awesome-docker): Un listado curado en GitHub con cientos de recursos,
  tutoriales y herramientas.
* [AwesomeDocker Compose](https://awesome-docker-compose.com/): Una plataforma ideal para descubrir e implementar
  fácilmente más de 700 aplicaciones populares alojadas en servidores propios.

**_¡Gracias por participar en el taller! Esperamos que te sea de gran utilidad._**

