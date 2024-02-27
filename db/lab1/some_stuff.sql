
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

CREATE TABLE people (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    father_name VARCHAR(20),
    birthday DATE,
    max_age INTEGER,
    action_id BIGINT, --REFERENCES actions(id),
    body_shape_id BIGINT REFERENCES body_shapes(id),
    state_id BIGINT, -- REFERENCES states(id),
    gender genders
);

CREATE TABLE actions (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    owner_id BIGINT REFERENCES people(id) NOT NULL,
    name VARCHAR(20) NOT NULL,
    difficulty difficulties NOT NULL,
    start_time TIMESTAMP,
    end_time TIMESTAMP
);

CREATE TABLE states (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    owner_id BIGINT REFERENCES people(id) NOT NULL,
    name VARCHAR(20) NOT NULL,
    intensity INT CHECK ( 0 <= intensity AND intensity <= 10 ),
    start_time TIMESTAMP,
    end_time TIMESTAMP
);

ALTER TABLE people ADD FOREIGN KEY (action_id) REFERENCES actions(id);
ALTER TABLE people ADD FOREIGN KEY (state_id) REFERENCES states(id);

CREATE TABLE feelings (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    owner_id BIGINT REFERENCES people(id),
    emotion_id BIGINT REFERENCES emotions(id)
);

CREATE TABLE miracles (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unesco_number BIGINT,
    creation_date DATE,
    loss_date DATE
);

CREATE TABLE creators (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    creator BIGINT REFERENCES people(id),
    miracle BIGINT REFERENCES miracles(id)
);

INSERT INTO people (first_name, last_name, father_name, birthday, max_age, gender)
VALUES
    ('Иван', 'Иванов', 'Иванович', '1980-05-15', 80, 'Мужчина'),
    ('Мария', 'Петрова', 'Ивановна', '1995-08-25', 85, 'Женщина'),
    ('Петр', 'Сидоров', 'Александрович', '1975-03-10', 70, 'Мужчина'),
    ('Елена', 'Смирнова', 'Петровна', '2000-11-30', 90, 'Женщина'),
    ('Александр', 'Николаев', 'Александрович', '1990-12-20', 85, 'Мужчина');
INSERT INTO people (first_name, last_name, father_name, birthday, max_age, gender)
VALUES
    ('Клименков', 'Сергей', 'Викторович', '1970-12-20', 999, 'Мужчина'),
    ('Афанасьев', 'Дмитрий', 'Борисович', '1970-02-12', 999, 'Мужчина');
INSERT INTO people (first_name, last_name, birthday, max_age, gender)
VALUES
    ('Хэммонд', 'Джон', '1942-07-07', 100, 'Мужчина');

INSERT INTO actions (owner_id, name, difficulty, start_time, end_time)
VALUES
    (1, 'Бег', 'сложно', '2020-10-30 11:30:00', '2020-10-30 11:30:00'),
    (2, 'Прогулка', 'легко', '2023-02-02 21:00:01', '2023-02-02 21:52:44'),
    (3, 'Чтение', 'легко', '2024-01-01 00:10:10', '2024-01-01 00:42:48'),
    (4, 'Плавание', 'средне', '2010-12-30 09:31:00', '2010-12-30 10:14:23'),
    (5, 'Изучение', 'сложно', '2020-10-30 11:30:00', '2020-10-30 11:30:00'),
    (8, 'Лазание по горам', 'сложно', '1982-08-12 09:30:00', '1982-08-12 23:30:00');


INSERT INTO actions (owner_id, name, difficulty)
VALUES
    (1, 'Саморазвитие', 'сложно'),
    (5, 'Саморазвитие', 'сложно');

INSERT INTO body_shapes (name, body_fat)
VALUES
    ('Худощавый', 10),
    ('Средний', 20),
    ('Плотный', 30),
    ('Плотный и спортивный', 25);

INSERT INTO states (owner_id, name, intensity, start_time, end_time)
VALUES
    (1, 'Усталость', 8, '2020-10-30 11:00:00', '2020-10-30 12:30:00'),
    (2, 'Бодрость', 3,  '2023-02-02 12:00:01', '2023-02-02 18:00:44'),
    (3, 'Расслабление', 5, '2023-02-02 10:45:20', '2023-02-02 17:15:10'),
    (4, 'Напряжение', 7, '2023-02-02 11:10:35', '2023-02-02 17:55:40'),
    (5, 'Концентрация', 6, '2023-02-02 09:15:25', '2023-02-02 15:40:50');

INSERT INTO emotions (name, danger)
VALUES
    ('Счастье', 2),
    ('Грусть', 5),
    ('Страх', 8),
    ('Радость', 3),
    ('Злость', 7);

INSERT INTO feelings (owner_id, emotion_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5);

INSERT INTO miracles (name, unesco_number)
VALUES
    ('Пирамиды Гизы', 234),
    ('Статуя Свободы', 785),
    ('Мачу-Пикчу', 347),
    ('Колизей', NULL),
    ('Стоунхендж', 689),
    ('Парк Юркского периода', 777),
    ('БЭВМ', 812);

INSERT INTO creators (creator, miracle)
VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5),
    (6, 7),
    (7, 7);


INSERT INTO feelings(owner_id, emotion_id)
VALUES
    (6, 4),
    (7, 4);

SELECT p.first_name, p.last_name, m.name
FROM creators c
JOIN people p ON c.creator = p.id
JOIN miracles m ON c.miracle = m.id;

