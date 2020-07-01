DROP DATABASE IF EXISTS beerservice;
DROP USER IF EXISTS beer_service_guru;
CREATE DATABASE beerservice ENCODING 'UTF8';
CREATE USER beer_service_guru WITH SUPERUSER LOGIN PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE beerservice TO beer_service_guru;