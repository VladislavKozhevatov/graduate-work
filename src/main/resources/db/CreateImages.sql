-- changeset vkozhevatov:1
CREATE TABLE user_images (
 id BIGINT PRIMARY KEY,
    data BYTEA NOT NULL,
    file_size BIGINT,
    media_type VARCHAR(255)
--    id BIGSERIAL PRIMARY KEY,
--    user_id BIGINT NOT NULL,
--    data BYTEA NOT NULL,
--    media_type VARCHAR(100) NOT NULL,
--    file_size BIGINT NOT NULL,
--    CONSTRAINT fk_user_image FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE ads_images (
    id BIGSERIAL PRIMARY KEY,
    file_path VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    media_type VARCHAR(100) NOT NULL,
    preview BYTEA NOT NULL,  -- двоичные данные
    ad_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_ad_image FOREIGN KEY (ad_id) REFERENCES advertisement(id)
);

-- Добавляем колонки для связей с изображениями
ALTER TABLE users ADD COLUMN image_id BIGINT;
ALTER TABLE users ADD CONSTRAINT fk_user_image
    FOREIGN KEY (image_id) REFERENCES user_images(id);

ALTER TABLE advertisement ADD COLUMN ad_image_id BIGINT;
ALTER TABLE advertisement ADD CONSTRAINT fk_advertisement_image
    FOREIGN KEY (ad_image_id) REFERENCES ads_images(id);