
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


CREATE TABLE actions (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    difficulty difficulties NOT NULL,
    duration interval NOT NULL
);

CREATE TABLE body_shapes (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    body_fat INT NOT NULL
);

CREATE TABLE states (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    intensity INT CHECK ( 0 <= intensity AND intensity <= 10 )
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
    father_name VARCHAR(20) NOT NULL,                         
    birthday DATE,                                            
    max_age INTEGER,                                          
    action_id BIGINT REFERENCES actions(id),                  
    body_shape_id BIGINT REFERENCES body_shapes(id),          
    state_id BIGINT REFERENCES states(id),                    
    gender genders
);

CREATE TABLE feelings (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    owner_id BIGINT REFERENCES people(id),
    emotion_id BIGINT REFERENCES emotions(id)
);

CREATE TABLE miracles (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unesco_number BIGINT
);

CREATE TABLE creators (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    creator BIGINT REFERENCES people(id),
    miracle BIGINT REFERENCES miracles(id)
);
