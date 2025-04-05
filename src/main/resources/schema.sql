-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) UNIQUE,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    phone_number VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255) CHECK (role IN ('CUSTOMER', 'ADMIN', 'KITCHEN_STAFF'))
);

-- Menu items table
CREATE TABLE IF NOT EXISTS menu_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id VARCHAR(255) UNIQUE,
    item_name VARCHAR(255),
    item_description VARCHAR(1000),
    item_price FLOAT NOT NULL,
    item_picture VARCHAR(255),
    available BOOLEAN NOT NULL
);

-- Carts table
CREATE TABLE IF NOT EXISTS carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id VARCHAR(255) UNIQUE,
    user_id BIGINT,
    total_price FLOAT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Cart items table
CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT,
    menu_item_id BIGINT,
    quantity INT NOT NULL,
    total_price FLOAT NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES carts(id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(255) UNIQUE,
    user_id BIGINT,
    status VARCHAR(255) CHECK (status IN ('PENDING', 'PREPARING', 'READY', 'DELIVERED')),
    total_price FLOAT NOT NULL,
    payment_status VARCHAR(255) CHECK (payment_status IN ('PAID', 'UNPAID', 'REFUNDED')),
    created_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Order items table
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    menu_item_id BIGINT,
    quantity INT NOT NULL,
    total_price NUMERIC(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);