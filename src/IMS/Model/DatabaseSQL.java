package IMS.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class DatabaseSQL implements IDatabaseSQL{

    DBUtils dbu;

    @Override
    public int insertItem(int itemId, int categoryID, String itemName, int itemPrice) {
        String sql = "INSERT INTO item (itemId,categoryID,itemName,itemPrice) VALUES" +
                "('"+itemId +"','"+categoryID+"','"+itemName+"','"+ itemPrice + "')";
        return dbu.insertOneRecord(sql);
    }


    @Override
    public int insertVendor(String vendorName, String vendorAddress, String vendorState, int vendorZip,String vendorDescription) {
        String sql = "INSERT INTO vendor (ven_name, ven_address, ven_state, ven_zip, ven_description) VALUES"
                + " ('"+vendorName+"', '"+vendorAddress+"', '"+vendorState+"', "
                +vendorZip+", '"+vendorDescription+"')";
        return dbu.insertOneRecord(sql);
    }


    @Override
    public int insertRetailStore(String storeAddress,String StoreState, int storeZip) {
        String sql = "INSERT INTO retail_store(store_address, store_state, store_zip) VALUES"
                + " ('"+storeAddress+"', '"+StoreState+"', "+storeZip+")";
        return dbu.insertOneRecord(sql);
    }

    @Override
    public int insertCategory(String categoryName, String categoryDescription) {
        String sql = "INSERT INTO item_category (cat_name, cat_description) VALUES ('"
                + categoryName+"', '"+categoryDescription+"')";
        return dbu.insertOneRecord(sql);
    }

    @Override
    public int insertCustomer(String customerName) {
        String sql = "INSERT INTO customer (cus_name) VALUES"
                + " ('" + customerName +"')";
        return dbu.insertOneRecord(sql);
    }

    @Override
    public void authenticate(String user, String password) {
        dbu = new DBUtils("jdbc:mysql://localhost:3306/ims?serverTimezone=EST5EDT", user, password);
    }

    @Override
    public void closeConnection() {
        dbu.closeConnection();
    }


//    @Override
//    public int insertItem(Item item) {
//        String sql = "INSERT INTO item (itemId,categoryID,itemName,itemPrice) VALUES" +
//                "('"+item.getItemId()+"','"+item.getCategoryID()+"','"+item.getItemName()+"','"+ item.getItemPrice() + "')";
//        return dbu.insertOneRecord(sql);
//    }



//    @Override
//    public int insertSupplyOrder(SupplyOrder supplyOrder) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String sql = "INSERT INTO supply_order (ven_id, store_id, supplyOrder_date) VALUES" +
//                " ("+supplyOrder.getVen_id()+", "+supplyOrder.getStore_id()+", '"
//                +sdf.format(supplyOrder.getOrder_date())+"')";
//        return dbu.insertOneRecord(sql);
//    }

//    @Override
//    public int insertVendor(Vendor vendor) {
//        String sql = "INSERT INTO vendor (ven_name, ven_address, ven_state, ven_zip, ven_description) VALUES"
//                + " ('"+vendor.getVendorName()+"', '"+vendor.getVendorAddress()+"', '"+vendor.getVendorState()+"', "
//                +vendor.getVendorZip()+", '"+vendor.getVendorDescription()+"')";
//        return dbu.insertOneRecord(sql);
//    }
//
//    @Override
//    public int insertRetailStore(RetailStore retailStore) {
//        String sql = "INSERT INTO retail_store(store_address, store_state, store_zip) VALUES"
//                + " ('"+retailStore.getStoreAddress()+"', '"+retailStore.getStoreState()+"', "+retailStore.getStoreZip()+")";
//        return dbu.insertOneRecord(sql);
//    }

//    @Override
//    public int insertSale(Sale sale) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String sql = "INSERT INTO sale (store_id, cus_id, sale_date) VALUES" +
//                " ("+sale.getStoreID()+", "+sale.getCustomerID()+", '"
//                +sdf.format(sale.getSaleDate())+"')";
//        return dbu.insertOneRecord(sql);
//    }

//    @Override
//    public int insertCategory(Category category) {
//        String sql = "INSERT INTO item_category (cat_name, cat_description) VALUES ('"
//                + category.getCategoryName()+"', '"+category.getCategoryDescription()+"')";
//        return dbu.insertOneRecord(sql);
//    }



//    public void findAvgSale(){
//        String sql = "select * from Item";
//
//        try {
//            Connection connection  = dbu.getConnection();
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(sql);
//            while (resultSet.next()) {
//                System.out.println(resultSet.getInt("item_id"));
//            }
//
//            resultSet.close();
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        DatabaseSQL databaseSQL = new DatabaseSQL();
//        databaseSQL.authenticate("root", "Y123456h");
//        databaseSQL.findAvgSale();
//        databaseSQL.closeConnection();
//    }


}
