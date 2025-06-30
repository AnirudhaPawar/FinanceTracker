ALTER TABLE finance_db.users
    ADD COLUMN firstname VARCHAR(100) NOT NULL default '',
    ADD COLUMN lastname VARCHAR(100) NOT NULL default '',
    ADD COLUMN contact VARCHAR(15);