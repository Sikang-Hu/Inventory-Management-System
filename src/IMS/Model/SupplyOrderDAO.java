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


public class SupplyOrderDAO {

  //input order_id, return one orderDTO object.
  public SupplyOrderDTO getOrderByID(int order_id) {
    try (Connection con = DatabaseUtil.createConnection();
         Statement stmt = con.createStatement()) {
      String sql = "SELECT * FROM supply_order WHERE order_id =" + order_id;
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return new SupplyOrderDTO(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                rs.getDate(4), rs.getDate(5));
      } else {
        throw new IMSException("OrderId: %d doesn't exist", order_id);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IMSException(e.getMessage());
    }
  }

  //input orderDTO object, and list of ItemInTransaction, insert this order.
  public int insertSupplyOrder(SupplyOrderDTO order, List<ItemInTransaction> items) {
    int key = -1;
    Connection con = null;
    Statement stmt = null;
    CallableStatement cstmt = null;

    try {
      con = DatabaseUtil.createConnection();
      stmt = con.createStatement();
      con.setAutoCommit(false);

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String sql = "INSERT INTO supply_order (ven_id, store_id, order_date) VALUES" +
              " (" + order.getVen_id() + ", " + order.getStore_id() + ", '"
              + sdf.format(order.getOrder_date()) + "')";
      stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) {
        key = rs.getInt(1);
      }

      cstmt = con.prepareCall("{call INSERT_INTO_SKU(?,?,?,?)}");
      for (ItemInTransaction i : items) {
        int item_id = i.getItem_id();
        double unit_cost = i.getUnit_cost();
        int quantity = i.getOrder_quantity();
        cstmt.setInt(1, key);
        cstmt.setInt(2, item_id);
        cstmt.setInt(3, quantity);
        cstmt.setDouble(4, unit_cost);
        cstmt.execute();
      }

      con.commit();
    } catch (SQLException e) {
      DatabaseUtil.rollback(con);
      e.printStackTrace();
      throw new IMSException(e.getMessage());
    } finally {
      DatabaseUtil.close(stmt);
      DatabaseUtil.close(cstmt);
      DatabaseUtil.close(con);
    }
    return key;
  }

  //input order id, return list of Item belongs to this order
  public List<ItemInTransaction> getOrderItems(int order_id) {
    List<ItemInTransaction> itemList = new ArrayList<>();
    try (Connection con = DatabaseUtil.createConnection();
         Statement stmt = con.createStatement()) {
      String sql = "SELECT * FROM sku WHERE order_id =" + order_id;
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

  //input vendor id and date, return list of orderDTO object.
  public List<SupplyOrderDTO> getOrdersByVen_Date(int ven_id, Date orderDate) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String date = df.format(orderDate);
    String sql = "SELECT * FROM supply_order WHERE ven_id = " + ven_id + " and order_date = '" + date + "'";
    System.out.println(sql);
    return filterOrders(sql);
  }

  //filter order by store
  public List<SupplyOrderDTO> getOrdersByStore_Date(int store_id, Date orderDate) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String date = df.format(orderDate);
    String sql = "SELECT * FROM supply_order WHERE store_id = " + store_id + " and order_date = '" + date + "'";
    return filterOrders(sql);
  }

  //update delivery
  public void updateDeliveryDate(int order_id) {
    try (Connection con = DatabaseUtil.createConnection();
         CallableStatement cstmt = con.prepareCall("{call update_order_for_delivery(?)}")) {
      cstmt.setInt(1, order_id);
      cstmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IMSException(e.getMessage());
    }
  }


  private List<SupplyOrderDTO> filterOrders(String sql){
    List<SupplyOrderDTO> orders = new ArrayList<>();
    try (Connection con = DatabaseUtil.createConnection();
         Statement stmt = con.createStatement()) {
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        orders.add(new SupplyOrderDTO(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                rs.getDate(4), rs.getDate(5)));
      }
      if (orders.isEmpty()) {
        throw new IMSException("No order found.");
      }
      return orders;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IMSException(e.getMessage());
    }
  }


}




