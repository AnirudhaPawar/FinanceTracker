CREATE TABLE IF NOT EXISTS finance_db.transaction_tags (
                                                           transaction_id BIGINT NOT NULL,
                                                           tag VARCHAR(255) NOT NULL,
    PRIMARY KEY (transaction_id, tag),
    CONSTRAINT fk_transaction
    FOREIGN KEY (transaction_id) REFERENCES finance_db.transactions(id)
    ON DELETE CASCADE
    );
