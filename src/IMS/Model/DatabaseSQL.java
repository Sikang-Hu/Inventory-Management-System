package IMS.Model;

import java.text.SimpleDateFormat;

public class DatabaseSQL implements IDatabaseSQL{

    DBUtils dbu;

    @Override
    public int insertItem(Item item) {
        String sql = "INSERT INTO item (itemId,categoryID,itemName,itemPrice) VALUES" +
                "('"+item.getItemId()+"','"+item.getCategoryID()+"','"+item.getItemName()+"','"+ item.getItemPrice() + "')";
        return dbu.insertOneRecord(sql);
    }

    @Override
    public int insertSupplyOrder(SupplyOrder supplyOrder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "INSERT INTO supply_order (ven_id, store_id, supplyOrder_date) VALUES" +
                " ("+supplyOrder.getVen_id()+", "+supplyOrder.getStore_id()+", '"
                +sdf.format(supplyOrder.getOrder_date())+"')";
        return dbu.insertOneRecord(sql);
    }

    @Override
    public int insertVendor(Vendor vendor) {
        String sql = "INSERT INTO vendor (ven_name, ven_address, ven_state, ven_zip, ven_description) VALUES"
                + " ('"+vendor.getVendorName()+"', '"+vendor.getVendorAddress()+"', '"+vendor.getVendorState()+"', "
                +vendor.getVendorZip()+", '"+vendor.getVendorDescription()+"')";
        return dbu.insertOneRecord(sql);
    }

    @Override
    public int insertRetailStore(RetailStore retailStore) {
        String sql = "INSERT INTO retail_store(store_address, store_state, store_zip) VALUES"
                + " ('"+retailStore.getStoreAddress()+"', '"+retailStore.getStoreState()+"', "+retailStore.getStoreZip()+")";
        return dbu.insertOneRecord(sql);
    }

    @Override
    public int insertSale(Sale sale) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "INSERT INTO sale (store_id, cus_id, sale_date) VALUES" +
                " ("+sale.getStoreID()+", "+sale.getCustomerID()+", '"
                +sdf.format(sale.getSaleDate())+"')";
        return dbu.insertOneRecord(sql);
    }

    @Override
    public int insertCategory(Category category) {
        String sql = "INSERT INTO item_category (cat_name, cat_description) VALUES ('"
                + category.getCategoryName()+"', '"+category.getCategoryDescription()+"')";
        return dbu.insertOneRecord(sql);
    }

    @Override
    public int insertCustomer(Customer customer) {
        String sql = "INSERT INTO customer (cus_name) VALUES"
                + " ('" + customer.getName()+"')";
        return dbu.insertOneRecord(sql);
    }

    @Override
    public void authenticate(String user, String password) {

    }

    @Override
    public void closeConnection() {

    }
}
