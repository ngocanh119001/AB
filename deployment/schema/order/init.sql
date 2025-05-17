-- Tạo bảng addresses
CREATE TABLE addresses (
    address_id VARCHAR(255) PRIMARY KEY DEFAULT (UUID()),
    recipient_name VARCHAR(255) NOT NULL,
    recipient_phone VARCHAR(50) NOT NULL,
    recipient_address TEXT NOT NULL
);

-- Tạo bảng orders
CREATE TABLE orders (
    order_id VARCHAR(255) PRIMARY KEY DEFAULT (UUID()),
    customer_id VARCHAR(255) NOT NULL,
    vendor_id VARCHAR(255) NOT NULL,
    notes TEXT,
    estimated_delivery_time DATETIME,
    address_id VARCHAR(255) NOT NULL,
    total_price INTEGER NOT NULL,
    order_state ENUM('PENDING', 'TRANSPORTING', 'DELIVERED', 'SUCCEEDED', 'RETURNED') NOT NULL,
    voucher_id VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, 
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_orders_address FOREIGN KEY (address_id) REFERENCES addresses(address_id)
);

-- Tạo bảng order_details
CREATE TABLE order_details (
    order_detail_id VARCHAR(255) PRIMARY KEY DEFAULT (UUID()),
    order_id VARCHAR(255) NOT NULL,
    product_id VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_image VARCHAR(255) NOT NULL,
    first_category VARCHAR(255),
    second_category VARCHAR(255),
    
    CONSTRAINT fk_orders_order FOREIGN KEY (order_id) REFERENCES orders(order_id),
    -- Constraint to ensure order_id and product_id are unique together
    UNIQUE (order_id, product_id)
);

-- Tạo index cho order_id trong order_details
CREATE INDEX idx_order_details_order_id ON order_details(order_id);
CREATE INDEX idx_orders_vendor_id ON orders(vendor_id);
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_updated_at ON orders(updated_at DESC);