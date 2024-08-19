CREATE TABLE users (
    email VARCHAR(255) PRIMARY KEY,
    account_id VARCHAR(255) NOT NULL,
    private_key VARCHAR(255) NOT NULL
);

INSERT INTO users (email, account_id, private_key) VALUES ('user1@example.com', '0.0.4690068', '302e020100300506032b65700422042061a20e3821aca4b9d7e8275b7813e3fbc059b94dd703325e325d0c5c8d0022d8');
INSERT INTO users (email, account_id, private_key) VALUES ('user2@example.com', '0.0.4690291', '302e020100300506032b6570042204206de1b309db2cf7ef1c5704edccac81e79702b717b7b21b35e1c4349675890368');
INSERT INTO users (email, account_id, private_key) VALUES ('user3@example.com', '0.0.4690295', '302e020100300506032b657004220420e971b956ecb1c377ac50de32eb4ace3413e23b1b7648ca5a26670dad498eaabe');
INSERT INTO users (email, account_id, private_key) VALUES ('user4@example.com', '0.0.4690296', '302e020100300506032b657004220420370da07fa6da9546ebcc83d7ab0be98dee7393c53f7c8ad2ff9abc10dc70a24c');
INSERT INTO users (email, account_id, private_key) VALUES ('user5@example.com', '0.0.4690297', '302e020100300506032b657004220420d70905c76f9477b06c335922145a48da8d1e33da33493943ae962777454e2e75');
INSERT INTO users (email, account_id, private_key) VALUES ('user6@example.com', '0.0.4690298', '302e020100300506032b6570042204208955635bf3ed7b417609490b9cf36e3f52db10acbce1a7402dc2b5097e4cdc7a');
INSERT INTO users (email, account_id, private_key) VALUES ('user7@example.com', '0.0.4690301', '302e020100300506032b657004220420ad4a26a62b4d23d3783946cfceb3aa9073bbbdcaa0a4d150068de2bca428e715');
INSERT INTO users (email, account_id, private_key) VALUES ('user8@example.com', '0.0.4690303', '302e020100300506032b657004220420b84df58ef8e6f4bc3175902d1e164ae694b856c75cf558da7c500adc4e76aefe');
INSERT INTO users (email, account_id, private_key) VALUES ('user9@example.com', '0.0.4690304', '302e020100300506032b6570042204200e5e586f94ee6c2bae8b8ab6b37fddef8daf57f731460bf6d05392c83eecd0a5');
INSERT INTO users (email, account_id, private_key) VALUES ('user2@example.com', '0.0.4690305', '302e020100300506032b657004220420156bb2f0c5a3be746d8af81c937b092b596c5d18b4c5334020091e95fe2c9949');