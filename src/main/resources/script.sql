-- Drop the old database if it exists
DROP DATABASE IF EXISTS vmix;

-- Create a new database
CREATE DATABASE vmix;
USE vmix;

-- Create the sequences table
CREATE TABLE sequences
(
    Id          INT AUTO_INCREMENT PRIMARY KEY,
    Name        VARCHAR(20) NOT NULL,
    Description VARCHAR(20) NOT NULL
);

-- Insert multiple entries into the sequences table
INSERT INTO sequences (Name, Description)
VALUES ('Brahms piano', 'Utrecht 2024'),
       ('Beethoven piano', 'Den Haag 2024');
