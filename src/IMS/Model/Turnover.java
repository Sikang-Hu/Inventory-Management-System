package IMS.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import IMS.IMSException;

public class Turnover {

    HashMap<Integer, turnoverObject> map = new HashMap<>();

    class turnoverObject {
        private int itemID;
        private double avgInventory;
        private double cogs;
        private double turnover;

        turnoverObject (int item_id) {
            this.itemID = item_id;

        }

        void setAvgInventory(double avgInventory) {
            this.avgInventory = avgInventory;
        }

        void setCogs(double cogs) {
            this.cogs = cogs;
        }

        double getCogs() {
            return this.cogs;
        }

        void computeTurnover() {
            this.turnover = (double) Math.round(this.cogs / this.avgInventory * 100)/ 100   ;

        }

        double getTurnover() {
            return this.turnover;
        }

        @Override
        public String toString() {
            return "itemID: " + this.itemID +
                    " avgInventory: " + this.avgInventory +
                    " cogs: " + this.cogs +
                    " turnover: " + this.turnover;
        }

    }

    void getAllIDs() {
        String sql =  "SELECT item_id FROM item;";
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                this.map.put(rs.getInt(1), new turnoverObject(rs.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }

    void getAvgInventory() {
        String sql =  "CALL get_avg_hist_inv_by_item_without_storeid(null, null, null, null);";
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                this.map.get(rs.getInt(1)).setAvgInventory(rs.getDouble(3) );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }
    void getCogs() {
        String sql =  "call get_cogs_by_item(null, null);";
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                turnoverObject object = this.map.get(rs.getInt(4));
                object.setCogs(rs.getDouble(8));
                object.computeTurnover();

            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }

    void getTurnover() {

        getAllIDs();
        getAvgInventory();
        getCogs();

        for (Map.Entry<Integer, turnoverObject> entry : this.map.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    public static void main(String[] args) {
        Turnover turn = new Turnover();
        turn.getTurnover();

    }



}
