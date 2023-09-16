CREATE TABLE transaction
(
    id        INTEGER      NOT NULL PRIMARY KEY,
    timestamp TIMESTAMP    NOT NULL,
    type      VARCHAR(255) NOT NULL,
    actor     VARCHAR(255) NOT NULL
);

CREATE TABLE transaction_data
(
    id             INTEGER      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    data_key       VARCHAR(255) NOT NULL,
    data_value     VARCHAR(255) NOT NULL,
    transaction_id INTEGER      NOT NULL,
    FOREIGN KEY (transaction_id) REFERENCES transaction (id),
    CONSTRAINT uc_transaction_data_key UNIQUE (transaction_id, data_key)
);