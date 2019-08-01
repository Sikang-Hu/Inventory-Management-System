package IMS.Model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class InventoryModelImpl implements InventoryModel {
    @Override
    public void insertItem(String category, String name, double unitPrice) {
        Item i = new Item(category, name, unitPrice);
        i.insertItem();
    }

    @Override
    public Item getItem(String name) {
        return Item.getItem(name);
    }

    @Override
    public void insertVendor(String name, String address, String state, String zip, String description) {

    }

    // TODO: Will vendor have the same name
    @Override
    public List<Vendor> getVendor(String name) {
        return Vendor.getVendor(name);
    }

    @Override
    public HashSet<Item> getSoldItems(String name) {
        List<Vendor> l = Vendor.getVendor(name);
        HashSet<Item> result = new HashSet<>();
        for (Vendor v : l) {
            result.addAll(v.getItemList());
        }
        return result;
    }

    @Override
    public void insertStore(String address, String state, int zipCode) {

    }

    @Override
    public List<RetailStore> getStores() {
        return null;
    }

    @Override
    public int insertOrder(String vendor, Date date, List<Item> items) {
        return 0;
    }

    @Override
    public int insertSale(String customer, Date date, Map<String, Integer> items) {
        return 0;
    }

    @Override
    public List<SaleOrder> getSales(String customer, Date date) {
        return null;
    }

    @Override
    public SaleOrder getSale(int saleID) {
        return null;
    }

    @Override
    public void insertCustomer(String name) {

    }
}
