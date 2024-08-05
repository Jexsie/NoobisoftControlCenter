CREATE TABLE users (
    email VARCHAR(255) PRIMARY KEY,
    account_id VARCHAR(255) NOT NULL
);

INSERT INTO users (email, account_id) VALUES ('foo@bar.com', '0.0.4624224');
INSERT INTO users (email, account_id) VALUES ('hendrik@openelements.com', '0.0.4624224');