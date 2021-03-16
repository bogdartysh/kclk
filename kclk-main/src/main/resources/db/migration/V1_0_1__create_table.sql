CREATE TABLE IF NOT EXISTS realm (
    id VARCHAR(255),
    enabled BOOLEAN DEFAULT (TRUE),
    displayName VARCHAR(255),
    secret VARCHAR(1024)
);

CREATE TABLE IF NOT EXISTS client (
    id UUID,
    realm_id VARCHAR(255),
    client_id VARCHAR(255),
    enabled BOOLEAN DEFAULT (TRUE),
    access_token_lifespan BIGINT DEFAULT (15*60),
    client_secret VARCHAR (1024),
    foreign key (realm_ID) references realm(id)
);

CREATE TABLE IF NOT EXISTS client_role (
    id UUID,
    client_id UUID,
    name VARCHAR(255),
    foreign key (client_id) references client(id)
)

