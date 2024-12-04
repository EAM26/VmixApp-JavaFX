-- Drop the old database if it exists
DROP DATABASE IF EXISTS vmix;

create database vmix;
use vmix;

create table sequences(
Id int auto_increment primary key,
Name varchar(20) not null,
Description varchar(20) not null
);

insert into sequences(Name, Description) values('Brahms piano', 'Utrecht 2024');