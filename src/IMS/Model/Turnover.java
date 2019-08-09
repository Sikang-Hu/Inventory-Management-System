package IMS.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import IMS.IMSException;

public class Turnover {


    class turnoverObject {
        private int itemID;
        private double storeID;
        private double turnover;

        turnoverObject (int item_id, int storeID, double turnover) {
            this.itemID = item_id;
            this.storeID = storeID;
            this.turnover = turnover;
        }

        double getTurnover() {
            return this.turnover;
        }

        @Override
        public String toString() {
            return "itemID: " + this.itemID +
                    " storeID: " + this.storeID +
                    " turnover: " + this.turnover;
        }

    }


    List<turnoverObject> getTurnoverByItemId(int itemId, int numPastWeek) {
        String sql = "CALL get_itr_by_item_for_past_num_week(null, " + itemId +"," +
                      numPastWeek + ");";
        return filterTurnovers(sql);
    }

    List<turnoverObject> getTurnoverByStoreId(int storeId, int numPastWeek) {
        String sql = "CALL get_itr_by_item_for_past_num_week(" + storeId +
                     ", null, " + numPastWeek + ");";
        return filterTurnovers(sql);
    }

    List<turnoverObject> getTurnoverByItemIdAndStoreId(int itemId, int storeId, int numPastWeek) {
        String sql = "CALL get_itr_by_item_for_past_num_week(" + storeId +
                "," + itemId +", " + numPastWeek + ");";
        return filterTurnovers(sql);
    }

    List<turnoverObject> getAllTurnovers(int numPastWeek) {
        String sql =  "CALL get_itr_by_item_for_past_num_week(null, null, " + numPastWeek + ");";
        return filterTurnovers(sql);
    }

    List<turnoverObject> filterTurnovers(String sql) {
        List<turnoverObject> list = new ArrayList<>();
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                turnoverObject object = new turnoverObject(rs.getInt(2),
                        rs.getInt(1),
                        rs.getDouble(7));
                list.add(object);
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }

    }
}
