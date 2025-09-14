package org.itmo.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

// Этот класс заменяет собой конфигурационный файл web.xml
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // Этот метод указывает на "корневые" конфигурационные классы.
    // Здесь обычно настраиваются сервисы, репозитории, безопасность.
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfig.class};
    }

    // Этот метод указывает на конфигурационные классы для веб-слоя.
    // Здесь настраиваются контроллеры, View Resolvers (как Thymeleaf).
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    // Этот метод определяет, какие URL-адреса будет обрабатывать наш DispatcherServlet.
    // "/" означает, что он будет обрабатывать все входящие запросы.
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}