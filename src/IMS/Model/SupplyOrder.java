package IMS.Model;

import java.util.Date;
import java.util.List;

public class SupplyOrder {
    private int order_id;
    private int ven_id;
    private int store_id;
    private Date order_date;
    private Date delivery_date;
    private List<ItemInOrder> items;

    public SupplyOrder( int ven_id, int store_id,
                        Date order_date, List<ItemInOrder> items) {
        this.order_id = -1;
        this.ven_id = ven_id;
        this.store_id = store_id;
        this.order_date = order_date;
        this.delivery_date = null;
        this.items = items;
    }

    public SupplyOrder( int order_id, int ven_id, int store_id,
                        Date order_date, List<ItemInOrder> items) {
        this.order_id = order_id;
        this.ven_id = ven_id;
        this.store_id = store_id;
        this.order_date = order_date;
        this.delivery_date = null;
        this.items = items;
    }

    public Date getDelivery_date() {
        return delivery_date;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public int getVen_id() {
        return ven_id;
    }

    public void setDelivery_date(Date delivery_date) {
        this.delivery_date = delivery_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public void setVen_id(int ven_id) {
        this.ven_id = ven_id;
    }
}