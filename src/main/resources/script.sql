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
VALUES ('Mozart piano', 'Utrecht'),
       ('Bach Viool', 'Amssterdam'),
       ('Wagner concert', 'Rotterdam'),
       ('Beethoven symfonie', 'Den Haag'),
       ('Vivaldi 4 jaargetijden ', 'Leiden');

INSERT INTO cameras(Ref, Name, SeqId)
VALUES (1, 'Cam1', 1),
       (2, 'Cam2', 1),
       (3, 'Cam3', 1),
       (1, 'Cam1', 2),
       (2, 'Cam2', 2),
       (1, 'Cam3', 3),
       (1, 'Cam1', 4);

INSERT INTO scenes(Number, Name, Description, SeqId, CamId)
VALUES (1, 'Scene 1', 'Descr. 1', 1, 1),
       (2, 'Scene 2', 'Descr. 2', 1, 1),
       (3, 'Scene 3', 'Descr. 3', 1, 2),
       (1, 'Scene 1', 'Descr. 1', 2, 4),
       (2, 'Scene 2', 'Descr. 2', 2, 5),
       (1, 'Scene 1', 'Descr. 1', 3, 6),
       (1, 'Scene 1', 'Descr. 1', 4, 7);




