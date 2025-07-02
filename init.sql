DROP TABLE IF EXISTS application;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS investment;
DROP TABLE IF EXISTS account;

CREATE TABLE IF NOT EXISTS account (
    id VARCHAR(255) PRIMARY KEY,
    number VARCHAR(10) NOT NULL,
    digit VARCHAR(2) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    address TEXT,
    cellphone_number VARCHAR(20) UNIQUE,
    balance NUMERIC(12,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (number, digit)
);

CREATE TABLE IF NOT EXISTS investment (
    expiration DATE NOT NULL,
    category VARCHAR(50) NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    rentability_percent NUMERIC(5,2) NOT NULL,
    rentability_index VARCHAR(20) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (expiration, category)
);

CREATE TABLE IF NOT EXISTS transaction (
    origin_id VARCHAR(255),
    target_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    type VARCHAR(50) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    description TEXT,
    balance_after NUMERIC(12,2),
    PRIMARY KEY (origin_id, target_id, created_at),
    FOREIGN KEY (origin_id) REFERENCES account(id),
    FOREIGN KEY (target_id) REFERENCES account(id)
);

CREATE TABLE IF NOT EXISTS application (
    expiration DATE NOT NULL,
    category VARCHAR(50) NOT NULL,
    id VARCHAR(255) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (expiration, category, id),
    FOREIGN KEY (expiration, category) REFERENCES investment(expiration, category),
    FOREIGN KEY (id) REFERENCES account(id)
);
