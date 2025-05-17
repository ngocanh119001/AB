-- Tạo bảng orders
CREATE TABLE orders (
    order_id VARCHAR(255) PRIMARY KEY,
    vendor_id VARCHAR(255) NOT NULL,
    customer_id VARCHAR(255) NOT NULL,
    notes TEXT,
    estimated_delivery_time DATETIME,
    address_id VARCHAR(255) NOT NULL,
    order_state ENUM('PENDING', 'TRANSPORTING', 'DELIVERED', 'SUCCEEDED', 'RETURNED') NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- Tạo bảng addresses
CREATE TABLE addresses (
    address_id VARCHAR(255) PRIMARY KEY,
    recipient_name VARCHAR(255) NOT NULL,
    recipient_phone VARCHAR(50) NOT NULL,
    recipient_address TEXT NOT NULL
    
    CONSTRAINT fk_orders_address FOREIGN KEY (address_id) REFERENCES addresses(address_id)
);