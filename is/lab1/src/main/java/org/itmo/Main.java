package org.itmo;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.servlet.DispatcherServlet;
import org.eclipse.jetty.server.Server;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.itmo.config.WebConfig;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Подключаем твой Spring DispatcherServlet
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(WebConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(ctx);
        context.addServlet(new ServletHolder(dispatcherServlet), "/*");

        server.start();
        server.join();
    }
}