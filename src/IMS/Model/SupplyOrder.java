package IMS.Model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class SupplyOrder {
  private int order_id;
  private Vendor vendor;
  private RetailStore store;
  private Date order_date;
  private Date delivery_date;
  private Map<Item, Integer> items;

  public SupplyOrder( int order_id, Vendor vendor, RetailStore store,
                      Date order_date, Map<Item, Integer> items) {
    this.order_id = order_id;
    this.store = store;
    this.vendor = vendor;
    this.order_date = order_date;
    this.delivery_date = null;
    this.items = items;
  }

  public SupplyOrder( Vendor vendor, RetailStore store, Date order_date, Map<Item, Integer> items) {
    this(-1, vendor, store, order_date, items);
  }

  public int insertSupplyOrder() {
    int key = -1;
    Connection con = null;
    Statement stmt = null;
    CallableStatement cstmt = null;

    try {
      con = DatabaseUtil.createConnection();
      stmt = con.createStatement();
      con.setAutoCommit(false);
      System.out.println(this.insertOrder());
      stmt.executeUpdate(this.insertOrder(), Statement.RETURN_GENERATED_KEYS);
      ResultSet rs = stmt.getGeneratedKeys();
      if(rs.next()) {
        key = rs.getInt(1);
      }
      System.out.println("order_id: "+ key);

      cstmt = con.prepareCall("{call INSERT_INTO_SKU(?,?,?,?)}");
      for (Map.Entry<Item, Integer> e : items.entrySet()) {
        int item_id = e.getKey().getItemId();
        double unit_cost = e.getKey().getItemPrice();
        int quantity = e.getValue();
        System.out.println(String.format("order_id: %d, item_id: %d, quantity: %d", key, item_id, quantity)
                + "cost: "+ unit_cost);
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
    } finally {
      DatabaseUtil.close(stmt);
      DatabaseUtil.close(cstmt);
      DatabaseUtil.close(con);
    }
    return key;
  }

  private String insertOrder() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return "INSERT INTO supply_order (ven_id, store_id, order_date) VALUES" +
            " (" + vendor.getVendorID() + ", " + store.getStoreId() + ", '"
            + sdf.format(order_date) + "')";
  }




  public Date getDelivery_date() {
    return delivery_date;
  }

  public Date getOrder_date() {
    return order_date;
  }

  public int getOrder_id() {
    return order_id;
  }

  public RetailStore getStore() {
    return store;
  }

  public Vendor getVendor() {
    return vendor;
  }

  public void setDelivery_date(Date delivery_date) {
    this.delivery_date = delivery_date;
  }

  public void setOrder_date(Date order_date) {
    this.order_date = order_date;
  }

  public void setOrder_id(int order_id) {
    this.order_id = order_id;
  }

  public void setStore_id(RetailStore store) {
    this.store = store;
  }

  public void setVen_id(Vendor vendor) {
    this.vendor = vendor;
  }
}
