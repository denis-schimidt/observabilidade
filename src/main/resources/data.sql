INSERT INTO users(id, email, encrypted_password, encrypted_salt, "name") VALUES(1, 'fulano@gmail.com', '123456', '123', 'Fulano da Silva');
INSERT INTO users(id, email, encrypted_password, encrypted_salt, "name") VALUES(2, 'beltrano@gmail.com', '123456', '123', 'Beltrano da Silva');
INSERT INTO addresses(id, user_id, country, "number", state, zip_code, city, street) VALUES(1, 1, 'BR', 45, 'SP', '03733-010', 'SP', 'Rua Virgínio Machado');
INSERT INTO addresses(id, user_id, country, "number", state, zip_code, city, street) VALUES(2, 2, 'BR', 520, 'SP', '02030-010', 'SP', 'Av. Paulista');