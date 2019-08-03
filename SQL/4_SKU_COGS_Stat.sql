USE ims_SKU;

-- display cogs for each sale (and shs) by store and item
# each sale can have multiple shs and thus different cogs even for the same item
DROP PROCEDURE IF EXISTS get_cogs_by_sale;
DELIMITER //
CREATE PROCEDURE get_cogs_by_sale(IN input_store_id INT,
                                  IN input_item_id INT)
BEGIN
    SELECT s.sale_id,
           rs.store_id,
           rs.store_address,
           i.item_id,
           i.item_name,
           shS.sale_quantity * SKU.unit_cost AS cogs,
           # yearweek has no usage in this query as every sale has its own date
           # but could prove beneficial if sum by week is needed in future
           YEARWEEK(sale_date)               AS year_week
    FROM retail_store rs
             JOIN supply_order so ON rs.store_id = so.store_id
             JOIN SKU ON so.order_id = SKU.order_id
             JOIN item i on SKU.item_id = i.item_id
             JOIN sale_has_SKU shS ON SKU.SKU_id = shS.SKU_id
             JOIN sale s on shS.sale_id = s.sale_id
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
    ORDER BY rs.store_id, i.item_id, sale_date, sale_id;
END//
DELIMITER ;

CALL get_cogs_by_sale(null, null);