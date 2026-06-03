CREATE TABLE vehicle(
    id uuid NOT NULL,
    driver_id uuid NOT NULL,
    plate VARCHAR(7) NOT NULL,
    type_vehicle VARCHAR(254) NOT NULL,
    brand VARCHAR(254) NOT NULL,
    model VARCHAR(254) NOT NULL,
    vehicle_year INTEGER NOT NULL,
    capacity_kg NUMERIC(10,2) NOT NULL,
    capacity_m3 NUMERIC(10,3) NOT NULL,
    status_vehicle VARCHAR(254) NOT NULL,
    created_at TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT uk_vehicle_plate UNIQUE (plate),

    CONSTRAINT fk_driver_id FOREIGN KEY(driver_id) REFERENCES driver(id)
);