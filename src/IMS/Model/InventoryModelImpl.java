package IMS.Model;

import IMS.IMSException;

import java.sql.*;
import java.util.*;
import java.util.Date;

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
    public void insertVendor(String name, String address, String state, int zip, String description) {
        Vendor v = new Vendor(name, address, state, zip, description);
        v.insertVendor();
    }

    // TODO: Will vendor have the same name
    @Override
    public List<Vendor> getVendor(String name) {
        return Vendor.getVendor(name);
    }

    @Override
    public Set<Item> getSoldItems(String name) {
        List<Vendor> l = Vendor.getVendor(name);
        HashSet<Item> result = new HashSet<>();
        for (Vendor v : l) {
            result.addAll(v.getItemList());
        }
        return result;
    }

    @Override
    public List<Status> getInvStatus() {
        try (Connection con = DatabaseUtil.createConnection();
             CallableStatement stmt = con.prepareCall("{CALL INV_STATUS(?, ?, ?)}")) {
            stmt.setNull(1, Types.INTEGER);
            stmt.setNull(2, Types.INTEGER);
            stmt.setNull(3, Types.INTEGER);
            ResultSet rs = stmt.executeQuery();
            List<Status> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new Status(rs.getString(2), rs.getString(4)
                        , rs.getString(6), rs.getInt(7)));
            }
            return result;
        } catch (SQLException e) {
            throw new IMSException(e.getMessage());
        }
    }



    @Override
    public void insertStore(String address, String state, int zipCode) {
        System.out.printf("%s, %s, %d", address, state, zipCode);
    }

    @Override
    public List<RetailStore> getStores(String name) {
        System.out.println(name);
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
    public List<Sale> getSales(String customer, Date date) {
        return  Sale.getSales(customer, date);
    }

    @Override
    public Sale getSale(int saleID) {
        return null;
    }

    @Override
    public void insertCustomer(String name) {

    }
}
