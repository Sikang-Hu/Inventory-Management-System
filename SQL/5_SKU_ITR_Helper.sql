USE ims_SKU;

DROP PROCEDURE IF EXISTS get_lead_time_by_item;
DELIMITER //
CREATE PROCEDURE get_lead_time_by_item(IN input_vendor_id INT,
                                       IN input_item_id INT)
BEGIN
    SELECT v.ven_id,
           v.ven_name,
           i.item_id,
           i.item_name,
           ROUND(AVG(DATEDIFF(so.delivery_date, so.order_date)), 0) AS avg_lead_date
    FROM item i
             JOIN sku ON i.item_id = sku.item_id
             JOIN supply_order so ON sku.order_id = so.order_id
             JOIN vendor v ON so.ven_id = v.ven_id
    WHERE so.delivery_date IS NOT NULL
      AND (
        CASE
            WHEN input_vendor_id IS NOT NULL THEN v.ven_id = input_vendor_id
            ELSE 1 = 1
            END)
      AND (
        CASE
            WHEN input_item_id IS NOT NULL THEN i.item_id = input_item_id
            ELSE 1 = 1
            END)
    GROUP BY v.ven_id, v.ven_name, i.item_id, i.item_name;
END//
DELIMITER ;

CALL get_lead_time_by_item(null, null); # vendor_id, item_id