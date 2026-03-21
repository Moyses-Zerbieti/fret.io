CREATE TABLE user_roles(
    id uuid NOT NULL,
   user_id uuid,
   role VARCHAR(30) NOT NULL,
   granted_at TIMESTAMP,
   PRIMARY KEY (id),
   CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id)
);