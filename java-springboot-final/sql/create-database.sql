create database if not exists web_shop;
use web_shop;

drop table if exists users, roles, products, orders, order_items;

create table users (
    username varchar(255) primary key,
    password varchar(255)
);

CREATE TABLE roles (
    username varchar(255) not null,
    role varchar(250) NOT NULL,
    PRIMARY KEY (username, role),
    FOREIGN KEY (username) REFERENCES users(username)
);

create table products (
    id int primary key auto_increment,
    name varchar(255),
    price decimal(10, 2)
);

create table orders (
    id int primary key auto_increment,
    username varchar(255),
    foreign key (username) references users(username)
);

create table order_items (
    id int primary key auto_increment,
    order_id int,
    product_id int,
    quantity int,
    foreign key (order_id) references orders(id),
    foreign key (product_id) references products(id)
);