-- Создаем таблицы только если их еще нет, чтобы избежать ошибок при перезапуске

-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Таблица ролей пользователей
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role)
);

CREATE TABLE IF NOT EXISTS coordinates (
                                           id BIGSERIAL PRIMARY KEY,
                                           x BIGINT NOT NULL,
                                           y REAL NOT NULL CHECK (y > -845)
    );

CREATE TABLE IF NOT EXISTS location (
                                        id BIGSERIAL PRIMARY KEY,
                                        x DOUBLE PRECISION NOT NULL,
                                        y INTEGER NOT NULL,
                                        z BIGINT NOT NULL,
                                        name VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS route (
                                     id SERIAL PRIMARY KEY CHECK (id > 0),
    name VARCHAR(255) NOT NULL CHECK (name <> ''),
    coordinates_id BIGINT NOT NULL REFERENCES coordinates(id) ON DELETE RESTRICT,
    creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
    from_location_id BIGINT NOT NULL REFERENCES location(id) ON DELETE RESTRICT,
    to_location_id BIGINT REFERENCES location(id) ON DELETE RESTRICT,
    distance BIGINT NOT NULL CHECK (distance > 1),
    rating BIGINT CHECK (rating > 0)
    );

-- Создаем тестовых пользователей (пароль: password123 для обоих)
-- Хеш BCrypt для "password123": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT INTO users (username, password, enabled, created_at) 
VALUES 
    ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true, NOW()),
    ('user', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true, NOW())
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles (user_id, role) 
VALUES 
    ((SELECT id FROM users WHERE username = 'admin'), 'ROLE_ADMIN'),
    ((SELECT id FROM users WHERE username = 'admin'), 'ROLE_USER'),
    ((SELECT id FROM users WHERE username = 'user'), 'ROLE_USER')
ON CONFLICT DO NOTHING;