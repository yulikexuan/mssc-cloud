DROP DATABASE IF EXISTS beer_inventory_service;
DROP USER IF EXISTS beer_inventory_service_guru;
CREATE DATABASE beer_inventory_service ENCODING 'UTF8';
CREATE USER beer_inventory_service_guru WITH SUPERUSER LOGIN PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE beer_inventory_service TO beer_inventory_service_guru;