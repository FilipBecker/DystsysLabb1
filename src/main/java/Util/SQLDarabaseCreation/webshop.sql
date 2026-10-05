CREATE DATABASE IF NOT EXISTS webshop;
USE webshop;


CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('CUSTOMER','ADMIN','WAREHOUSE') NOT NULL DEFAULT 'CUSTOMER',
    email VARCHAR(100)
);


CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);


CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);


CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('NEW','PACKED','SHIPPED') DEFAULT 'NEW',
    FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE order_lines (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL, 
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);


INSERT INTO users (username, password, role, email) VALUES
('admin', 'admin', 'ADMIN', 'admin@webshop.se'),
('warehouse', 'warehouse', 'WAREHOUSE', 'warehouse@webshop.se'),
('customer1', 'customer1', 'CUSTOMER', 'customer1@email.se'),
('customer2', 'customer2', 'CUSTOMER', 'customer2@email.se');

INSERT INTO categories (name) VALUES
('Electronics'),
('Clothes'),
('Books'),
('Sport');

INSERT INTO products (name, description, price, stock, category_id) VALUES
('Laptop Pro 15', 'Powerful laptop with 16GB RAM and 512GB SSD', 1299.00, 15, 1),
('Wireless headseat', 'Bluetooth 5.0, noise cancelling', 89.00, 40, 1),
('Smartphone X', '6.5" OLED, 128GB Storage', 799.00, 25, 1),
('T-shirt Classic', 'Cotton, Multiple Colours', 19.00, 100, 2),
('Jeans Slim Fit', 'Stretchdenim, Navy', 59.00, 50, 2),
('Hoodie', 'Cozy fleece hoodie', 49.00, 35, 2),
('Java', 'Book for Java programming', 79.00, 20, 3),
('Dumbbells 5kg', 'Pair of dumbbells', 39.00, 18, 4),
('Hack Squat Machine', 'Leg Machine for Home Gym with Linear Bearing, Professional Adjustable Leg Exercise Machine, Lower Body Workout', 1499.99, 22, 4),
('Beyond Good And Evil', 'Beyond Good and Evil is a captivating philosophical masterpiece that challenges conventional thinking and explores the depths of human nature. Written by Friedrich Nietzsche', 15.00, 10, 3);

CREATE USER 'webshop'@'localhost' IDENTIFIED BY 'webshop123';
GRANT ALL PRIVILEGES ON webshop.* TO 'webshop'@'localhost';
FLUSH PRIVILEGES;
