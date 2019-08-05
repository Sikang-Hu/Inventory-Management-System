package IMS.Model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import java.util.List;

import IMS.IMSException;

public class SaleDAO {

    //input sale_id, return one SaleDTO object.
    public SaleDTO getOrderByID(int sale_id) {
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            String sql = "SELECT * FROM sale WHERE order_id =" + sale_id;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return new SaleDTO(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                        rs.getDate(4));
            } else {
                throw new IMSException("Sale_Id: %d doesn't exist", sale_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }



    //input saleDTO item and list of ItemInTransaction, insert this sale to database
    public int insertSale(SaleDTO sale, List<ItemInTransaction> items, String cus_name) {
        int key = -1;
        Connection con = null;
        CallableStatement cstmt = null;
        CallableStatement cstmt1 = null;

        try {
            con = DatabaseUtil.createConnection();
            cstmt = con.prepareCall("{call INSERT_INTO_SALE(?,?,?,?)}");
            con.setAutoCommit(false);
            cstmt.setInt(1, sale.getStore_id());
            cstmt.setDate(2, (java.sql.Date) sale.getSaleDate());
            cstmt.setInt(3, sale.getCustomer_id());
            cstmt.setString(4, cus_name);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            if (rs.next()) {
                key = rs.getInt(1);
            }
            cstmt1 = con.prepareCall("{call INSERT_INTO_SALE_HAS_SQL(?,?,?,?)}");
            for (ItemInTransaction i : items) {
                int item_id = i.getItem_id();
                double unit_cost = i.getUnit_cost();
                int quantity = i.getOrder_quantity();
                cstmt1.setInt(1, key);
                cstmt1.setInt(2, item_id);
                cstmt1.setInt(3, quantity);
                cstmt1.setDouble(4, unit_cost);
                cstmt1.execute();
            }
            con.commit();
        } catch (SQLException e) {
            DatabaseUtil.rollback(con);
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        } finally {
            DatabaseUtil.close(cstmt1);
            DatabaseUtil.close(cstmt);
            DatabaseUtil.close(con);
        }
        return key;
    }


    //get sale items by sale id
    public List<ItemInTransaction> getSaleItems(int sale_id) {
        List<ItemInTransaction> itemList = new ArrayList<>();
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            String sql = "SELECT * FROM sale_has_sku WHERE sale_id =" + sale_id;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                itemList.add(new ItemInTransaction(rs.getInt(3), rs.getInt(4),
                        rs.getDouble(5)));
            }
            return itemList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }


    //get list of sale by customer id
    public List<SaleDTO> getSalesByCus_Date(int cus_id, Date saleDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(saleDate);
        String sql = "SELECT * FROM sale WHERE cus_id = " + cus_id + "and order_date = ''" + date + "''";
        return filterSales(sql);
    }

    //get list of sale by store id
    public List<SaleDTO> getSalesStore_Date(int store_id, Date saleDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(saleDate);
        String sql = "SELECT * FROM sale WHERE store_id = " + store_id + "and order_date = ''" + date + "''";
        return filterSales(sql);
    }

    private List<SaleDTO> filterSales(String sql) {
        List<SaleDTO> sales = new ArrayList<>();
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                sales.add(new SaleDTO(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                        rs.getDate(4)));
            }
            if (sales.isEmpty()) {
                throw new IMSException("No order found.");
            }
            return sales;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }



}

