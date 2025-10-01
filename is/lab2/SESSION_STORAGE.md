# Хранение сессий - Конфигурация

## Текущая реализация

**In-Memory (в памяти приложения)**
- Сессии хранятся в JVM (Tomcat HttpSession)
- ❌ Теряются при перезапуске
- ❌ Не работает с несколькими инстансами приложения

## Вариант 1: Spring Session + PostgreSQL (рекомендуется для вашего случая)

### 1. Добавить зависимость в `build.gradle`
```gradle
dependencies {
    // Существующие зависимости...
    
    // Spring Session с JDBC
    implementation 'org.springframework.session:spring-session-jdbc'
}
```

### 2. Создать таблицу для сессий в `init.sql`
```sql
-- Таблица для Spring Session (добавить в конец файла)
CREATE TABLE IF NOT EXISTS spring_session (
    primary_id CHAR(36) NOT NULL,
    session_id CHAR(36) NOT NULL,
    creation_time BIGINT NOT NULL,
    last_access_time BIGINT NOT NULL,
    max_inactive_interval INT NOT NULL,
    expiry_time BIGINT NOT NULL,
    principal_name VARCHAR(100),
    CONSTRAINT spring_session_pk PRIMARY KEY (primary_id)
);

CREATE UNIQUE INDEX IF NOT EXISTS spring_session_ix1 ON spring_session (session_id);
CREATE INDEX IF NOT EXISTS spring_session_ix2 ON spring_session (expiry_time);
CREATE INDEX IF NOT EXISTS spring_session_ix3 ON spring_session (principal_name);

CREATE TABLE IF NOT EXISTS spring_session_attributes (
    session_primary_id CHAR(36) NOT NULL,
    attribute_name VARCHAR(200) NOT NULL,
    attribute_bytes BYTEA NOT NULL,
    CONSTRAINT spring_session_attributes_pk PRIMARY KEY (session_primary_id, attribute_name),
    CONSTRAINT spring_session_attributes_fk FOREIGN KEY (session_primary_id) 
        REFERENCES spring_session(primary_id) ON DELETE CASCADE
);
```

### 3. Настроить в `application.properties`
```properties
# Существующие настройки...

# Spring Session в БД
spring.session.store-type=jdbc
spring.session.jdbc.initialize-schema=never
spring.session.timeout=30m
```

### 4. Добавить аннотацию в Main.java
```java
@SpringBootApplication
@EnableJdbcHttpSession  // <- добавить эту аннотацию
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
```

**Результат:** Сессии сохраняются в PostgreSQL и переживают перезапуск приложения.

---

## Вариант 2: Spring Session + Redis (лучшее для production)

### 1. Добавить зависимости
```gradle
dependencies {
    // Spring Session с Redis
    implementation 'org.springframework.session:spring-session-data-redis'
    implementation 'org.springframework.boot:spring-boot-starter-data-redis'
}
```

### 2. Настроить Redis в `docker-compose.yml`
```yaml
services:
  postgres:
    # существующая конфигурация...

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data

volumes:
  postgres-data:
  redis-data:  # добавить
```

### 3. Настроить в `application.properties`
```properties
# Spring Session в Redis
spring.session.store-type=redis
spring.session.timeout=30m
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

### 4. Добавить аннотацию в Main.java
```java
@SpringBootApplication
@EnableRedisHttpSession  // <- добавить эту аннотацию
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
```

**Преимущества Redis:**
- ⚡ Очень быстро
- 🚀 Поддержка кластеризации
- 💾 Автоматическое истечение сессий (TTL)
- 📊 Идеально для микросервисов

---

## Сравнение вариантов

| Вариант | Скорость | Надежность | Масштабирование | Сложность |
|---------|----------|------------|-----------------|-----------|
| **In-Memory (текущий)** | ⚡⚡⚡ | ❌ | ❌ | ✅ Простой |
| **PostgreSQL** | ⚡ | ✅ | ✅ | ✅ Простой |
| **Redis** | ⚡⚡⚡ | ✅ | ✅✅ | ⚠️ Средний |

---

## Проверка текущей реализации

Чтобы убедиться, что сессии в памяти:

```bash
# 1. Запустить приложение
./gradlew bootRun

# 2. Войти
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}' \
  -c cookies.txt

# 3. Проверить доступ
curl -X GET http://localhost:8080/api/routes -b cookies.txt
# Ответ: 200 OK

# 4. Перезапустить приложение (Ctrl+C и снова ./gradlew bootRun)

# 5. Попробовать снова
curl -X GET http://localhost:8080/api/routes -b cookies.txt
# Ответ: 401 Unauthorized (сессия потеряна!)
```

---

## Рекомендация

Для вашего проекта:

1. **Для разработки:** Оставить как есть (in-memory) - проще
2. **Для production:** Использовать **PostgreSQL** (вариант 1) - не требует дополнительных сервисов
3. **Для микросервисов:** Использовать **Redis** (вариант 2) - лучшая производительность

---

## Текущий статус

✅ Сейчас: **In-Memory (HttpSession)**
- Простая реализация
- Подходит для одиночного инстанса
- Теряется при перезапуске

Хотите настроить персистентное хранение сессий? Скажите, какой вариант предпочитаете.
