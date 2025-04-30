--liquibase formatted sql

--changeset carlos:2025_04_29_0
--comment: Create tables
CREATE TABLE IF NOT EXISTS device
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(240) NOT NULL,
    brand VARCHAR(24) NOT NULL,
    state integer NOT NULL,
    creation_time TIMESTAMP NOT NULL
)