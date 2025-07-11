-- liquibase formatted sql

-- changeset kozhevatov:1

ALTER TABLE users
    ADD COLUMN enabled BOOLEAN NOT NULL