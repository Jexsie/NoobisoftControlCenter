CREATE TABLE users (
    email VARCHAR(255) PRIMARY KEY,
    account_id VARCHAR(255) NOT NULL,
    private_key VARCHAR(255) NOT NULL
);

INSERT INTO users (email, account_id, private_key) VALUES ('testuser1@example.com', '0.0.5219827', '302e020100300506032b6570042204208b8592176ed47abd7f9137c70aa32f722db93bfd941d4a40f4eddcff6599ea09');
INSERT INTO users (email, account_id, private_key) VALUES ('testuser2@example.com', '0.0.5219828', '302e020100300506032b6570042204205433281c6c49edac46165b04814582d385ffefe32aeca84835ca192431d19bff');
INSERT INTO users (email, account_id, private_key) VALUES ('testuser3@example.com', '0.0.5219834', '302e020100300506032b657004220420bc771e02e190c90cb4f36ed33e215b91240f670c7cb4ee45044dc7ebc1cea52a');
INSERT INTO users (email, account_id, private_key) VALUES ('noah@example.com', '0.0.4690304', '302e020100300506032b6570042204200e5e586f94ee6c2bae8b8ab6b37fddef8daf57f731460bf6d05392c83eecd0a5');
INSERT INTO users (email, account_id, private_key) VALUES ('jexie@example.com', '0.0.4690305', '302e020100300506032b657004220420156bb2f0c5a3be746d8af81c937b092b596c5d18b4c5334020091e95fe2c9949');
INSERT INTO users (email, account_id, private_key) VALUES ('foobar@example.com', '0.0.5219819', '302e020100300506032b65700422042041160e8b4274298c1f4107f5ee0c537fd5b086c34ed49aecf9c1e0e65c3b96f0');
INSERT INTO users (email, account_id, private_key) VALUES ('johndoe@example.com', '0.0.5219835', '302e020100300506032b6570042204202062eb0ecc257b8d1bbda9121cc398f28a06fea930ac025f4e1d45d879243dd1');
INSERT INTO users (email, account_id, private_key) VALUES ('josh@example.com', '0.0.5219838', '302e020100300506032b6570042204202f03ee184d8a01d9d1b4074870b1e0a85e0c711412f01d93eca6464cf99e2022');
INSERT INTO users (email, account_id, private_key) VALUES ('brian@example.com', '0.0.5219844', '302e020100300506032b657004220420278c7be71d45491ba2e4ceabf20e33a575a07839d4844853fb9a9a66d5fd9f71');
INSERT INTO users (email, account_id, private_key) VALUES ('jerry@example.com', '0.0.5219850', '302e020100300506032b6570042204206849642c7665cdcda5dd885cd85b58b5620ee212e4b8796cad62d0db6e5fd0a9');