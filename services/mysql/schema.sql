CREATE DATABASE shop_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE shop_db;

-- Bảng accounts
CREATE TABLE accounts (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    registed_date DATE NOT NULL,
    description TEXT,
    accumulate_point INT NOT NULL DEFAULT 0,
    person_type VARCHAR(50) NOT NULL,
    shop_created_date DATE,
    shop_name VARCHAR(255),
    shop_description TEXT
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

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
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

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
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

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
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Bảng product_categories
CREATE TABLE product_categories (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(255)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

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
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Bảng product_images
CREATE TABLE product_images (
    product_id CHAR(36) PRIMARY KEY,
    cover_image TEXT,
    video TEXT,
    FOREIGN KEY (product_id) REFERENCES products(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Bảng product_common_images
CREATE TABLE product_common_images (
    product_id CHAR(36),
    image VARCHAR(255),
    PRIMARY KEY (product_id, image),
    FOREIGN KEY (product_id) REFERENCES products(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

INSERT INTO accounts (id, name, email, phone_number, registed_date, description, accumulate_point, person_type, shop_created_date, shop_name, shop_description) VALUES
('a1b2c3d4-e5f6-7890-1234-567890abcdef', 'Nguyễn Văn A', 'nguyenvana@example.com', '0123-456-789', '2023-01-10', 'Khách hàng thường xuyên', 150, 'consumer', NULL, NULL, NULL),
('b2c3d4e5-f6g7-8901-2345-678901abcdef', 'Trần Thị B', 'tranthib@example.com', '0987-654-321', '2023-02-15', 'Chủ cửa hàng nhỏ', 300, 'vendor', '2023-02-16', 'Cửa hàng B', 'Chuyên bán quần áo và đồ điện tử'),
('c3d4e5f6-g7h8-9012-3456-789012abcdef', 'Lê Admin', 'admin@example.com', '0555-555-555', '2023-03-01', 'Quản trị viên hệ thống', 0, 'admin', NULL, NULL, NULL);

INSERT INTO product_categories (id, name) VALUES
('d4e5f6g7-h8i9-0123-4567-890123abcdef', 'Điện tử'),
('e5f6g7h8-i9j0-1234-5678-901234abcdef', 'Quần áo'),
('f6g7h8i9-j0k1-2345-6789-012345abcdef', 'Sách');

INSERT INTO products (id, name, price, quantity, description, shop_id, category_id) VALUES
('g7h8i9j0-k1l2-3456-7890-123456abcdef', 'Máy tính xách tay', 15000000.00, 20, 'Máy tính hiệu năng cao', 'b2c3d4e5-f6g7-8901-2345-678901abcdef', 'd4e5f6g7-h8i9-0123-4567-890123abcdef'),
('h8i9j0k1-l2m3-4567-8901-234567abcdef', 'Áo thun cotton', 150000.00, 50, 'Áo thun thoải mái', 'b2c3d4e5-f6g7-8901-2345-678901abcdef', 'e5f6g7h8-i9j0-1234-5678-901234abcdef'),
('i9j0k1l2-m3n4-5678-9012-345678abcdef', 'Sách lập trình', 120000.00, 100, 'Sách học lập trình cơ bản', 'b2c3d4e5-f6g7-8901-2345-678901abcdef', 'f6g7h8i9-j0k1-2345-6789-012345abcdef');

INSERT INTO vouchers (id, name, start_date, end_date, min_value, quantity, type, account_id) VALUES
('j0k1l2m3-n4o5-6789-0123-456789abcdef', 'Giảm 10%', '2023-03-01', '2023-03-31', 200000.00, 50, 'discount', 'a1b2c3d4-e5f6-7890-1234-567890abcdef'),
('k1l2m3n4-o5p6-7890-1234-567890abcdef', 'Miễn phí vận chuyển', '2023-04-01', '2023-04-30', 0.00, 30, 'shipping', 'b2c3d4e5-f6g7-8901-2345-678901abcdef');

INSERT INTO addresses (id, receiver_name, concrete_address, province, ward, type, account_id) VALUES
('l2m3n4o5-p6q7-8901-2345-678901abcdef', 'Nguyễn Văn A', '123 Đường Láng', 'Hà Nội', 'Láng Thượng', 1, 'a1b2c3d4-e5f6-7890-1234-567890abcdef'),
('m3n4o5p6-q7r8-9012-3456-789012abcdef', 'Trần Thị B', '456 Nguyễn Trãi', 'TP Hồ Chí Minh', 'Phường 7', 2, 'b2c3d4e5-f6g7-8901-2345-678901abcdef');

INSERT INTO cards (number, end_date, cvv_code, name, address, zip_code, account_id) VALUES
('4111-1111-1111-1111', '2025-12-31', '123', 'Nguyễn Văn A', '123 Đường Láng', '100000', 'a1b2c3d4-e5f6-7890-1234-567890abcdef'),
('4222-2222-2222-2222', '2026-06-30', '456', 'Trần Thị B', '456 Nguyễn Trãi', '700000', 'b2c3d4e5-f6g7-8901-2345-678901abcdef');

INSERT INTO product_images (product_id, cover_image, video) VALUES
('g7h8i9j0-k1l2-3456-7890-123456abcdef', 'http://example.com/laptop.jpg', 'http://example.com/laptop.mp4'),
('h8i9j0k1-l2m3-4567-8901-234567abcdef', 'http://example.com/aothun.jpg', NULL),
('i9j0k1l2-m3n4-5678-9012-345678abcdef', 'http://example.com/sach.jpg', NULL);



