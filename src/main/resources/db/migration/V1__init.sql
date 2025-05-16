CREATE TABLE finance_db.users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE finance_db.categories (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL
);

CREATE TABLE finance_db.transactions (
                              id BIGSERIAL PRIMARY KEY,
                              user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                              category_id INTEGER REFERENCES categories(id) ON DELETE SET NULL,
                              type VARCHAR(10) CHECK (type IN ('income', 'expense')),
                              amount DECIMAL(10, 2) NOT NULL,
                              note TEXT,
                              created_at TIMESTAMP NOT NULL
);

CREATE TABLE finance_db.budgets (
                         id SERIAL PRIMARY KEY,
                         user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                         category_id INTEGER REFERENCES categories(id) ON DELETE SET NULL,
                         month VARCHAR(10) NOT NULL,
                         amount DECIMAL(10, 2) NOT NULL
);
