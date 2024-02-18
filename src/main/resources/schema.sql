USE prueba;
DROP DATABASE dental_moovi;
CREATE DATABASE dental_moovi;

USE dental_moovi;

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(70) NOT NULL UNIQUE,
    id_parent_category BIGINT, FOREIGN KEY (id_parent_category) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(70) NOT NULL,
    content_type VARCHAR(10) NOT NULL,
    data LONGBLOB NOT NULL,
    id_product BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(70) NOT NULL UNIQUE,
    description VARCHAR(255),
    short_description VARCHAR(70),
    unit_price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    open_to_public BOOLEAN NOT NULL,
    id_category BIGINT NOT NULL, FOREIGN KEY (id_category) REFERENCES categories(id),
    id_main_image BIGINT, FOREIGN KEY (id_main_image) REFERENCES images(id)
);

ALTER TABLE images
ADD FOREIGN KEY (id_product) REFERENCES products(id);

CREATE TABLE IF NOT EXISTS users(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(70) NOT NULL UNIQUE,
    cel_phone VARCHAR(13) NOT NULL,
    birthdate DATE,
    gender ENUM('FEMALE', 'MALE', 'OTHER', 'UNDEFINED') NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users_roles(
    id_user BIGINT,
    id_role BIGINT,
    PRIMARY KEY (id_user, id_role),
    FOREIGN KEY (id_user) REFERENCES users(id),
    FOREIGN KEY (id_role) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS departaments(
    id INT PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS municipaly_city(
    id INT PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    id_departament INT NOT NULL, FOREIGN KEY (id_departament) REFERENCES departaments(id)
);

CREATE TABLE IF NOT EXISTS addresses(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    address VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(13) NOT NULL,
    id_municipaly_city INT NOT NULL, FOREIGN KEY (id_municipaly_city) REFERENCES municipaly_city(id),
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users_addresses(
    id_user BIGINT,
    id_address BIGINT,
    PRIMARY KEY (id_user, id_address),
    FOREIGN KEY (id_user) REFERENCES users(id),
    FOREIGN KEY (id_address) REFERENCES addresses(id)
);

CREATE TABLE IF NOT EXISTS orders(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_file LONGBLOB,
    status ENUM('COMPLETE', 'CANCEL', 'PENDING') NOT NULL,
    id_user BIGINT NOT NULL, FOREIGN KEY (id_user) REFERENCES users(id),
    id_address BIGINT NOT NULL, FOREIGN KEY (id_address) REFERENCES addresses(id)
);

CREATE TABLE IF NOT EXISTS orders_products(
    id_order BIGINT,
    id_product BIGINT,
    PRIMARY KEY (id_order, id_product),
    amount INT NOT NULL,
    FOREIGN KEY (id_order) REFERENCES orders(id),
    FOREIGN KEY (id_product) REFERENCES products(id)
);

