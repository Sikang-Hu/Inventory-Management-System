package IMS.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import IMS.IMSException;

public class ReorderPoint {

    public class reorderObject{
        private int itemID;
        private String  itemName;
        private int storeID;
        private String message;

        reorderObject (int itemID, int storeID, String message) {
            this.itemID = itemID;
            this.storeID = storeID;
            this.message = message;
        }

        void setItemName() {this.itemName = new ItemDAO().getItemByID(this.itemID).getItemName();}


        public int getitemID() {
            return this.itemID;
        }


        public int getStoreID() {
            return this.storeID;
        }


        public String  getMessage() {
            return this.message;
        }

        @Override
        public String toString() {
            return //"item_id: " + this.itemID +
                   " itemName: " + this.itemName +
                    //" storeID: " + this.storeID +
                    " message: " + this.message;
        }
    }



    List<reorderObject> getReminderByItemId(int itemId) {
        String sql = "SELECT * FROM inv_reminder where item_id = " + itemId + ";";
        return filterReminders(sql);
    }

    List<reorderObject> getReminderByStoreId(int storeId) {
        String sql = "SELECT * FROM inv_reminder where store_id = " + storeId + ";";
        return filterReminders(sql);
    }

    List<reorderObject> getReminderByItemIdAndStoreId(int itemId, int storeId) {
        String sql = "SELECT * FROM inv_reminder where item_id = " + itemId
                     + "and store_id = " + storeId + ";";
        return filterReminders(sql);
    }

    List<reorderObject> getAllReminders() {
        String sql =  "SELECT * FROM inv_reminder;";
        return filterReminders(sql);
    }

    List<reorderObject> filterReminders(String sql) {
        List<reorderObject> list = new ArrayList<>();
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                reorderObject object = new reorderObject(rs.getInt(2), rs.getInt(1),
                        rs.getString(3));
                object.setItemName();
                list.add(object);
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }

    }


}
