-- liquibase formatted sql

-- changeset kozhevatov:2

ALTER TABLE users
    ALTER COLUMN password TYPE VARCHAR(128)