USE ims_SKU;

-- display weekly profits by item and store
# TODO: SORTING
DROP PROCEDURE IF EXISTS get_weekly_profit_by_item;
DELIMITER //
CREATE PROCEDURE get_weekly_profit_by_item(IN input_store_id INT,
                                           IN input_item_id INT)
BEGIN
    SELECT rs.store_id,
           rs.store_address,
           i.item_id,
           i.item_name,
           YEARWEEK(sale.sale_date)                                     AS year_week,
           SUM(shS.sale_quantity * (shS.unit_sale_price - S.unit_cost)) AS profit
    FROM retail_store rs
             JOIN supply_order so ON rs.store_id = so.store_id
             JOIN SKU S on S.order_id = so.order_id
             JOIN item i on S.item_id = i.item_id
             JOIN sale_has_SKU shS on S.SKU_id = shS.SKU_id
             JOIN sale on shS.sale_id = sale.sale_id
    WHERE 1 = 1
      AND (
        CASE # set condition for store_id
            WHEN input_store_id IS NOT NULL THEN rs.store_id = input_store_id
            ELSE 1 = 1
            END)
      AND (
        CASE # set condition for item_id
            WHEN input_item_id IS NOT NULL THEN i.item_id = input_item_id
            ELSE 1 = 1
            END)
    GROUP BY rs.store_id, rs.store_address, i.item_id, i.item_name, YEARWEEK(sale.sale_date);
END//
DELIMITER ;

CALL get_weekly_profit_by_item(null, null);