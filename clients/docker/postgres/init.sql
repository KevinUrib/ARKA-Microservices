CREATE DATABASE clientsdb;

\connect clientsdb;

CREATE TABLE clients (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    address VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO clients (name, email, phone, address, created_at, updated_at) 
VALUES ('John Doe', 'john.doe@example.com', '123-456-7890', '123 Main St, Anytown, USA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO clients (name, email, phone, address, created_at, updated_at) 
VALUES ('Jane Smith', 'jane.smith@example.com', '123-456-7891', '456 Elm St, Anytown, USA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO clients (name, email, phone, address, created_at, updated_at)
VALUES ('Alice Johnson', 'alice.johnson@example.com', '123-456-7892', '789 Oak St, Anytown, USA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO clients (name, email, phone, address, created_at, updated_at)
VALUES ('Bob Brown', 'bob.brown@example.com', '123-456-7893', '321 Pine St, Anytown, USA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);