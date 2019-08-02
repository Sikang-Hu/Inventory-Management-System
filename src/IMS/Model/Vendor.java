package IMS.Model;

import IMS.IMSException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Vendor {
    private int vendorID;
    private String vendorName;
    private String vendorAddress;
    private String vendorState;
    private int vendorZip;
    private String vendorDescription;
    private HashSet<Item> itemList;


    Vendor(int vendorID, String vendorName, String vendorAddress, String vendorState,
           int vendorZip, String vendorDescription) {
        this.vendorID = vendorID;
        this.vendorName = vendorName;
        this.vendorAddress = vendorAddress;
        this.vendorState = vendorState;
        this.vendorZip = vendorZip;
        this.vendorDescription = vendorDescription;
    }

    Vendor(String vendorName, String vendorAddress, String vendorState,
           int vendorZip, String vendorDescription) {
        this(-1, vendorName, vendorAddress, vendorState, vendorZip, vendorDescription);
    }

    Vendor(String vendorName, String vendorAddress, String vendorState,
           int vendorZip) {
        this(-1, vendorName, vendorAddress, vendorState, vendorZip, null);
    }


    void insertVendor() {
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(this.insertStmt(), Statement.RETURN_GENERATED_KEYS);
            this.vendorID = DatabaseUtil.getGeneratedId(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }

    static List<Vendor> getVendor(String name) {
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            List<Vendor> result = new ArrayList<>();
            String sql = String.format("SELECT * FROM vendor WHERE ven_name = \'%s\';", name);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.add(new Vendor(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getInt(5), rs.getString(6)));
            }
            if (result.isEmpty()) {
                throw new IMSException("Vendor: %s doesn't exist", name);
            } else {
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }

    public HashSet<Item> getItemList() {
        if (this.itemList != null) {
            return new HashSet<>(this.itemList);
        }
        this.itemList = new HashSet<>();
        try (Connection con = DatabaseUtil.createConnection();
             CallableStatement stmt = con.prepareCall("{CALL GET_ITEMS_FROM_VENDOR(?)}")) {
            stmt.setString(1, this.vendorName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                this.itemList.add(new Item(rs.getInt(1), rs.getString(2)
                        , rs.getString(3), rs.getDouble(4)));
            }
            return new HashSet<>(this.itemList);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }

    }


    void addSoldItem(HashSet<Item> items) {
        CallableStatement stmt = null;
        Connection con = null;
        try {
            con = DatabaseUtil.createConnection();
            con.setAutoCommit(false);
            for (Item i : items) {
                stmt = con.prepareCall("{CALL INSERT_VENDOR_HAS_ITEM(?, ?)}");
                stmt.setString(1, this.vendorName);
                stmt.setString(2, i.getItemName());
                stmt.execute();
            }
            // TODO: How to deal with items not existing in ITEM table
            // TODO: SOLUTION: just insert relationship with existing items and return a list of new items
            con.commit();
        } catch (SQLException e) {
            DatabaseUtil.rollback(con);
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        } finally {
            DatabaseUtil.close(stmt);
            DatabaseUtil.close(con);
        }
    }

    private String insertStmt() {
        return "INSERT INTO vendor (ven_name, ven_address, ven_state, ven_zip, ven_description) VALUES (\'"
                + this.vendorName + "\', \'"
                + this.vendorAddress + "\', \'"
                + this.vendorState + "\', "
                + this.vendorZip + ", \'"
                + this.vendorDescription + "\');";
    }


    @Override
    public String toString() {
        return "Vendor{" +
                "vendorID=" + vendorID +
                ", vendorName='" + vendorName + '\'' +
                ", vendorAddress='" + vendorAddress + '\'' +
                ", vendorState=" + vendorState +
                ", vendorZip='" + vendorZip + '\'' +
                ", vendorDescription='" + vendorDescription + '\'' +
                '}';
    }


    public int getVendorID() {
        return vendorID;
    }


    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public String getVendorState() {
        return vendorState;
    }

    public void setVendorState(String vendorState) {
        this.vendorState = vendorState;
    }

    public int getVendorZip() {
        return vendorZip;
    }

    public void setVendorZip(int vendorID) {
        this.vendorID = vendorID;
    }

    public String getVendorDescription() {
        return vendorDescription;
    }

    public void setVendorDescription(String vendorDescription) {
        this.vendorDescription = vendorDescription;
    }
}
