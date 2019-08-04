USE ims_SKU;

-- display weekly sale amount by item and store
# TODO: SORTING
# this procedure WILL NOT display store that hasn't made any sale
# sale/profit is measured in $ AMOUNT, NOT QUANTITY
DROP PROCEDURE IF EXISTS get_weekly_sale_by_item;
DELIMITER //
CREATE PROCEDURE get_weekly_sale_by_item(IN input_store_id INT,
                                         IN input_item_id INT)
BEGIN
    SELECT rs.store_id,
           rs.store_address,
           i.item_id,
           i.item_name,
           YEARWEEK(sale.sale_date)                     AS year_week,
           SUM(shs.sale_quantity * shs.unit_sale_price) AS sale_amt
    FROM retail_store rs
             JOIN supply_order so on rs.store_id = so.store_id
             JOIN sku S on so.order_id = S.order_id
             JOIN item i on S.item_id = i.item_id
             JOIN sale_has_sku shs on S.sku_id = shs.sku_id
             JOIN sale on shs.sale_id = sale.sale_id
    WHERE 1 = 1 # a delivery_date check is unnecessary since no sale can be inserted if not delivered
      AND (
        CASE
            WHEN input_store_id IS NOT NULL THEN rs.store_id = input_store_id
            ELSE 1 = 1
            END)
      AND (
        CASE
            WHEN input_item_id IS NOT NULL THEN i.item_id = input_item_id
            ELSE 1 = 1
            END)
    GROUP BY rs.store_id, rs.store_address, i.item_id, i.item_name, year_week
    ORDER BY rs.store_id, i.item_id, year_week;
END//
DELIMITER ;

CALL get_weekly_sale_by_item(null, null);