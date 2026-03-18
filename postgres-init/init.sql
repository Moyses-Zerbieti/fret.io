-- Bancos
CREATE DATABASE auth_db;
CREATE DATABASE companies_db;
CREATE DATABASE drivers_db;
CREATE DATABASE cargos_db;
CREATE DATABASE operation_db;

-- Usuários isolados
CREATE USER auth_user WITH PASSWORD 'auth123';
CREATE USER companies_user WITH PASSWORD 'companies123';
CREATE USER drivers_user WITH PASSWORD 'drivers123';
CREATE USER cargos_user WITH PASSWORD 'cargos123';
CREATE USER operation_user WITH PASSWORD 'operation123';

-- Permissões restritas (cada usuário só acessa seu banco)
GRANT ALL PRIVILEGES ON DATABASE auth_db TO auth_user;
GRANT ALL PRIVILEGES ON DATABASE companies_db TO companies_user;
GRANT ALL PRIVILEGES ON DATABASE drivers_db TO drivers_user;
GRANT ALL PRIVILEGES ON DATABASE cargos_db TO cargos_user;
GRANT ALL PRIVILEGES ON DATABASE operation_db TO operation_user;
