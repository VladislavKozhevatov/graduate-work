-- liquibase formatted sql

-- changeset ai:increase_phone_length
ALTER TABLE users
    ALTER COLUMN phone TYPE VARCHAR(32);