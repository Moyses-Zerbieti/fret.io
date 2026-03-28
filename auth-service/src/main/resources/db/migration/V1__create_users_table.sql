CREATE TABLE users(
    id uuid NOT NULL,
    document VARCHAR(18) NOT NULL,
    document_type VARCHAR(4) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash text NOT NULL ,
    role VARCHAR(12) NOT NULL,
    user_status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_document UNIQUE (document),
    CONSTRAINT uk_users_email UNIQUE (email)
);