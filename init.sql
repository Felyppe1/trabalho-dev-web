CREATE TABLE IF NOT EXISTS account (
    cpf VARCHAR(11) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    address TEXT,
    cellphone_number VARCHAR(20),
    balance NUMERIC(12,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS investment (
    expiration DATE NOT NULL,
    category VARCHAR(50) NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    rentability_percent NUMERIC(5,2) NOT NULL,
    rentability_index VARCHAR(20) NOT NULL,
    PRIMARY KEY (expiration, category)
);

CREATE TABLE IF NOT EXISTS transaction (
    cpf VARCHAR(11) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    type VARCHAR(50) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    description TEXT,
    balance_after NUMERIC(12,2),
    PRIMARY KEY (cpf, created_at),
    FOREIGN KEY (cpf) REFERENCES account(cpf)
);

CREATE TABLE IF NOT EXISTS application (
    expiration DATE NOT NULL,
    category VARCHAR(50) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (expiration, category, cpf),
    FOREIGN KEY (expiration, category) REFERENCES investment(expiration, category),
    FOREIGN KEY (cpf) REFERENCES account(cpf)
);
