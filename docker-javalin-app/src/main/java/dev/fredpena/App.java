package dev.fredpena;

import io.javalin.Javalin;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        var app = Javalin.create()
                .get("/", ctx -> ctx.html("¡Hola Mundo desde Javalin y Docker!"))
                .start(7070);

        System.out.println("Servidor Javalin corriendo en http://localhost:7070");
    }
}
