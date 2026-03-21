CREATE TABLE password_resets(
    id uuid NOT NULL,
    user_id uuid,
    token_hash TEXT NOT NULL UNIQUE,
    expires_at TIMESTAMP,
    used_at TIMESTAMP,
    created_at TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_password_resets_user FOREIGN KEY (user_id) REFERENCES users(id)
);