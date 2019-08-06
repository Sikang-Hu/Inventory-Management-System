package IMS.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import IMS.IMSException;

public class ProfitAnalysis {


    HashMap<Integer, profitAnalysisObject> map = new HashMap<>();

    class profitAnalysisObject {
        private int itemID;
        private double avgInventory;
        private double profit;
        private double ratio;

        profitAnalysisObject(int item_id) {
            this.itemID = item_id;

        }

        void setAvgInventory(double avgInventory) {
            this.avgInventory = avgInventory;
        }

        void setProfit(double profit) {
            this.profit = profit;
        }

        double getProfit() {
            return this.profit;
        }

        void computeRatio() {
            this.ratio = (double) Math.round(this.profit / this.avgInventory * 100) / 100;

        }

        double getRatio() {
            return this.ratio;
        }

        @Override
        public String toString() {
            return "itemID: " + this.itemID +
                    " avgInventory: " + this.avgInventory +
                    " profit: " + this.profit +
                    " ratio: " + this.ratio;
        }

    }

        void getAllIDs() {
            String sql = "SELECT item_id FROM item;";
            try (Connection con = DatabaseUtil.createConnection();
                 Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    this.map.put(rs.getInt(1), new profitAnalysisObject(rs.getInt(1)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IMSException(e.getMessage());
            }
        }

    void getAvgInventoryforItem() {
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

    void getProfitforItem() {
        String sql =  "CALL get_weekly_profit_by_item(null, null);";
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                profitAnalysisObject object = this.map.get(rs.getInt(3));
                object.setProfit(rs.getDouble(6));
                object.computeRatio();

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }

    void getProfitRatio() {
        getAllIDs();
        getAvgInventoryforItem();
        getProfitforItem();

        for (Map.Entry<Integer, profitAnalysisObject> entry : this.map.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    public static void main(String[] args) {
        new ProfitAnalysis().getProfitRatio();
    }




    }

