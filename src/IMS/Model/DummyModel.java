package IMS.Model;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This is a dummy model for this inventory system. It enable user to insert various entities, query about them.
 */
public class DummyModel implements InventoryModel {

    private File data = new File("./order.dbt");

    @Override
    public void insertOrder(String order) {
      Order o = new Order(order);
      o.insertToDB();
    }

    /**
     * Insert an item into our dummy database
     *
     * @param name
     * @param category
     * @param unitPrice
     */
    @Override
    public void insertItem(String name, String category, double unitPrice) {
        Item i = new Item(name, category, unitPrice);
        i.insertToDB();
    }

    /**
     * Query an item by its name
     *
     * @param name
     * @return
     */
    @Override
    public Item getItem(String name) {
        return Item.getItem(name);
    }

    @Override
    public void insertVendor(String name, String address, String state, String zip, String description) {

    }

    @Override
    public Vendor getVendor(String name) {
        return null;
    }

    @Override
    public void insertStore() {

    }
}
