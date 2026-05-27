CREATE TABLE driver(
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    cpf VARCHAR(18) NOT NULL,
    cnh_number VARCHAR(11) NOT NULL,
    cnh_category VARCHAR(11) NOT NULL,
    cnh_expires_at DATE NOT NULL,
    availability VARCHAR(10) NOT NULL,
    avg_rating NUMERIC(3,2) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_driver_cpf UNIQUE (cpf),
    CONSTRAINT uk_driver_cnh_number UNIQUE (cnh_number)
);