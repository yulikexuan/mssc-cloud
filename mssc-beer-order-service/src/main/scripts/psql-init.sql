DROP DATABASE IF EXISTS beer_order_service;
DROP USER IF EXISTS beer_order_service_guru;
CREATE DATABASE beer_order_service ENCODING 'UTF8';
CREATE USER beer_order_service_guru WITH SUPERUSER LOGIN PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE beer_order_service TO beer_order_service_guru;

DROP TABLE IF EXISTS beer_order CASCADE;
DROP TABLE IF EXISTS beer_order_line CASCADE;
DROP TABLE IF EXISTS customer CASCADE;

CREATE TABLE beer_order (
    id VARCHAR(36) NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    version BIGINT,
    customer_ref VARCHAR(255),
    order_status INTEGER,
    order_status_callback_url VARCHAR(255),
    customer_id VARCHAR(36),
    PRIMARY KEY (id)
);

CREATE TABLE beer_order_line (
     id VARCHAR(36) NOT NULL,
     created_date TIMESTAMP,
     last_modified_date TIMESTAMP,
     version BIGINT,
     beer_id VARCHAR(36),
     order_quantity INTEGER,
     quantity_allocated INTEGER,
     upc varchar(255),
     beer_order_id VARCHAR(36),
     PRIMARY KEY (id)
);

CREATE TABLE customer (
    id VARCHAR(36) NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    version BIGINT,
    api_key VARCHAR(36),
    customer_name VARCHAR(36),
    PRIMARY KEY (id)
);

ALTER TABLE beer_order
ADD CONSTRAINT FK5siih2e7vpx70nx4wexpxpji
FOREIGN KEY (customer_id)
REFERENCES customer;

ALTER TABLE beer_order_line
ADD CONSTRAINT FKhkgofxhwx8yw9m3vat8mgtnxs
FOREIGN KEY (beer_order_id)
REFERENCES beer_order;