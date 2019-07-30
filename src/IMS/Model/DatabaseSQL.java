package IMS.Model;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseSQL {
  IMSUtil dbu;


  public void authenticate(String user, String password) {
    dbu = new IMSUtil("jdbc:mysql://localhost:3306/ims?serverTimezone=EST5EDT", user, password);
  }


  public void closeConnection() {
    dbu.closeConnection();
  }


  ///////////////////////Add order//////////////////////


  /**
   * Add a new order with vendor object, ItemInOrder object, and store object
   *
   * @param ven_name      vendor must be in the Vendor table
   * @param store_address store must be in the Store table
   * @param order_date    default is current date
   * @param itemInfo in the formate of “iteme_name, item_quantity, unit_cost”
   * @return The newly created supply_order ID or -1 if creation failed.
   */
  public int addNewOrder(String ven_name, String store_address,
                         String order_date, List<String> itemInfo)
          throws IllegalArgumentException {
    int order_id = -1;

    //prase string order_date
    Date date = null;
    try {
      date = new SimpleDateFormat("yyyy-MM-dd").parse(order_date);
    } catch (java.text.ParseException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }

    //find ven_id
    int ven_id = dbu.findTerm("vendor", "ven_id",
            "ven_name = '" + ven_name + "'");
    if (ven_id == -1) {
      throw new IllegalArgumentException("Vendor name must be added to the database first.");
    }

    //find store_id
    int store_id = dbu.findTerm("retail_store", "store_id",
            "store_address = '" + store_address + "'");
    if (store_id == -1) {
      throw new IllegalArgumentException("Not a valid store. ");
    }

    //get order_id
    order_id = addNewOrderHelper(ven_id, store_id, date);
    Map<Item, Integer>itemQuantityMap = processItemList(itemInfo);
    addOrderHasItem(order_id, itemQuantityMap);
    return order_id;
  }

  /**
   * Add a new order with supply_order object.
   *
   * @return The newly created supply_order ID or -1 if creation failed.
   */
  private int addNewOrderHelper(int ven_id, int store_id, Date order_date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String sql = "INSERT INTO supply_order (ven_id, store_id, order_date) VALUES" +
            " (" + ven_id + ", " + store_id + ", '"
            + sdf.format(order_date) + "')";
    return dbu.insertOneRecord(sql);
  }

  /**
   *
   * @param itemInfo in the formate of “iteme_name, item_quantity, unit_cost”
   * @return Map of Item Object and it's quantity
   */
  private Map<Item, Integer> processItemList(List<String> itemInfo)
          throws IllegalArgumentException {
    Map<Item, Integer> result = new HashMap<>();
    String item_name;
    Double unit_cost;
    int quantity;
    for (String i : itemInfo) {
      String[] i_array = i.split(",");
      item_name = i_array[0];
      unit_cost = Double.parseDouble(i_array[2]);
      int item_id = dbu.findTerm("item", "item_id", "item_name = '"
              + item_name + "'");
      if (item_id == -1) {
        throw new IllegalArgumentException("Item is not in the database, add item first. ");
      }

      Item newItem = new Item(item_id, -1, item_name, unit_cost);

      quantity = Integer.parseInt(i_array[1]);
      result.put(newItem, quantity);
    }
    return result;
  }



  /**
   * Add to order Has Item table
   * @param order_id
   * @param itemQuantityMap map of Item object and corresponding order quantity.
   */
  private void addOrderHasItem(int order_id, Map<Item, Integer> itemQuantityMap) {
    try {
      Connection con = dbu.getConnection();
      CallableStatement stmt = con.prepareCall("{call INSERT_INTO_ORDER_HAS_ITEM(?,?,?,?)}");
      for (Map.Entry<Item, Integer> e : itemQuantityMap.entrySet()) {
        int item_id = e.getKey().getItemId();
        double unit_cost = e.getKey().getItemPrice();
        int quantity = e.getValue();
        stmt.setInt(1, order_id);
        stmt.setInt(2, item_id);
        stmt.setInt(3, quantity);
        stmt.setDouble(4, unit_cost);
        stmt.execute();
      }
      stmt.close();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }

  }

  /**
   * Update the dilevery_date of an supply_order to current date
   *
   * @param order_id order_id which needs to be updated.
   */
  public void UpdateDeliveryDate(int order_id) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDateTime now = LocalDateTime.now();
    dbu.updateTerm("supply_order", "delivery_date",
            "'" + dtf.format(now).toString() + "'",
            "order_id = " + order_id);
  }

}






