USE ims;

SHOW TABLES;

-- each vendor has what item?
SELECT ven_name, item_name
FROM vendor
         LEFT JOIN vendor_has_item vhi ON vendor.ven_id = vhi.ven_id
         LEFT JOIN item ON vhi.item_id = item.item_id;

-- how many items does each vendor have for every category?
SELECT ven_name, cat_name, count(vhi.item_id)
FROM vendor
         LEFT JOIN vendor_has_item vhi ON vendor.ven_id = vhi.ven_id
         LEFT JOIN item i ON vhi.item_id = i.item_id
         LEFT JOIN item_category ic ON i.cat_id = ic.cat_id
GROUP BY ven_name, cat_name;

-- STORE SALE BY ITEM
SELECT retail_store.store_address, i.item_name, SUM(shi.sale_quantity)
FROM retail_store
         JOIN sale s ON retail_store.store_id = s.store_id
         JOIN sale_has_item shi ON s.sale_id = shi.sale_id
         JOIN item i ON shi.item_id = i.item_id
GROUP BY retail_store.store_address, i.item_name;

-- INVENTORY STATUS
SELECT supply.store_address                                    AS STORE,
       supply.item_name                                        AS ITEM,
       IF(sale.sq IS NULL, supply.oq - 0, supply.oq - sale.sq) AS IN_STOCK
FROM (SELECT rs.store_id, rs.store_address, i.item_id, i.item_name, SUM(ohi.order_quantity) AS oq
      FROM item i
               JOIN order_has_item ohi ON ohi.item_id = i.item_id
               JOIN supply_order so ON ohi.order_id = so.order_id
               JOIN retail_store rs ON so.store_id = rs.store_id
      WHERE so.delivery_date IS NOT NULL
      GROUP BY i.item_id, so.store_id, i.item_name) AS supply
         LEFT JOIN
     (SELECT rs.store_id, rs.store_address, i.item_id, i.item_name, SUM(shi.sale_quantity) AS sq
      FROM item i
               JOIN sale_has_item shi ON shi.item_id = i.item_id
               JOIN sale s ON shi.sale_id = s.sale_id
               JOIN retail_store rs ON s.store_id = rs.store_id
      GROUP BY i.item_id, s.store_id, i.item_name) AS sale
     ON (supply.store_id = sale.store_id AND supply.item_id = sale.item_id)
;

-- Sale Insert Trigger


-- COGS
DROP FUNCTION IF EXISTS COGS;
DELIMITER //
CREATE FUNCTION COGS(input_store_address VARCHAR(30), input_item_name VARCHAR(30), sale_quantity INT)
    RETURNS DECIMAL(8, 2)
    READS SQL DATA
BEGIN
    DECLARE cost DECIMAL(8, 2) DEFAULT 0;
    DECLARE stack_quantity INT;
    DECLARE stack_cost DECIMAL(8, 2);

    CREATE TEMPORARY TABLE fifo_stack
    SELECT ohi.unit_cost                                                      AS uc,
           SUM(DISTINCT ohi.order_quantity) - SUM(DISTINCT shi.sale_quantity) AS in_stock
    FROM item
             JOIN order_has_item ohi ON item.item_id = ohi.item_id
             JOIN supply_order so ON ohi.order_id = so.order_id
             JOIN retail_store rs ON so.store_id = rs.store_id
             LEFT JOIN sale s ON rs.store_id = s.store_id
             JOIN sale_has_item shi ON item.item_id = shi.item_id
    WHERE rs.store_address = input_store_address
      AND item.item_name = input_item_name
      AND so.delivery_date IS NOT NULL
    GROUP BY so.order_id, so.delivery_date, ohi.unit_cost
    ORDER BY so.delivery_date, so.order_id;

    WHILE sale_quantity > 0 DO
    SELECT uc, in_stock INTO stack_cost, stack_quantity FROM fifo_stack LIMIT 1;
    IF sale_quantity > stack_quantity THEN
        SET sale_quantity = sale_quantity - stack_quantity;
        SET cost = cost + stack_quantity * stack_cost;
        DELETE FROM fifo_stack LIMIT 1;
    ELSE
        SET cost = cost + sale_quantity * stack_cost;
        SET sale_quantity = 0;
    END IF;
    END WHILE;

    DROP TEMPORARY TABLE IF EXISTS fifo_stack;
    RETURN cost;
END
//
DELIMITER ;

select COGS('wanda street', 'apple', 50);

SELECT ohi.unit_cost,
       SUM(DISTINCT ohi.order_quantity) - SUM(DISTINCT shi.sale_quantity) AS in_stock
FROM item
         JOIN order_has_item ohi ON item.item_id = ohi.item_id
         JOIN supply_order so ON ohi.order_id = so.order_id
         JOIN retail_store rs ON so.store_id = rs.store_id
         LEFT JOIN sale s ON rs.store_id = s.store_id
         JOIN sale_has_item shi ON item.item_id = shi.item_id
WHERE rs.store_address = 'wanda street'
  AND item.item_name = 'apple'
  AND so.delivery_date IS NOT NULL
GROUP BY so.order_id, so.delivery_date, ohi.unit_cost
ORDER BY so.delivery_date, so.order_id;
