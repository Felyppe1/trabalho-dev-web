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
    rentability_index VARCHAR(20),
    is_available BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (expiration, category)
);

CREATE TABLE IF NOT EXISTS transaction (
    id VARCHAR(255) PRIMARY KEY,
    origin_id VARCHAR(255),
    target_id VARCHAR(255),
    type VARCHAR(50) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    description TEXT,
    balance_after NUMERIC(12,2),
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (origin_id) REFERENCES account(id),
    FOREIGN KEY (target_id) REFERENCES account(id)
);


CREATE TABLE IF NOT EXISTS application (
    id VARCHAR(255) PRIMARY KEY,
    expiration DATE NOT NULL,
    category VARCHAR(50) NOT NULL,
    account_id VARCHAR(255) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (expiration, category) REFERENCES investment(expiration, category),
    FOREIGN KEY (account_id) REFERENCES account(id)
);


INSERT INTO investment (expiration, category, unit_price, rentability_percent, rentability_index, is_available) VALUES
('2028-01-01', 'TESOURO PREFIXADO', 731.42, 13.44, NULL, true),
('2032-01-01', 'TESOURO PREFIXADO', 440.71, 13.54, NULL, true),
('2035-01-01', 'TESOURO PREFIXADO', 821.23, 13.65, NULL, true),

('2028-03-01', 'TESOURO SELIC', 16867.79, 0.0522, 'SELIC', true),
('2031-03-01', 'TESOURO SELIC', 16793.29, 0.1035, 'SELIC', true),

('2029-05-15', 'TESOURO IPCA+', 3431.34, 7.50, 'IPCA', true),
('2040-08-15', 'TESOURO IPCA+', 1648.50, 6.95, 'IPCA', true),
('2050-08-15', 'TESOURO IPCA+', 878.19, 6.79, 'IPCA', true),
('2035-05-15', 'TESOURO IPCA+', 4210.20, 7.19, 'IPCA', true),

('2069-12-15', 'TESOURO RENDA+', 506.09, 6.82, 'IPCA', true),
('2074-12-15', 'TESOURO RENDA+', 360.82, 6.85, 'IPCA', true),
('2079-12-15', 'TESOURO RENDA+', 257.53, 6.87, 'IPCA', true),
('2084-12-15', 'TESOURO RENDA+', 185.11, 6.87, 'IPCA', true),

('2030-12-15', 'TESOURO EDUCA+', 3636.68, 7.86, 'IPCA', true),
('2031-12-15', 'TESOURO EDUCA+', 3402.42, 7.62, 'IPCA', true),
('2032-12-15', 'TESOURO EDUCA+', 3185.54, 7.46, 'IPCA', true),
('2033-12-15', 'TESOURO EDUCA+', 2983.34, 7.35, 'IPCA', true),
('2034-12-15', 'TESOURO EDUCA+', 2792.12, 7.28, 'IPCA', true);