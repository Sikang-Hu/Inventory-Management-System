package IMS.Model;

public class ItemInTransaction {

    private int item_id;
    private int order_quantity;
    private double unit_cost;

    public ItemInTransaction(int item_id, int order_quantity, double unit_cost) {
        this.item_id = item_id;
        this.order_quantity = order_quantity;
        this.unit_cost = unit_cost;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public void setOrder_quantity(int order_quantity) {
        this.order_quantity = order_quantity;
    }

    public void setUnit_cost(double unit_cost) {
        this.unit_cost = unit_cost;
    }

    public double getUnit_cost() {
        return unit_cost;
    }

    public int getItem_id() {
        return item_id;
    }

    public int getOrder_quantity() {
        return order_quantity;
    }
}