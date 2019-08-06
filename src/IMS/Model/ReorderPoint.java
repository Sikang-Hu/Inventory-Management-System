package IMS.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import IMS.IMSException;

public class ReorderPoint {

    class reorderObject{
        private int itemID;
        private int saleQuantity;
        private int leadTime;
        private int reorderPoint;

        reorderObject (int itemID) {
            this.itemID = itemID;
        }

        void setSaleQuantity(int quantity) {
            this.saleQuantity = quantity;
        }

        int getSaleQuantity() {
            return this.saleQuantity;
        }

        void setLeadTime(int leadTime) {
            this.leadTime = leadTime;
        }

        int getLeadTime() {
            return this.leadTime;
        }

        void computeReorderPoint() {
            this.reorderPoint = saleQuantity * leadTime;
        }

        int getReorderPoint() {
            return this.reorderPoint;
        }

        @Override
        public String toString() {
            return "item_id: " + this.itemID +
                    " saleQuantity: " + this.saleQuantity +
                    " leadTime: " + this.leadTime +
                    " reorderPoint: " + this.reorderPoint;
        }
    }

    HashMap<Integer, ReorderPoint.reorderObject> getAllIDs() {
        HashMap<Integer, ReorderPoint.reorderObject> map = new HashMap<>();
        String sql =  "SELECT item_id FROM item;";
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                map.put(rs.getInt(1), new ReorderPoint().new reorderObject(rs.getInt(1)));
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }
    //TODO : Update daily quantity(now assume the sale occurred in  a week)
    HashMap<Integer, ReorderPoint.reorderObject>  getSaleQuantityByItem(HashMap<Integer, ReorderPoint.reorderObject> map) {
        String sql =  "call get_sale_by_item(null,null);";
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                map.get(rs.getInt(1)).setSaleQuantity((int) (rs.getInt(6) / 7.0));
            }

            return map;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }

    HashMap<Integer, ReorderPoint.reorderObject> getItemLeadTime(HashMap<Integer, ReorderPoint.reorderObject> map) {
        String sql =  "CALL get_lead_time_by_item(null, null);";
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                reorderObject object = map.get(rs.getInt(1));
                object.setLeadTime(rs.getInt(5));
                object.computeReorderPoint();
            }

            return map;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }

    void updateItemReorderPoint(int itemID, int reorderPonit) {
        String sql =  "update item set reorder_point = " + reorderPonit
                        + " where item_id = " + itemID;
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }




    // ROP = lead time * daily items sold
    // insert to item object
    public void updateAllReorderPoint() {
        //get item_id list
        HashMap<Integer, reorderObject> map = getAllIDs();

        //get daily sale quantity for each item
        getSaleQuantityByItem(map);

        //get lead time for each item and compute reorder point
        getItemLeadTime(map);

        //update RP to item in DB
        for (Map.Entry<Integer, reorderObject> entry : map.entrySet()) {
            updateItemReorderPoint(entry.getKey(), entry.getValue().getReorderPoint());
        }

    }

    public static void main(String[] args) {
        new ReorderPoint().updateAllReorderPoint();

    }
}
