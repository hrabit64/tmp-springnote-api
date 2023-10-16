CREATE TABLE IF NOT EXISTS config
(
    config_pk VARCHAR(20) NOT NULL PRIMARY KEY,
    config_val VARCHAR(200) NOT NULL,
    CONSTRAINT config_val_UNIQUE UNIQUE (config_val)
);

CREATE TABLE IF NOT EXISTS image
(
    image_pk BIGINT NOT NULL PRIMARY KEY,
    image_origin_nm VARCHAR(260) NOT NULL,
    image_origin_type VARCHAR(5) NOT NULL,
    image_save_nm VARCHAR(260) NOT NULL,
    image_create_at TIMESTAMP NOT NULL,
    CONSTRAINT image_origin_nm_UNIQUE UNIQUE (image_origin_nm)
);

CREATE TABLE IF NOT EXISTS series
(
    series_pk BIGINT AUTO_INCREMENT PRIMARY KEY,
    series_title VARCHAR(50) NOT NULL,
    series_description VARCHAR(50) NOT NULL,
    CONSTRAINT series_title_UNIQUE UNIQUE (series_title)
);

CREATE TABLE IF NOT EXISTS post
(
    post_pk BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_title VARCHAR(50) NOT NULL,
    post_content CLOB NOT NULL,
    series_series_pk BIGINT NOT NULL,
    post_create_at TIMESTAMP NOT NULL,
    post_update_at TIMESTAMP NOT NULL,
    post_thumbnail CLOB NOT NULL,
    CONSTRAINT fk_post_series FOREIGN KEY (series_series_pk) REFERENCES series (series_pk)
);

CREATE INDEX IF NOT EXISTS fk_post_series_idx ON post (series_series_pk);

CREATE TABLE IF NOT EXISTS post_content
(
    post_content_pk BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_content_text CLOB NOT NULL,
    post_post_pk BIGINT NOT NULL,
    CONSTRAINT fk_post_content_post1 FOREIGN KEY (post_post_pk) REFERENCES post (post_pk)
);

CREATE INDEX IF NOT EXISTS fk_post_content_post1_idx ON post_content (post_post_pk);

CREATE TABLE IF NOT EXISTS post_convert_content
(
    post_convert_content_pk BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_convert_content_text CLOB NOT NULL,
    post_post_pk BIGINT NOT NULL,
    CONSTRAINT fk_post_content_post10 FOREIGN KEY (post_post_pk) REFERENCES post (post_pk)
);

CREATE INDEX IF NOT EXISTS fk_post_content_post1_idx ON post_convert_content (post_post_pk);

CREATE TABLE IF NOT EXISTS user_info
(
    user_pk CHAR(28) NOT NULL PRIMARY KEY,
    user_is_admin TINYINT NOT NULL,
    user_nm VARCHAR(10) NOT NULL,
    CONSTRAINT user_nm_unique UNIQUE (user_nm)
);

CREATE TABLE IF NOT EXISTS comment
(
    comment_pk BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_depth TINYINT NOT NULL,
    comment_content VARCHAR(500) NOT NULL,
    comment_comment_pk BIGINT NULL,
    user_user_pk CHAR(28) NOT NULL,
    post_post_pk BIGINT NOT NULL,
    CONSTRAINT fk_comment_comment1 FOREIGN KEY (comment_comment_pk) REFERENCES comment (comment_pk),
    CONSTRAINT fk_comment_post1 FOREIGN KEY (post_post_pk) REFERENCES post (post_pk),
    CONSTRAINT fk_comment_user1 FOREIGN KEY (user_user_pk) REFERENCES user_info (user_pk)
);

CREATE INDEX IF NOT EXISTS fk_comment_comment1_idx ON comment (comment_comment_pk);
CREATE INDEX IF NOT EXISTS fk_comment_post1_idx ON comment (post_post_pk);
CREATE INDEX IF NOT EXISTS fk_comment_user1_idx ON comment (user_user_pk);
