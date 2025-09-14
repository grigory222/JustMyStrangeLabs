-- Создаем таблицы только если их еще нет, чтобы избежать ошибок при перезапуске
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
    coordinates_id BIGINT NOT NULL UNIQUE REFERENCES coordinates(id) ON DELETE RESTRICT,
    creation_date TIMESTAMPTZ NOT NULL,
    from_location_id BIGINT NOT NULL REFERENCES location(id) ON DELETE RESTRICT,
    to_location_id BIGINT REFERENCES location(id) ON DELETE RESTRICT,
    distance BIGINT NOT NULL CHECK (distance > 1),
    rating BIGINT CHECK (rating > 0)
    );