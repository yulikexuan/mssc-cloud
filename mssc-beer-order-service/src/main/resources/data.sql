DELETE FROM beer_order_line;
DELETE FROM beer_order;
DELETE FROM customer;

INSERT INTO customer (id, customer_name, api_key, created_date, last_modified_date, version)
VALUES ('46d12cf0-4557-4b76-bc27-73fe5da78f59', 'Tasting Room', 'c3cca631-478b-4e88-9de8-afa8209fe67e', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1)
ON CONFLICT DO NOTHING;