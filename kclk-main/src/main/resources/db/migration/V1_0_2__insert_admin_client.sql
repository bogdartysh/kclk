DELETE FROM client_role WHERE id = 'e1501eea-b9ec-419e-bfcc-c13d0bb58066';
DELETE FROM client WHERE id = 'e1501eea-b9ec-419e-bfcc-c13d0bb58066';
DELETE FROM realm WHERE id = 'admin';

INSERT INTO realm(id, secret) VALUES ('admin', '6DF52mfeO24sXtV-foDouAXZ9ZcGoGF89zwgDNZ9B2M');
INSERT INTO client(id, realm_id, client_id, client_secret) VALUES ('e1501eea-b9ec-419e-bfcc-c13d0bb58066','admin', 'admin', 'password');
INSERT INTO client_role(id, client_id, name) VALUES ('e1501eea-b9ec-419e-bfcc-c13d0bb58066', 'e1501eea-b9ec-419e-bfcc-c13d0bb58066', 'admin');