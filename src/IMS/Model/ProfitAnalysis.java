package IMS.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import IMS.IMSException;

public class ProfitAnalysis {


     private HashMap<Integer, profitAnalysisObject> map = new HashMap<>();

    class profitAnalysisObject {
        private int itemID;
        private int storeID;
        private double avgInventory = 0;
        private double profit = 0;
        private double ratio;

        profitAnalysisObject(int itemID, int storeID) {
            this.itemID = itemID;
            this.storeID = storeID;

        }

        void setAvgInventory(double avgInventory) {
            this.avgInventory = avgInventory;
        }

        void increaseProfit(double profit) {
            this.profit = this.profit + profit;
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
        public int hashCode() {
            return Objects.hash(this.itemID, this.storeID);
        }

        @Override
        public String toString() {
            return "itemID: " + this.itemID +
                    "storeID " + this.storeID +
                    " avgInventory: " + this.avgInventory +
                    " profit: " + this.profit +
                    " ratio: " + this.ratio;
        }

    }

    private void getAvgInventoryByItemID(int itemID, String startDate, String endDate) {
        String sql = "CALL get_avg_hist_inv_by_item(null," + itemID + ", " + startDate +
                ", " + endDate + ");";
        filterAvgInventory(sql);
    }

    private void getAvgInventoryByStoreID(int storeID, String startDate, String endDate) {
        String sql = "CALL get_avg_hist_inv_by_item("+ storeID +", null," + startDate +
                     ", " + endDate +");";
        filterAvgInventory(sql);
    }

    private void getAvgInventoryByItemIDAndStoreID(int itemID, int storeID, String startDate, String endDate) {
        String sql = "CALL get_avg_hist_inv_by_item(" + storeID + ", "+ itemID
                + ", " + startDate + ", " + endDate + ");";
        filterAvgInventory(sql);
    }

    private void getAllAvgInventory(String startDate, String endDate) {
        String sql =  "CALL get_avg_hist_inv_by_item(null, null," + startDate + ", " + endDate + ");";
        filterAvgInventory(sql);
    }


    void filterAvgInventory(String sql) {
        try (Connection con = DatabaseUtil.createConnection();

             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                profitAnalysisObject object = new profitAnalysisObject(rs.getInt(2),
                                              rs.getInt(1));
                object.setAvgInventory(rs.getDouble(4));
                this.map.put(object.hashCode(), object);
//                System.out.println(object);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }

    private void getProfitByItemID(int itemID, String startDate, String endDate) {
        String sql = "CALL get_weekly_profit_by_item(null," + itemID + ", " + startDate +
                     ", " + endDate + ");";
        filterProfit(sql);
    }

    private void getProfitByStoreID(int storeID, String startDate, String endDate) {
        String sql = "CALL get_weekly_profit_by_item("+ storeID +", null," + startDate +
                ", " + endDate +");";
        filterProfit(sql);
    }

    private void getProfitByItemIDAndStoreID(int itemID, int storeID, String startDate, String endDate) {
        String sql = "CALL get_weekly_profit_by_item(" + storeID + ", "+ itemID
                      + ", " + startDate + ", " + endDate + ");";
        filterProfit(sql);
    }

    private void getAllProfit(String startDate, String endDate) {
        String sql =  "CALL get_weekly_profit_by_item(null, null," + startDate + ", " + endDate + ");";
        filterProfit(sql);
    }

    void filterProfit(String sql) {
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                profitAnalysisObject newObject = new profitAnalysisObject(rs.getInt(3),
                                                 rs.getInt(1));
                profitAnalysisObject object = this.map.get(newObject.hashCode());
                object.increaseProfit(rs.getDouble(5));
                object.computeRatio();
//                System.out.println(object);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }

    void getProfitRatioByItemId(int itemID, Date startDate, Date endDate) {
        String start;
        String end;
        if (startDate == null || endDate == null) {
            start = null;
            end = null;
        }
        else {
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            start = df.format(startDate);
            end = df.format(endDate);
        }

        getAvgInventoryByItemID(itemID, start, end);
        getProfitByItemID(itemID, start, end);
    }

    void getProfitRatioByStoreId(int storeID, Date startDate, Date endDate) {
        String start;
        String end;
        if (startDate == null || endDate == null) {
            start = null;
            end = null;
        }
        else {
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            start = df.format(startDate);
            end = df.format(endDate);
        }

        getAvgInventoryByStoreID(storeID, start, end);
        getProfitByStoreID(storeID, start, end);
    }

    void getProfitRatioByItemIdAndStoreId(int storeID, int itemID, Date startDate, Date endDate) {
        String start;
        String end;
        if (startDate == null || endDate == null) {
            start = null;
            end = null;
        }
        else {
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            start = df.format(startDate);
            end = df.format(endDate);
        }

        getAvgInventoryByItemIDAndStoreID(itemID, storeID, start, end);
        getProfitByItemIDAndStoreID(itemID, storeID, start, end);
    }


    void getAllProfitRatio(Date startDate, Date endDate) {
        String start;
        String end;
        if (startDate == null || endDate == null) {
            start = null;
            end = null;
        }
        else {
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            start = df.format(startDate);
            end = df.format(endDate);
        }

        getAllAvgInventory(start, end);
        getAllProfit(start, end);




    }

    HashMap<Integer, profitAnalysisObject> getMap() {
        return this.map;
    }

    }

