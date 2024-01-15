drop database if exists `spring-tutorial`;
drop database if exists `spring-tutorial`;
drop user if exists `spring-tutorial`@'localhost';

create database `spring-tutorial`;

create user `spring-tutorial`@'localhost' identified by 'spring-tutorial';
grant all privileges on `spring-tutorial`.* TO `spring-tutorial`@'localhost';

create table `spring-tutorial`.`guests`
(
    id         int auto_increment,
    first_name varchar(32),
    last_name  varchar(32),
    primary key (id)
);

create table `spring-tutorial`.`rooms`
(
    id      int auto_increment,
    name    varchar(32),
    section varchar(32),
    primary key (id)
);
