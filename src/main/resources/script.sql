-- -- Drop the table if it already exists (SQLite doesn't support DROP DATABASE)
-- DROP TABLE IF EXISTS sequences;
--
-- -- Create the table
-- CREATE TABLE sequences (
--                            Id INTEGER PRIMARY KEY, -- Auto-increment is implicit with INTEGER PRIMARY KEY
--                            Name TEXT NOT NULL, -- Use TEXT instead of VARCHAR
--                            Description TEXT NOT NULL
-- );
--
-- -- Insert data into the table
-- INSERT INTO sequences (Name, Description)
-- VALUES
--     ('Brahms piano', 'Utrecht 2024'),
--     ('Beethoven piano', 'Den Haag 2024');




-- DROP TABLE IF EXISTS sequences;
--
--
-- CREATE TABLE sequences (
--                            Id INTEGER PRIMARY KEY,
--                            Name TEXT NOT NULL,
--                            Description TEXT NOT NULL
-- );


INSERT INTO sequences (Name, Description)
VALUES
    ('Mozart piano', 'Utrecht 2024'),
    ('Mozart piano', 'Utrecht 2024'),
    ('Mozart piano', 'Utrecht 2024'),
    ('Mozart piano', 'Utrecht 2024'),
    ('Mozart piano', 'Utrecht 2024');


