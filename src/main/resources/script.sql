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


INSERT INTO sequences (Name, Description, IPAddress, Port)
VALUES ('Mozart piano', 'Utrecht', '86.86.79.147', '8088'),
       ('Bach Viool', 'Amssterdam', '192.168.0.1', '8080'),
       ('Wagner concert', 'Rotterdam', '192.168.0.1', '8080'),
       ('Beethoven symfonie', 'Den Haag', '192.168.0.1', '8080'),
       ('Vivaldi 4 jaargetijden ', 'Leiden', '192.168.0.1', '8080');

INSERT INTO cameras(Ref, Name, SeqId)
VALUES (1, 'Cam1', 1),
       (2, 'Cam2', 1),
       (3, 'Cam3', 1),
       (1, 'Cam1', 2),
       (2, 'Cam2', 2),
       (1, 'Cam3', 3),
       (1, 'Cam1', 4);

INSERT INTO scenes(Number, Name, Description, SeqId, CamId)
VALUES
--     (1, 'Opening', 'Descr. 1', 1, 1),
--        (2, 'Dirigent ', 'Descr. 2', 1, 1),
--        (3, 'Eindshot', 'Descr. 3', 1, 2),
(1, '1', '1', 1, 1),
(2, '2', '2', 1, 1),
(3, '3', '3', 1, 1),
(4, '4', '4', 1, 1),
(5, '5', '5', 1, 1),
(6, '6', '6', 1, 1),
(7, '7', '7', 1, 1),
(8, '8', '8', 1, 1),
(9, '9', '9', 1, 1),
(10, '10', '10', 1, 1),
(11, '11', '11', 1, 1),
(12, '12', '12', 1, 1),
(13, '13', '13', 1, 1),
(14, '14', '14', 1, 1),
(15, '15', '15', 1, 1),
(16, '16', '16', 1, 1),
(17, '17', '17', 1, 1),
(18, '18', '18', 1, 1),
(19, '19', '19', 1, 1),
(20, '20', '20', 1, 1),
(21, '21', '21', 1, 1),
(22, '22', '22', 1, 1),
(23, '23', '23', 1, 1),
(24, '24', '24', 1, 1),
(25, '25', '25', 1, 1),
(26, '26', '26', 1, 1),
(27, '27', '27', 1, 1),
(28, '28', '28', 1, 1),
(29, '29', '29', 1, 1),
(30, '30', '30', 1, 1),
(1, 'Zaal', 'Descr. 1', 2, 4),
(2, 'Viool 2', 'Descr. 2', 2, 5),
(1, 'Zaal', 'Descr. 1', 3, 6),
(1, 'Opening', 'Descr. 1', 4, 7);




