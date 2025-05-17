CREATE DATABASE shop_db;
USE shop_db;

-- Bảng accounts
CREATE TABLE accounts (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    phone_number VARCHAR(20),
    registed_date DATE,
    description TEXT,
    accumulate_point INT,
    person_type VARCHAR(50),
    shop_created_date DATE,
    shop_name VARCHAR(255),
    shop_description TEXT
);


-- Bảng vouchers
CREATE TABLE vouchers (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(255),
    start_date DATE,
    end_date DATE,
    min_value DECIMAL(10,2),
    quantity INT,
    type VARCHAR(50),
    account_id CHAR(36),
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

-- Bảng addresses
CREATE TABLE addresses (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    receiver_name VARCHAR(255),
    concrete_address TEXT,
    province VARCHAR(100),
    ward VARCHAR(100),
    type SMALLINT,
    account_id CHAR(36),
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

-- Bảng cards
CREATE TABLE cards (
    number VARCHAR(50) PRIMARY KEY,
    end_date DATE,
    cvv_code VARCHAR(10),
    name VARCHAR(255),
    address TEXT,
    zip_code VARCHAR(20),
    account_id CHAR(36),
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

-- Bảng product_categories
CREATE TABLE product_categories (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(255)
);

-- Bảng products
CREATE TABLE products (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(255),
    price DECIMAL(10,2),
    quantity INT,
    description TEXT,
    shop_id CHAR(36),
    category_id CHAR(36),
    FOREIGN KEY (shop_id) REFERENCES accounts(id),
    FOREIGN KEY (category_id) REFERENCES product_categories(id)
);
-- Bảng product_images
CREATE TABLE product_images (
    product_id CHAR(36) PRIMARY KEY,
    cover_image TEXT,
    video TEXT,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Bảng product_common_images
CREATE TABLE product_common_images (
    product_id CHAR(36),
    image VARCHAR(255),
    PRIMARY KEY (product_id, image),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
