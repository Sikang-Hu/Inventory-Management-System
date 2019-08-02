package IMS.Model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Sale {
  private Customer customer;
  private RetailStore store;
  private Date date;
  private Map<Item, Integer> items;

  Sale(Customer customer, Date date, Map<Item, Integer> items, RetailStore store) {
    this.customer = customer;
    this.date = date;
    this.items = items;
    this.store = store;
  }

  public Customer getCustomer() {
    return this.customer;
  }

  public Date getDate() {
    return this.date;
  }

  public Map<Item, Integer> getItems() {
    return this.items;
  }

  public RetailStore getStore() {
    return this.store;
  }

  int insertSale() {
    int result = -1;
    Connection connection = null;
    Statement stmt = null;
    try  {
      connection = DatabaseUtil.createConnection();
      stmt = connection.createStatement();
      connection.setAutoCommit(true);
      result = stmt.executeUpdate(this.insertStmt(), Statement.RETURN_GENERATED_KEYS);;
      System.out.println("sale_id: "+ result);
      for (Item i : items.keySet()) {
        stmt.executeUpdate(insertItem(i, result));
      }
      connection.commit();
    } catch (SQLException e) {
      DatabaseUtil.rollback(connection);
      e.printStackTrace();
    } finally {
      DatabaseUtil.close(stmt);
      DatabaseUtil.close(connection);
    }
    return result;
  }

  private String insertStmt() {
    String datePtrn = "yyyy-mm-dd";
    return String.format("INSERT INTO sale VALUES (%d, %d, %s)"
            , store.getStoreId(), customer.getCustomerID(),
            new SimpleDateFormat(datePtrn).format(this.date));
  }

  private String insertItem(Item i, int saleID) {
    return String.format("INSERT INTO sale_has_item VALUES (%d, %d, %d, %.2f)"
            , saleID, i.getItemId(), items.get(i), i.getItemPrice());
  }

}