CREATE DATABASE fitness_user_db;

DROP TABLE user_details;
CREATE TABLE user_details
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    keycloak_id VARCHAR(255),
    first_name  VARCHAR(255),
    last_name   VARCHAR(255),
    email       VARCHAR(255) UNIQUE NOT NULL,
    username    VARCHAR(255) UNIQUE NOT NULL,
    password    VARCHAR(255)        NOT NULL,
    role        VARCHAR(255),
    created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);