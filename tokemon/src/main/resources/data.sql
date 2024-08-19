CREATE TABLE users (
    email VARCHAR(255) PRIMARY KEY,
    account_id VARCHAR(255) NOT NULL,
    private_key VARCHAR(255) NOT NULL
);

INSERT INTO users (email, account_id, private_key) VALUES ('foo@bar.com', '0.0.4690068', '302e020100300506032b65700422042061a20e3821aca4b9d7e8275b7813e3fbc059b94dd703325e325d0c5c8d0022d8');
INSERT INTO users (email, account_id, private_key) VALUES ('hendrik@openelements.com', '0.0.4624224', '12345');