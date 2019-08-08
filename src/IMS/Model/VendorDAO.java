package IMS.Model;

import IMS.IMSException;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class VendorDAO {


    int insertVendor(VendorDTO vendor) {
        String sql = this.insertStmt(vendor);
        return DatabaseUtil.insertOneRecord(sql);
    }

    VendorDTO getVendorByName(String name) {
        String sql = String.format("SELECT * FROM vendor WHERE ven_name = \'%s\';", name);
        return getVendor(sql);
    }

    VendorDTO getVendorByID(int id) {
        String sql = String.format("SELECT * FROM vendor WHERE ven_id = \'%d\';", id);
        return getVendor(sql);
    }


    public Set<ItemDTO> getSoldItem(VendorDTO vendor) {
        Set<ItemDTO> result = new HashSet<>();
        try (Connection con = DatabaseUtil.createConnection();
             CallableStatement stmt = con.prepareCall("{CALL GET_ITEMS_FROM_VENDOR(?)}")) {
            stmt.setString(1, vendor.getVendorName());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new ItemDTO(rs.getInt(1), rs.getString(2)
                        , rs.getString(3), rs.getDouble(4)));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }

    }


    void addSoldItem(String vendorName, Iterable<ItemDTO> items) {
        CallableStatement stmt = null;
        Connection con = null;
        try {
            con = DatabaseUtil.createConnection();
            con.setAutoCommit(false);
            for (ItemDTO i : items) {
                stmt = con.prepareCall("{CALL INSERT_VENDOR_HAS_ITEM(?, ?)}");
                stmt.setString(1, vendorName);
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

    private String insertStmt(VendorDTO vendor) {
        return "INSERT INTO vendor (ven_name, ven_address, ven_state, ven_zip, ven_description) VALUES (\'"
                + vendor.getVendorName() + "\', \'"
                + vendor.getVendorAddress() + "\', \'"
                + vendor.getVendorState() + "\', "
                + vendor.getVendorZip() + ", \'"
                + vendor.getVendorDescription() + "\');";
    }


    private VendorDTO getVendor(String sql) {
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return new VendorDTO(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getInt(5), rs.getString(6));
            }
            else {
                throw new IMSException("Vendor doesn't exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }
}

