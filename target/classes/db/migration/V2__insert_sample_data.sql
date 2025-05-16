-- Insert sample categories
INSERT INTO finance_db.categories (name)
VALUES ('Groceries'),
       ('Rent'),
       ('Utilities'),
       ('Entertainment'),
       ('Salary'),
       ('Freelance');

-- Insert sample users (if needed; skip if users already exist)
INSERT INTO finance_db.users (username, email, password)
VALUES ('test1', 'test1@gmail.com', '$2a$10$cdOSiLB4NFKIU/XYx7n4BOZcJI8zRNHQsnABDYCMBPDKVpoEiS3ny'), -- Test@123
       ('test2', 'test2@gmail.com', '$2a$10$cdOSiLB4NFKIU/XYx7n4BOZcJI8zRNHQsnABDYCMBPDKVpoEiS3ny'); -- Test@123

-- Insert sample transactions
INSERT INTO finance_db.transactions (user_id, category_id, type, amount, note, created_at)
VALUES (1, 5, 'income', 5000.00, 'Monthly salary', NOW()),
       (1, 1, 'expense', 150.75, 'Supermarket shopping', NOW() - INTERVAL '2 days'),
       (1, 2, 'expense', 1200.00, 'Monthly apartment rent', NOW() - INTERVAL '10 days'),
       (2, 6, 'income', 800.00, 'Freelance web project', NOW() - INTERVAL '5 days'),
       (2, 3, 'expense', 90.00, 'Electricity bill', NOW() - INTERVAL '3 days'),
       (2, NULL, 'expense', 50.00, 'Uncategorized expense', NOW());

-- Insert sample budgets
INSERT INTO finance_db.budgets (user_id, category_id, month, amount)
VALUES (1, 1, '2025-05', 600.00),
       (1, 2, '2025-05', 1200.00),
       (2, 3, '2025-05', 200.00),
       (2, NULL, '2025-05', 300.00);
