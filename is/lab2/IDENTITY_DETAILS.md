# Идентификация и хранение сессий - Технические детали

## ⚡ Быстрый ответ

**Вопрос:** Как `authenticationManager.authenticate()` проверяет пароль без обращения к БД?  
**Ответ:** **НЕ БЕЗ!** Обращение к БД есть, оно происходит внутри этого метода.

### Цепочка вызовов:
```
authenticationManager.authenticate()
  ↓
DaoAuthenticationProvider
  ↓
UserService.loadUserByUsername()  ← ЗДЕСЬ!
  ↓
UserRepository.findByUsername()
  ↓
SQL: SELECT * FROM users WHERE username = ?  ← ЗАПРОС К БД!
SQL: SELECT role FROM user_roles WHERE user_id = ?
```

**Когда происходит обращение к БД:**
- ✅ При каждом **входе** (POST /api/auth/login)
- ❌ При последующих запросах (используется сессия из памяти)

---

## 🔐 Как происходит идентификация (пошагово)

### Шаг 1: Вход пользователя (POST /api/auth/login)

**Файл:** `src/main/java/org/itmo/controller/AuthController.java`  
**Строки:** 58-74

```java
@PostMapping("/login")
public ResponseEntity<AuthResponse> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletRequest httpRequest,
        HttpServletResponse httpResponse) {
    
    // 1. AuthenticationManager проверяет username и password
    // ⚠️ ЗДЕСЬ ПРОИСХОДИТ ОБРАЩЕНИЕ К БД!
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            )
    );
    // ↑ Внутри этого вызова:
    //   authenticationManager
    //   → DaoAuthenticationProvider (SecurityConfig.java, строка 65-70)
    //   → UserService.loadUserByUsername() (строка 27-41)
    //   → UserRepository.findByUsername() ← ЗАПРОС К POSTGRESQL!
    //   → PasswordEncoder.matches() (сравнение паролей BCrypt)
    
    // 2. Создаем SecurityContext и сохраняем Authentication
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
    
    // 3. Сохраняем контекст в HTTP сессию
    securityContextRepository.saveContext(context, httpRequest, httpResponse);
    // ↑ Здесь создается JSESSIONID cookie!
```

**Что происходит внутри `authenticate()` (под капотом):**
1. `AuthenticationManager` делегирует `DaoAuthenticationProvider` (настроено в SecurityConfig)
2. `DaoAuthenticationProvider` вызывает `UserService.loadUserByUsername()` ← **ЗАПРОС К БД**
3. `UserRepository.findByUsername()` выполняет `SELECT * FROM users WHERE username = ?`
4. Загружаются роли из `user_roles`
5. `PasswordEncoder.matches()` сравнивает введенный пароль с хешем из БД (BCrypt)
6. Возвращается объект `Authentication` с данными пользователя и ролями

---

## 🔍 Детальная цепочка вызовов при authenticate()

### Что происходит внутри одной строки кода:

```java
Authentication authentication = authenticationManager.authenticate(
    new UsernamePasswordAuthenticationToken("admin", "password123")
);
```

### Пошаговая распаковка:

```
1. AuthController.login() [ваш код]
   ↓
2. authenticationManager.authenticate()
   ↓
3. ProviderManager [Spring Security]
   - Проходит по списку AuthenticationProvider'ов
   ↓
4. DaoAuthenticationProvider [настроено в SecurityConfig.java, строка 65-70]
   - Использует UserDetailsService для загрузки пользователя
   ↓
5. userService.loadUserByUsername("admin") [UserService.java, строка 27-41]
   ↓
6. userRepository.findByUsername("admin") [UserRepository.java]
   ↓
7. ═══════════════════════════════════════════════════
   ║  ЗАПРОС К POSTGRESQL (JPA/Hibernate)             ║
   ═══════════════════════════════════════════════════
   SELECT u.* FROM users u WHERE u.username = 'admin'
   SELECT r.role FROM user_roles r WHERE r.user_id = 1
   ↓
8. Возврат User объекта с ролями [ROLE_ADMIN, ROLE_USER]
   ↓
9. DaoAuthenticationProvider.additionalAuthenticationChecks()
   - Сравнивает пароли:
   ↓
10. passwordEncoder.matches("password123", "$2a$10$N9qo8uLOickgx2ZMRZoMyeI...")
    - BCrypt алгоритм проверяет пароль
    - Извлекает salt из хеша
    - Хеширует введенный пароль с этим salt
    - Сравнивает результаты
    ↓
11. ✓ Пароль совпал!
    ↓
12. Создается Authentication объект:
    Authentication {
        principal: "admin",
        credentials: "password123",
        authorities: [
            SimpleGrantedAuthority("ROLE_ADMIN"),
            SimpleGrantedAuthority("ROLE_USER")
        ],
        authenticated: true
    }
    ↓
13. Возврат в AuthController.login() [ваш код]
```

### Конкретные файлы и классы:

| Шаг | Файл/Класс | Что делает |
|-----|------------|------------|
| 1 | `AuthController.java:58-74` | Точка входа |
| 2 | `SecurityConfig.java:73-75` | Создание AuthenticationManager |
| 3 | `ProviderManager` (Spring) | Поиск подходящего Provider |
| 4 | `SecurityConfig.java:65-70` | Создание DaoAuthenticationProvider |
| 5 | `UserService.java:27-41` | Реализация loadUserByUsername |
| 6 | `UserRepository.java` | JPA интерфейс |
| 7 | **PostgreSQL** | **База данных** |
| 10 | `BCryptPasswordEncoder` (Spring) | Проверка пароля |

### Код конфигурации (SecurityConfig.java):

```java
// Строки 65-70: Связываем UserService с PasswordEncoder
@Bean
public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userService);  // ← Здесь UserService
    authProvider.setPasswordEncoder(passwordEncoder()); // ← Здесь BCrypt
    return authProvider;
}
```

Это связывает все компоненты:
- **UserService** → загружает пользователя из БД
- **PasswordEncoder** → проверяет пароль
- **DaoAuthenticationProvider** → координирует процесс

---

### Шаг 2: Проверка пароля (UserService.java)

**Файл:** `src/main/java/org/itmo/service/UserService.java`  
**Строки:** 24-42

```java
@Override
@Transactional(readOnly = true)
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // 1. Загружаем пользователя из БД
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    
    // 2. Возвращаем UserDetails с хешированным паролем и ролями
    return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),  // ← BCrypt хеш из БД
            user.isEnabled(),
            true,
            true,
            true,
            user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toList())
    );
}
```

**Что происходит:**
1. Загружается пользователь из таблицы `users` (PostgreSQL)
2. Роли загружаются из таблицы `user_roles`
3. Spring Security автоматически сравнивает введенный пароль с хешем через `BCryptPasswordEncoder`

### SQL запросы, которые выполняются:

```sql
-- Запрос 1: Загрузка пользователя (UserRepository.findByUsername)
SELECT 
    u.id, 
    u.username, 
    u.password, 
    u.enabled, 
    u.created_at 
FROM users u 
WHERE u.username = 'admin';

-- Результат:
-- id=1, username='admin', 
-- password='$2a$10$N9qo8uLOickgx2ZMRZoMyeI...', 
-- enabled=true

-- Запрос 2: Загрузка ролей (из-за @ElementCollection в User.java)
SELECT 
    r.role 
FROM user_roles r 
WHERE r.user_id = 1;

-- Результат:
-- ROLE_ADMIN
-- ROLE_USER
```

**Итого: 2 SQL запроса к PostgreSQL при каждом входе!**

---

## 📦 Где хранится сессия

### В коде (конфигурация)

**Файл:** `src/main/java/org/itmo/config/SecurityConfig.java`  
**Строки:** 44-48

```java
.sessionManagement(session -> session
    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // ← Создать сессию при необходимости
    .maximumSessions(1)  // ← Максимум 1 активная сессия на пользователя
    .maxSessionsPreventsLogin(false)  // ← Новый вход вытесняет старую сессию
)
```

**Параметры:**
- `SessionCreationPolicy.IF_REQUIRED` - сессия создается при входе
- `maximumSessions(1)` - один пользователь = одна активная сессия
- `maxSessionsPreventsLogin(false)` - повторный вход закрывает предыдущую сессию

### Физическое хранение

**⚠️ В ПАМЯТИ JVM (не в файлах!)** 

Сессия хранится в объекте `org.apache.catalina.session.StandardManager` (Tomcat):

```
JVM Memory (Heap)
├── SessionManager (Tomcat)
│   └── Map<String, StandardSession>
│       └── "abc123..." → StandardSession
│           ├── JSESSIONID: "abc123..."
│           ├── creationTime: 1696176000000
│           ├── lastAccessedTime: 1696176300000
│           └── attributes:
│               └── "SPRING_SECURITY_CONTEXT" → SecurityContextImpl
│                   └── Authentication
│                       ├── username: "admin"
│                       └── authorities: [ROLE_ADMIN, ROLE_USER]
```

**НЕТ никаких файлов на диске!** Все в оперативной памяти.

---

## 🔄 Процесс идентификации при запросе

### Последующие запросы (с cookie)

**Автоматически обрабатывается Spring Security Filter Chain:**

1. **SessionManagementFilter** (встроенный в Spring Security)
   - Извлекает `JSESSIONID` из cookie
   - Находит сессию в памяти Tomcat

2. **SecurityContextPersistenceFilter** (встроенный)
   - Достает `SecurityContext` из сессии
   - Устанавливает в `SecurityContextHolder`

3. **AuthorizationFilter** (встроенный)
   - Проверяет аннотацию `@PreAuthorize`
   - Сравнивает роли пользователя с требуемыми

**Ваш код не участвует в этом процессе!** Все делает Spring Security автоматически.

---

## 🗂️ Где НЕ хранится сессия (сейчас)

❌ **Не в БД** - таблицы `spring_session` нет  
❌ **Не в файлах** - нет файлов на диске  
❌ **Не в Redis** - зависимость не подключена  

✅ **В памяти JVM** (Tomcat HttpSession)

---

## 📝 Конкретные файлы и их роли

| Файл | Роль | Что делает |
|------|------|------------|
| `SecurityConfig.java` | Конфигурация | Настраивает правила безопасности, session management |
| `UserService.java` | Аутентификация | Загружает пользователя из БД, проверяет пароль |
| `AuthController.java` | Вход/Выход | Создает и удаляет сессии |
| `User.java` | Модель | Хранит данные пользователя (username, пароль, роли) |
| `UserRepository.java` | Доступ к БД | Загружает пользователя из PostgreSQL |
| **БД (PostgreSQL)** | Хранилище | Таблицы `users` и `user_roles` |
| **JVM Memory (Tomcat)** | Сессии | HttpSession с SecurityContext |

---

## 🔍 Как проверить, что сессия в памяти

### Тест 1: Перезапуск приложения

```bash
# 1. Запустить
./gradlew bootRun

# 2. Войти
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}' \
  -c cookies.txt

# 3. Проверить (работает)
curl -X GET http://localhost:8080/api/routes -b cookies.txt
# → 200 OK

# 4. Перезапустить приложение (Ctrl+C, затем снова ./gradlew bootRun)

# 5. Проверить снова (не работает!)
curl -X GET http://localhost:8080/api/routes -b cookies.txt
# → 401 Unauthorized (сессия потеряна!)
```

### Тест 2: Посмотреть в дебаггере

Поставьте breakpoint в `AuthController.login()` на строке:
```java
securityContextRepository.saveContext(context, httpRequest, httpResponse);
```

**Вы увидите:**
- `securityContextRepository` = `HttpSessionSecurityContextRepository`
- Это класс Spring Security, который сохраняет в `HttpSession`
- `HttpSession` = объект Tomcat в памяти JVM

---

## 🎯 Итог

### Где хранится сессия:
**В памяти JVM** (объект `org.apache.catalina.session.StandardSession` в Tomcat)

### Как работает идентификация:

```
Вход:
1. AuthController.login() 
   → AuthenticationManager.authenticate()
   → UserService.loadUserByUsername() (из БД)
   → PasswordEncoder.matches() (BCrypt)
   → securityContextRepository.saveContext() (создает JSESSIONID)

Последующие запросы:
1. Клиент шлет cookie: JSESSIONID=abc123
2. SessionManagementFilter извлекает сессию из памяти Tomcat
3. SecurityContextPersistenceFilter достает Authentication
4. AuthorizationFilter проверяет права доступа
5. Контроллер выполняется
```

### Ключевые файлы:
- **Настройка:** `SecurityConfig.java` (строки 44-48, 50-54)
- **Вход:** `AuthController.java` (строки 58-74)
- **Загрузка пользователя:** `UserService.java` (строки 24-42)
- **Хранение в БД:** таблицы `users` и `user_roles` в PostgreSQL
- **Хранение сессий:** **нигде на диске** - только в RAM

Хотите настроить персистентное хранилище сессий? Могу показать, как добавить таблицу в PostgreSQL для этого.
