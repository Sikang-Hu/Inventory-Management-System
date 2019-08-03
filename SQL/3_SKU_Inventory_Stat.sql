USE ims_SKU;

-- display inventory remain by item and store and cat
# TODO: SORTING
# this procedure generates QUANTITY, NOT $ AMOUNT
DROP PROCEDURE IF EXISTS get_inventory_status_by_item;
DELIMITER //
CREATE PROCEDURE get_inventory_status_by_item(IN input_store_id INT,
                                              IN input_item_id INT,
                                              IN input_cat_id INT)
BEGIN
    SELECT supply.store_id,
           supply.store_address,
           supply.item_id,
           supply.item_name,
           supply.cat_id,
           supply.cat_name,
           supply.bought                                       AS bought,
           IF(sale.sold IS NULL, 0, sale.sold)                 AS sold,
           supply.bought - IF(sale.sold IS NULL, 0, sale.sold) AS remain
    FROM (SELECT rs.store_id,
                 rs.store_address,
                 i.item_id,
                 i.item_name,
                 ic.cat_id,
                 ic.cat_name,
                 SUM(SKU.order_quantity) AS bought
          FROM retail_store rs
                   JOIN supply_order so ON rs.store_id = so.store_id
                   JOIN SKU on SKU.order_id = so.order_id
                   JOIN item i on SKU.item_id = i.item_id
                   JOIN item_category ic on i.cat_id = ic.cat_id
          WHERE so.delivery_date IS NOT NULL
          GROUP BY rs.store_id, rs.store_address, i.item_id, i.item_name) AS supply
             LEFT JOIN
         (SELECT i.item_id, s.store_id, SUM(shS.sale_quantity) as sold
          FROM item i
                   JOIN SKU ON i.item_id = SKU.item_id
                   JOIN sale_has_SKU shS ON SKU.SKU_id = shS.SKU_id
                   JOIN sale s on shS.sale_id = s.sale_id
          GROUP BY i.item_id, s.store_id) AS sale
         ON (supply.item_id = sale.item_id AND supply.store_id = sale.store_id)
    WHERE 1 = 1
      AND (
        CASE # set condition for store_id
            WHEN input_store_id IS NOT NULL THEN supply.store_id = input_store_id
            ELSE 1 = 1
            END)
      AND (
        CASE # set condition for item_id
            WHEN input_item_id IS NOT NULL THEN supply.item_id = input_item_id
            ELSE 1 = 1
            END)
      AND (
        CASE # set condition for cat_id
            WHEN input_cat_id IS NOT NULL THEN supply.cat_id = input_cat_id
            ELSE 1 = 1
            END)
    ORDER BY supply.store_id, supply.item_id, supply.cat_id;
END//
DELIMITER ;

CALL get_inventory_status_by_item(null, null, null);
# store_id, item_id, cat_id

-- return quantity remain for every sku (NOT item)
# a potential helper method used for fifo
# default order by delivery date
# non-delivered sku are not included
DROP PROCEDURE IF EXISTS get_inventory_status_by_sku;
DELIMITER //
CREATE PROCEDURE get_inventory_status_by_sku(IN input_store_id INT,
                                             IN input_item_id INT)
BEGIN
    SELECT bought.SKU_id,
           bought.store_id,
           bought.store_address,
           bought.item_id,
           bought.item_name,
           bought.unit_cost,
           bought.order_quantity,
           bought.order_quantity - IF(sold.num IS NULL, 0, sold.num) AS remain
    FROM (SELECT SKU.SKU_id,
                 SKU.order_quantity,
                 SKU.unit_cost,
                 rs.store_id,
                 rs.store_address,
                 i.item_id,
                 i.item_name
          FROM retail_store rs
                   JOIN supply_order so ON rs.store_id = so.store_id
                   JOIN SKU ON so.order_id = SKU.order_id
                   JOIN item i ON SKU.item_id = i.item_id
          WHERE so.delivery_date IS NOT NULL
         ) AS bought
             LEFT JOIN
         (SELECT SKU.SKU_id, SUM(shS.sale_quantity) AS num
          FROM SKU
                   JOIN sale_has_SKU shS ON SKU.SKU_id = shS.SKU_id
          GROUP BY SKU.SKU_id
         ) AS sold ON (bought.SKU_id = sold.SKU_id)
    WHERE 1 = 1
      AND (
        CASE # set condition for store_id
            WHEN input_store_id IS NOT NULL THEN bought.store_id = input_store_id
            ELSE 1 = 1
            END)
      AND (
        CASE # set condition for item_id
            WHEN input_item_id IS NOT NULL THEN bought.item_id = input_item_id
            ELSE 1 = 1
            END)
    ORDER BY bought.store_id, bought.item_id, remain;
END//
DELIMITER ;

CALL get_inventory_status_by_sku(null, null);