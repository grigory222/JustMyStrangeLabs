# Аутентификация и авторизация (Spring Security)

## Описание

Реализована **session-based аутентификация** с поддержкой ролей `USER` и `ADMIN`.

## Механизм работы

1. **Вход:** POST `/api/auth/login` → сервер возвращает cookie `JSESSIONID`
2. **Запросы:** Клиент отправляет cookie в каждом запросе
3. **Проверка:** Spring Security проверяет сессию и права доступа
4. **Выход:** POST `/api/auth/logout` → сессия удаляется

> **Примечание:** Это НЕ JWT токены. Используются HTTP сессии (stateful).

## Тестовые пользователи

```
admin / password123  → роли: ADMIN, USER
user  / password123  → роль: USER
```

## API Endpoints

### Публичные (без входа)
```bash
# Регистрация
POST /api/auth/register
{
  "username": "newuser",
  "password": "password123"
}

# Вход
POST /api/auth/login
{
  "username": "admin",
  "password": "password123"
}
```

### Требуется аутентификация
```bash
# Проверить текущего пользователя
GET /api/auth/me

# Выйти
POST /api/auth/logout

# Работа с маршрутами (доступно USER и ADMIN)
GET    /api/routes
POST   /api/routes
PATCH  /api/routes/{id}
DELETE /api/routes/{id}
```

### Только для ADMIN
```bash
# Удалить все маршруты по рейтингу
DELETE /api/routes/by-rating?rating=5

# Админ-панель
GET /api/admin/users
```

## Быстрый тест

```bash
# 1. Войти
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}' \
  -c cookies.txt

# 2. Использовать API
curl -X GET http://localhost:8080/api/routes \
  -b cookies.txt

# 3. Админ операция
curl -X DELETE "http://localhost:8080/api/routes/by-rating?rating=5" \
  -b cookies.txt

# 4. Выйти
curl -X POST http://localhost:8080/api/auth/logout \
  -b cookies.txt
```

## Коды ответов

- **200** - успех
- **401** - не авторизован (нужен вход)
- **403** - нет прав доступа (нужна роль ADMIN)
- **409** - username уже существует

## Структура

### Основные классы
- `SecurityConfig` - конфигурация Spring Security
- `User` - модель пользователя с ролями
- `UserService` - загрузка пользователей, регистрация
- `AuthController` - регистрация, вход, выход
- `AdminController` - административные операции

### База данных
```sql
users (id, username, password, enabled, created_at)
user_roles (user_id, role)
```

### Защита endpoints
```java
// Требуется аутентификация
@PreAuthorize("isAuthenticated()")

// Только для админа
@PreAuthorize("hasRole('ADMIN')")
```

## Session vs JWT

**Текущая реализация (Session):**
- ✅ Cookie JSESSIONID
- ✅ Сессия на сервере
- ✅ Проще в реализации
- ❌ Stateful (не для микросервисов)

**JWT (если нужно):**
- Токен в заголовке Authorization
- Stateless
- Для микросервисной архитектуры

## Безопасность

- Пароли хешируются BCrypt
- CSRF отключен (REST API)
- Максимум 1 активная сессия на пользователя
- Валидация входных данных (Jakarta Validation)

## Troubleshooting

**401 Unauthorized** → Выполните вход через `/api/auth/login`  
**403 Forbidden** → Нужна роль ADMIN  
**Cookie не работает** → Используйте `-c cookies.txt` и `-b cookies.txt` в curl
