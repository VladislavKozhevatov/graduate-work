-- changeset author:1
CREATE TABLE user_images (
    id BIGSERIAL PRIMARY KEY,
    data BYTEA NOT NULL,
    file_size BIGINT,
    media_type VARCHAR(255)
);

-- changeset author:2
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    role VARCHAR(10) NOT NULL,
    image_id BIGINT UNIQUE REFERENCES user_images(id) DEFERRABLE INITIALLY DEFERRED
);

-- changeset author:3
CREATE TABLE advertisement (
    pk BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL REFERENCES users(id),
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- changeset author:4
CREATE TABLE ads_images (
    id BIGSERIAL PRIMARY KEY,
    ad_id BIGINT NOT NULL REFERENCES advertisement(pk) ON DELETE CASCADE, -- исправлено с id на pk
    data BYTEA NOT NULL,
    file_size BIGINT NOT NULL,
    media_type VARCHAR(255) NOT NULL
);

-- changeset author:5
ALTER TABLE advertisement ADD COLUMN image_id BIGINT REFERENCES ads_images(id);

-- changeset author:6
CREATE TABLE comment (
    pk BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL REFERENCES users(id),
    ad_id BIGINT NOT NULL REFERENCES advertisement(pk) ON DELETE CASCADE, -- исправлено с id на pk
    text TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- changeset author:7
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_ads_author ON advertisement(author_id);
CREATE INDEX idx_comments_ad ON comment(ad_id);
CREATE INDEX idx_ads_images_ad ON ads_images(ad_id);

-- changeset author:8
ALTER TABLE users ADD CONSTRAINT chk_phone_format
CHECK (phone ~ '^\+7\d{10}$');

ALTER TABLE advertisement ADD CONSTRAINT chk_price
CHECK (price >= 0 AND price <= 10000000);

ALTER TABLE comment ADD CONSTRAINT chk_text_length
CHECK (length(text) >= 8 AND length(text) <= 512);