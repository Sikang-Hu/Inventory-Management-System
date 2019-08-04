DROP DATABASE IF EXISTS ims_SKU;
CREATE DATABASE IF NOT EXISTS ims_SKU;
USE ims_SKU;


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
    item_id         INT PRIMARY KEY AUTO_INCREMENT,
    cat_id          INT NOT NULL,
    item_name       VARCHAR(30),
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
    order_id      INT PRIMARY KEY AUTO_INCREMENT,
    ven_id        INT  NOT NULL,
    store_id      INT  NOT NULL,
    order_date    DATE NOT NULL,
    delivery_date DATE DEFAULT NULL,

    CONSTRAINT order_fk_ven FOREIGN KEY (ven_id) REFERENCES vendor (ven_id),
    CONSTRAINT order_fk_store FOREIGN KEY (store_id) REFERENCES retail_store (store_id)
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
    sale_id   INT PRIMARY KEY AUTO_INCREMENT,
    store_id  INT  NOT NULL,
    cus_id    INT  NOT NULL,
    sale_date DATE NOT NULL,

    CONSTRAINT sale_fk_store FOREIGN KEY (store_id) REFERENCES retail_store (store_id),
    CONSTRAINT sale_fk_cus FOREIGN KEY (cus_id) REFERENCES customer (cus_id)
);


DROP TABLE IF EXISTS sku;
CREATE TABLE IF NOT EXISTS sku
(
    sku_id         INT AUTO_INCREMENT UNIQUE,
    order_id       INT NOT NULL,
    item_id        INT NOT NULL,
    order_quantity INT NOT NULL,
    unit_cost      DECIMAL(8, 2),

    PRIMARY KEY (sku_id, order_id, item_id),
    CONSTRAINT sku_FK_order FOREIGN KEY (order_id) REFERENCES supply_order (order_id),
    CONSTRAINT sku_FK_item FOREIGN KEY (item_id) REFERENCES item (item_id)
);

DROP TABLE IF EXISTS sale_has_sku;
CREATE TABLE IF NOT EXISTS sale_has_sku
(
    sale_id         INT           NOT NULL,
    sku_id          INT           NOT NULL,
    sale_quantity   INT           NOT NULL,
    unit_sale_price DECIMAL(8, 2) NOT NULL,

#     PRIMARY KEY (sale_id, sku_id), # in order to have return entry this should be blocked
    CONSTRAINT si_fk_sale FOREIGN KEY (sale_id) REFERENCES sale (sale_id),
    CONSTRAINT si_fk_sku FOREIGN KEY (sku_id) REFERENCES sku (sku_id)
);

# making sure the vendor has the item available for sell
DROP TRIGGER IF EXISTS verify_vhi_for_sku_insertion;
DELIMITER //
CREATE TRIGGER verify_vhi_for_sku_insertion
    BEFORE INSERT
    ON sku
    FOR EACH ROW
BEGIN
    DECLARE message VARCHAR(255);
    DECLARE cur_ven_id INT;

    SELECT ven_id INTO cur_ven_id FROM supply_order WHERE order_id = NEW.order_id;

    IF (SELECT ven_id FROM vendor_has_item WHERE ven_id = cur_ven_id AND item_id = NEW.item_id) IS NULL THEN
        SELECT CONCAT('Vendor id=', cur_ven_id, ' is not selling Item id=', NEW.item_id,
                      '\nOrder can not be inserted. Update vendor_has_item table first.')
        INTO message;
        SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = message;
    END IF;
END//
DELIMITER ;

-- Supply_Order Insertion Procedure ## AUTO_INCREMENT ID
# Does not need delivery date as every order starts with a null delivery date.
# Delivery_date will be initialized by UPDATE_ORDER_FOR_DELIVERY procedure.
DROP PROCEDURE IF EXISTS insert_into_supply_order;
DELIMITER //
CREATE PROCEDURE insert_into_supply_order(IN input_ven_id INT,
                                          IN input_store_id INT,
                                          IN input_order_date DATE) # date can be ignored if set to NOW()
BEGIN
    INSERT INTO supply_order (ven_id, store_id, order_date)
    VALUES (input_ven_id, input_store_id, input_order_date);
END//
DELIMITER ;

-- Supply_Order delivery_date Update Procedure
# Set delivery date to now() when an order has been delivered.
DROP PROCEDURE IF EXISTS update_order_for_delivery;
DELIMITER //
CREATE PROCEDURE update_order_for_delivery(
    IN input_order_id INT
)
BEGIN
    IF (SELECT delivery_date FROM supply_order WHERE order_id = input_order_id) IS NULL THEN
        UPDATE supply_order o
        SET o.delivery_date = NOW()
        WHERE o.order_id = input_order_id;
    END IF;
END//
DELIMITER ;

-- sku Insertion Procedure ## AUTO_INCREMENT ID
# Insert into sku table procedure.
DROP PROCEDURE IF EXISTS insert_into_sku;
DELIMITER //
CREATE PROCEDURE insert_into_sku(IN input_order_id INT, # TODO: JAVA need to provide the order_id
                                 IN input_item_id INT, # TODO: JAVA need to provide the item_id instead of item_name
                                 IN input_order_quantity INT,
                                 IN input_unit_cost DECIMAL(8, 2))
BEGIN
    INSERT INTO sku (order_id, item_id, order_quantity, unit_cost)
    VALUES (input_order_id, input_item_id, input_order_quantity, input_unit_cost);
END//
DELIMITER ;


-- Sale Insertion Procedure ## AUTO_INCREMENT ID
# Insert into sale table procedure.
# Will create new customer if current customer id is not in customer table.
DROP PROCEDURE IF EXISTS insert_into_sale;
DELIMITER //
CREATE PROCEDURE insert_into_sale(IN input_store_id INT,
                                  IN input_sale_date DATETIME,
                                  IN input_cus_id INT, # can be null
                                  IN input_cus_name VARCHAR(30), # can be null
                                  OUT output_sale_id INT)
BEGIN
    IF input_cus_id IS NULL THEN
        INSERT INTO customer (cus_name) VALUES (input_cus_name);
        # reset input_cus_id in case of NULL
        SET input_cus_id = (SELECT cus_id FROM customer ORDER BY cus_id DESC LIMIT 1);
    ELSEIF input_cus_id NOT IN (SELECT cus_id FROM customer) THEN
        INSERT INTO customer VALUES (input_cus_id, input_cus_name);
    END IF;
    # insert into sell
    INSERT INTO sale (store_id, cus_id, sale_date) VALUES (input_store_id, input_cus_id, input_sale_date);
    # return new sale_id for SHS insertions
    SELECT sale_id INTO output_sale_id FROM sale ORDER BY sale_id DESC LIMIT 1;
END//
DELIMITER ;


-- Sale_has_sku Insertion Trigger
# this trigger is a double check
# it's not needed when procedure is called when inserting on shs
DROP TRIGGER IF EXISTS verify_sku_remain_for_shs_insertion;
DELIMITER //
CREATE TRIGGER verify_sku_remain_for_shs_insertion
    BEFORE INSERT
    ON sale_has_sku
    FOR EACH ROW
BEGIN
    DECLARE message VARCHAR(255); -- The error message
    DECLARE remain INT DEFAULT 0;
    DECLARE cur_store_id INT;
    DECLARE cur_sale_date DATE;

    # making sure the sku is actually in the store where the sale is made
    SELECT store_id, sale_date INTO cur_store_id, cur_sale_date FROM sale WHERE sale.sale_id = NEW.sale_id;
    IF NEW.sku_id NOT IN (SELECT sku_id
                          FROM sku
                                   JOIN supply_order s on sku.order_id = s.order_id
                          WHERE s.store_id = cur_store_id) THEN
        SELECT CONCAT('This stack of NEW.sku id=', NEW.sku_id, ' does not belong to the store id=', cur_store_id,
                      ' associated with the NEW.Sale id=', NEW.sale_id)
        INTO message;
        SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = message;
    END IF;

    # making sure the remain of this sku stack is enough for the current sale quantity
    SELECT sku.order_quantity - IF(SUM(shs.sale_quantity) IS NULL, 0, SUM(shs.sale_quantity))
    INTO remain
    FROM supply_order so
             JOIN sku ON so.order_id = sku.order_id
             LEFT JOIN sale_has_sku shs on sku.sku_id = shs.sku_id
    WHERE so.delivery_date IS NOT NULL      # making sure the sku is actually delivered
      AND cur_sale_date >= so.delivery_date # todo: verify this

      AND sku.sku_id = NEW.sku_id;

    IF NEW.sale_quantity > remain THEN
        SELECT CONCAT('This stack of sku id=', NEW.sku_id, ' does not have enough items to sell. Selling ',
                      NEW.sale_quantity, ' but only found ', remain)
        INTO message;
        SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = message;
    END IF;

END //
DELIMITER ;

-- Get Current Stock for Item X in Store Y
DROP FUNCTION IF EXISTS get_item_stock_at_store;
DELIMITER //
CREATE FUNCTION get_item_stock_at_store(input_item_id INT, input_store_id INT)
    RETURNS INT
    READS SQL DATA
BEGIN
    DECLARE stock_remain INT DEFAULT 0;
    DECLARE total INT;
    DECLARE sold INT;

    SELECT SUM(sku.order_quantity)
    INTO total
    FROM supply_order so
             JOIN sku on sku.order_id = so.order_id
    WHERE sku.item_id = input_item_id
      AND so.store_id = input_store_id
      AND so.delivery_date IS NOT NULL;

    SELECT SUM(shs.sale_quantity)
    INTO sold
    FROM supply_order so
             JOIN sku on sku.order_id = so.order_id
             LEFT JOIN sale_has_sku shs on sku.sku_id = shs.sku_id
    WHERE sku.item_id = input_item_id
      AND so.store_id = input_store_id
      AND so.delivery_date IS NOT NULL;

    # handles NULL --> if item doesn't exist in this store, still returns 0.
    SET stock_remain = IF(total IS NULL, 0, total) - IF(sold IS NULL, 0, sold);

    RETURN stock_remain;
END//
DELIMITER ;

-- Sale_has_sku Insertion Procedure
# the logical for insertion on SHS is complicated.
# TODO: the Java provides sale_id, item_id (INSTEAD OF sku_id), quantity, and price.
# the procedure get all sku with item_id in the store related to sale_id and order them by their delivery_date.
# the procedure then write off every sku_id with sale_id until sale_quantity reaches to 0.
# this procedure checks if quantity remain for all sku_id is enough for sale_quantity.
# VERIFY_sku_REMAIN_FOR_SHS_INSERTION trigger ONLY checks if the sale_quantity is good for CURRENT sku stack and
# this check will be handled in fifo_stack while loop.
DROP PROCEDURE IF EXISTS insert_into_sale_has_sku;
DELIMITER //
CREATE PROCEDURE insert_into_sale_has_sku(IN input_sale_id INT,
                                          IN input_item_id INT, # NOT sku_ID!!!!
                                          IN input_sale_quantity INT, #TODO: validate input
                                          IN input_unit_sale_price DECIMAL(8, 2))
BEGIN
    DECLARE message VARCHAR(255);
    DECLARE cur_store_id INT;
    DECLARE stack_quantity INT;
    DECLARE stack_remain INT;
    DECLARE stack_sku INT;

    SELECT store_id INTO cur_store_id FROM sale WHERE sale_id = input_sale_id;
    SET stack_quantity = input_sale_quantity;

    # check if there's enough item to sell for all sku with item_id in this store
    IF input_sale_quantity > get_item_stock_at_store(input_item_id, cur_store_id) THEN
        SELECT CONCAT('This Store id=', cur_store_id, ' does not have enough Item id=', input_item_id,
                      ' for sale. Selling ', input_sale_quantity, ' but only found ',
                      get_item_stock_at_store(input_item_id, cur_store_id))
        INTO message;
        SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = message;
    END IF;

    # get all sku for input_item in the sale related store
    DROP TEMPORARY TABLE IF EXISTS fifo_stack;
    CREATE TEMPORARY TABLE IF NOT EXISTS fifo_stack
    SELECT sku.sku_id,
           sku.order_quantity - IF(SUM(shs.sale_quantity) IS NULL, 0, SUM(shs.sale_quantity)) AS remain
    FROM supply_order so
             JOIN sku on sku.order_id = so.order_id
             LEFT JOIN sale_has_sku shs on sku.sku_id = shs.sku_id
    WHERE sku.item_id = input_item_id
      AND so.store_id = cur_store_id
      AND so.delivery_date IS NOT NULL # making sure the selected fifo sku are delivered
#       AND (SELECT sale_date FROM sale WHERE sale_id = input_sale_id) >= so.delivery_date # todo: verify this
    GROUP BY so.delivery_date, sku.sku_id
    HAVING remain > 0
    ORDER BY so.delivery_date;

    # as long as the sale quantity hasn't reached 0, keep looping
    WHILE stack_quantity > 0 DO
    # since the quantity check has been completed, the stack will not go out of bound
    SELECT sku_id, remain INTO stack_sku, stack_remain FROM fifo_stack LIMIT 1;

    IF stack_quantity > stack_remain THEN
        # if remaining sale quantity is greater than current remain on this stack,
        # insert shs with current stack remain and update remaining sale quantity.
        INSERT INTO sale_has_sku VALUES (input_sale_id, stack_sku, stack_remain, input_unit_sale_price);
        SET stack_quantity = stack_quantity - stack_remain;
    ELSE
        # if remaining sale quantity is less than current stack remain,
        # insert shs with sale quantity and set it to 0, end the loop.
        INSERT INTO sale_has_sku VALUES (input_sale_id, stack_sku, stack_quantity, input_unit_sale_price);
        SET stack_quantity = 0;
    END IF;

    DELETE FROM fifo_stack LIMIT 1;
    END WHILE;

END//
DELIMITER ;

