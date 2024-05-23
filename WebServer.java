package ru.krista.yargu;

import io.undertow.Undertow;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;

public class WebServer {

    public static void main(String[] argv) {
        start();
    }
    private static void start() {
        UndertowJaxrsServer server = new UndertowJaxrsServer().start(Undertow.builder().addHttpListener(8081, "0.0.0.0"));
        server.deploy(ru.krista.yargu.RestApplication.class);
        System.out.println("Сервер запущен: http://localhost:8081/");
    }
}
