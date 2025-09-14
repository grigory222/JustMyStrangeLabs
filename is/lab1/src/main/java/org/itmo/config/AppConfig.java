package org.itmo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.itmo.service") // Говорим Spring искать бины-сервисы в этом пакете
public class AppConfig {
    // Пока что этот класс пуст.
    // Позже мы добавим сюда конфигурацию для JPA (EntityManagerFactory, TransactionManager).
}