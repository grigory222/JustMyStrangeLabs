
CREATE TYPE difficulties as ENUM (
    'невозможно',
    'сложно',
    'средне',
    'легко'
);

CREATE TYPE genders as ENUM (
    'Мужчина',
    'Женщина'
);

CREATE TABLE body_shapes (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    body_fat INT NOT NULL
);


CREATE TABLE emotions (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    danger INT CHECK ( 0 <= danger AND danger <= 10 )
);

CREATE TABLE actions (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE states (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE people (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    father_name VARCHAR(20),
    birthday DATE,
    max_age INTEGER,
    body_shape_id BIGINT REFERENCES body_shapes(id),
    gender genders
);

CREATE TABLE actions_history (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    action_id BIGINT REFERENCES actions(id),
    owner_id BIGINT REFERENCES people(id),
    difficulty difficulties NOT NULL,
    start_time TIMESTAMP,
    end_time TIMESTAMP
);

CREATE TABLE states_history (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    state_id BIGINT REFERENCES states(id),
    owner_id BIGINT REFERENCES people(id),
    intensity INT CHECK ( 0 <= intensity AND intensity <= 10 ),
    start_time TIMESTAMP,
    end_time TIMESTAMP
);

CREATE TABLE feelings (
    owner_id BIGINT REFERENCES people(id),
    emotion_id BIGINT REFERENCES emotions(id),
    PRIMARY KEY (owner_id, emotion_id)
);

CREATE TABLE miracles (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unesco_number BIGINT,
    creation_date DATE,
    loss_date DATE
);

CREATE TABLE creators (
    creator BIGINT REFERENCES people(id),
    miracle BIGINT REFERENCES miracles(id),
    PRIMARY KEY (creator, miracle)
);

CREATE TABLE alerts (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    human BIGINT REFERENCES people(id) NOT NULL,
    danger INTEGER,
    emotion BIGINT REFERENCES emotions(id) NOT NULL,
    time timestamp NOT NULL
);


CREATE OR REPLACE FUNCTION update_alerts()
RETURNS TRIGGER AS $$
BEGIN
    IF (SELECT danger from emotions where NEW.emotion_id = id) > 8
    THEN
        insert into alerts values
            ( NEW.owner_id,
              (SELECT danger from emotions where NEW.emotion_id = id),
              NEW.emotion_id,
              NOW()
            );
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_alerts
AFTER INSERT OR UPDATE ON feelings
FOR EACH ROW
EXECUTE FUNCTION update_alerts();

