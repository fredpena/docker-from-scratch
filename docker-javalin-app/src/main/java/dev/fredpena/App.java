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
        var app = Javalin.create().get("/", ctx -> {
            log.info("Acceso a la ruta /");
            ctx.html("Hola Mundo desde Javalin y Docker");
        }).get("/datos", ctx -> {
            log.info("Acceso a la ruta /datos");

            StringBuilder responseHtml = new StringBuilder();
            responseHtml.append("Registros desde PostgreSQL:");

            String dbHost = System.getenv().getOrDefault("DB_HOST", "localhost");
            String dbUrl = "jdbc:postgresql://" + dbHost + ":5432/postgres";
            String user = "postgres";
            String password = "mysecretpassword";

            try (Connection conn = DriverManager.getConnection(dbUrl, user, password); Statement stmt = conn.createStatement()) {

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
                responseHtml.append("Conexión exitosa");

                log.info("Datos cargados correctamente desde la base de datos");

            } catch (Exception e) {
                log.error("Error al conectar a la base de datos: {}", e.getMessage(), e);
                responseHtml.append("Error al conectar a la base de datos: ").append(e.getMessage());
            }

            ctx.html(responseHtml.toString());
        }).start(7070);

        log.info("Servidor Javalin corriendo en http://localhost:7070");
    }
}
