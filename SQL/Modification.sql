-- Avoid duplicate item

-- Avoid duplicate Category

-- Avoid duplicate Store


-- Avoid duplicate Vendor


-- Add procedure to get all sold
USE ims_SKU;
DROP PROCEDURE IF EXISTS GET_ITEMS_FROM_VENDOR;

DELIMITER //

CREATE PROCEDURE GET_ITEMS_FROM_VENDOR(
	IN vendor_name VARCHAR(30)
    )
    BEGIN
    SELECT i.item_id, c.cat_name, i.item_name, i.item_unit_price
    FROM vendor_has_item
		JOIN item i USING (item_id)
        JOIN vendor v USING (ven_id)
        JOIN item_category c USING (cat_id)
	WHERE v.ven_name = vendor_name;
    END //

DELIMITER ;
-- test for GET_ITEMS_FROM_VENDOR
CALL GET_ITEMS_FROM_VENDOR(
	(SELECT ven_name
    FROM vendor
    WHERE ven_id = 1)
    );


--  Add procedure to insert Vendor_Has_Item
DROP PROCEDURE IF EXISTS INSERT_VENDOR_HAS_ITEM;
DELIMITER //
CREATE PROCEDURE INSERT_VENDOR_HAS_ITEM(
	IN vendor_name VARCHAR(30),
    IN item_name VARCHAR(30)
)
BEGIN
	DECLARE ven_id INT(11);
	DECLARE item_id INT(11);
    SELECT v.ven_id
		FROM vendor v
        WHERE v.ven_name = vendor_name
        INTO ven_id;
	SELECT i.item_id
		FROM item i
        WHERE i.item_name = item_name
        INTO item_id;
	INSERT INTO vendor_has_item
		VALUES (ven_id, item_id);
END //

DELIMITER ;

INSERT INTO item VALUES (DEFAULT, 1, 'alienware', 1699.00);

-- test
CALL INSERT_VENDOR_HAS_ITEM('Ward, Shields and Oberbrunner', 'alienware');

SELECT ven_name, item_name
    FROM vendor JOIN vendor_has_item vhi on vendor.ven_id = vhi.ven_id
    JOIN item i on vhi.item_id = i.item_id;

