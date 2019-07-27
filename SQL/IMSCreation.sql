DROP DATABASE IF EXISTS ims;
CREATE DATABASE IF NOT EXISTS ims;
USE ims;


DROP TABLE IF EXISTS item_category;
CREATE TABLE IF NOT EXISTS item_category
(
    cat_id          INT PRIMARY KEY AUTO_INCREMENT,
    cat_name        VARCHAR(30),
    cat_description VARCHAR(255)
);


DROP TABLE IF EXISTS item;
CREATE TABLE IF NOT EXISTS item
(
    item_id    INT PRIMARY KEY AUTO_INCREMENT,
    cat_id     INT NOT NULL,
    item_name  VARCHAR(30),
    item_unit_price DECIMAL(8, 2),

    CONSTRAINT item_FK_cat FOREIGN KEY (cat_id) REFERENCES item_category (cat_id)
);


DROP TABLE IF EXISTS vendor;
CREATE TABLE IF NOT EXISTS vendor
(
    ven_id          INT PRIMARY KEY AUTO_INCREMENT,
    ven_name        VARCHAR(30),
    ven_address     VARCHAR(30),
    ven_state       CHAR(2),
    ven_zip         INT,
    ven_description VARCHAR(255)
);

DROP TABLE IF EXISTS vendor_has_item;
CREATE TABLE IF NOT EXISTS vendor_has_item
(
    ven_id  INT,
    item_id INT,

    PRIMARY KEY (ven_id, item_id),
    CONSTRAINT vi_fk_ven FOREIGN KEY (ven_id) REFERENCES vendor (ven_id),
    CONSTRAINT vi_fk_item FOREIGN KEY (item_id) REFERENCES item (item_id)
);


DROP TABLE IF EXISTS retail_store;
CREATE TABLE IF NOT EXISTS retail_store
(
    store_id      INT PRIMARY KEY AUTO_INCREMENT,
    store_address VARCHAR(255),
    store_state   CHAR(2),
    store_zip     INT
);


DROP TABLE IF EXISTS supply_order;
CREATE TABLE IF NOT EXISTS supply_order
(
    order_id      INT AUTO_INCREMENT UNIQUE,
    ven_id        INT,
    store_id      INT,
    order_date    DATE NOT NULL,
    delivery_date DATE,

    PRIMARY KEY (order_id, ven_id, store_id),
    CONSTRAINT order_fk_ven FOREIGN KEY (ven_id) REFERENCES vendor (ven_id),
    CONSTRAINT order_fk_store FOREIGN KEY (store_id) REFERENCES retail_store (store_id)
);


DROP TABLE IF EXISTS order_has_item;
CREATE TABLE IF NOT EXISTS order_has_item
(
    order_id       INT,
    item_id        INT,
    order_quantity INT NOT NULL,
    unit_cost      DECIMAL(8, 2),

    PRIMARY KEY (order_id, item_id),
    CONSTRAINT oi_fk_order FOREIGN KEY (order_id) REFERENCES supply_order (order_id),
    CONSTRAINT oi_fk_item FOREIGN KEY (item_id) REFERENCES item (item_id)
);


DROP TABLE IF EXISTS customer;
CREATE TABLE IF NOT EXISTS customer
(
    cus_id   INT PRIMARY KEY AUTO_INCREMENT,
    cus_name VARCHAR(30)
);


DROP TABLE IF EXISTS sale;
CREATE TABLE IF NOT EXISTS sale
(
    sale_id   INT AUTO_INCREMENT UNIQUE,
    store_id  INT,
    cus_id    INT,
    sale_date DATETIME NOT NULL,

    PRIMARY KEY (sale_id, store_id, cus_id),
    CONSTRAINT sale_fk_store FOREIGN KEY (store_id) REFERENCES retail_store (store_id),
    CONSTRAINT sale_fk_cus FOREIGN KEY (cus_id) REFERENCES customer (cus_id)
);


DROP TABLE IF EXISTS sale_has_item;
CREATE TABLE IF NOT EXISTS sale_has_item
(
    sale_id         INT,
    item_id         INT,
    sale_quantity   INT           NOT NULL,
    unit_sale_price DECIMAL(8, 2) NOT NULL,

    PRIMARY KEY (sale_id, item_id),
    CONSTRAINT si_fk_sale FOREIGN KEY (sale_id) REFERENCES sale (sale_id),
    CONSTRAINT si_fk_item FOREIGN KEY (item_id) REFERENCES item (item_id)
);
