CREATE TABLE refresh_tokens(
    id uuid NOT NULL,
    user_id uuid,
    token_hash TEXT NOT NULL UNIQUE ,
    device_info TEXT NOT NULL ,
    expires_at TIMESTAMP,
    revoked_at TIMESTAMP,
    created_at TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(id)
);