CREATE TABLE finance_db.recurring_transactions (
                                                   id BIGSERIAL PRIMARY KEY,
                                                   user_id BIGINT NOT NULL,
                                                   category_id INTEGER NOT NULL,
                                                   type VARCHAR(10) NOT NULL,
                                                   amount NUMERIC(10,2) NOT NULL,
                                                   note TEXT,
                                                   frequency VARCHAR(20) NOT NULL,
                                                   start_date DATE NOT NULL,
                                                   end_date DATE,
                                                   next_due_date DATE NOT NULL,
                                                   active BOOLEAN DEFAULT TRUE,
                                                   created_at TIMESTAMP NOT NULL,
                                                   CONSTRAINT fk_rec_tx_user FOREIGN KEY (user_id) REFERENCES finance_db.users(id),
                                                   CONSTRAINT fk_rec_tx_category FOREIGN KEY (category_id) REFERENCES finance_db.categories(id)
);
