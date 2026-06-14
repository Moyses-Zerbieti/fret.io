CREATE TABLE driver_rating(
    id uuid NOT NULL,
    driver_id uuid NOT NULL,
    cargo_id uuid NOT NULL,
    rated_by_user_id uuid NOT NULL,
    score NUMERIC(3,2) NOT NULL,
    comment VARCHAR (254) NOT NULL,
    created_at TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT fk_driver_rating_driver FOREIGN KEY (driver_id) REFERENCES driver(id)

);