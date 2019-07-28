package IMS.Model;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class SQL {
  IMSUtil dbu;


  public void authenticate(String user, String password) {
    dbu = new IMSUtil("jdbc:mysql://localhost:3306/ims?serverTimezone=EST5EDT", user, password);
  }

  public void closeConnection() { dbu.closeConnection(); }


  ////////////ADD To each table///////////////////
  public int addCategory(Category category) {
    String sql = "INSERT INTO item_category (cat_name, cat_description) VALUES ('"
            + category.getCategoryName()+"', '"+category.getCategoryDescription()+"')";
    return dbu.insertOneRecord(sql);
  }

  public int addItem(Item item) {
    String sql = "INSERT INTO item (cat_id, item_name, item_unit_price) VALUES ("
            + item.getItemId()+", '"+item.getItemName()+"', "+item.getItemPrice()+")";
    return dbu.insertOneRecord(sql);
  }

  public int addVendor(Vendor ven) {
    String sql = "INSERT INTO vendor (ven_name, ven_address, ven_state, ven_zip, ven_description) VALUES"
            + " ('"+ven.getVendorName()+"', '"+ven.getVendorAddress()+"', '"+ven.getVendorState()+"', "
            +ven.getVendorZip()+", '"+ven.getVendorDescription()+"')";
    return dbu.insertOneRecord(sql);
  }

  public int addStore(RetailStore store) {
    String sql = "INSERT INTO retail_store(store_address, store_state, store_zip) VALUES"
            + " ('"+store.getStoreAddress()+"', '"+store.getStoreState()+"', "+store.getStoreZip()+")";
    return dbu.insertOneRecord(sql);
  }

  public int addCustomer(Customer c) {
    String sql = "INSERT INTO customer (cus_name) VALUES"
            + " ('" + c.getName()+"')";
    return dbu.insertOneRecord(sql);
  }

  public int addOrderHasItem(int order_id, List<ItemInOrder> items) {
    return 0;
  }


  ///////////////////////Add order//////////////////////
  /**
   * Add a new order with supply_order object.
   * @param order supply_order object represents an order.
   * @return The newly created supply_order ID or -1 if creation failed.
   */
  public int addNewOrder(SupplyOrder order) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String sql = "INSERT INTO supply_order (ven_id, store_id, order_date) VALUES" +
            " ("+order.getVen_id()+", "+order.getStore_id()+", '"
            +sdf.format(order.getOrder_date())+"')";
    return dbu.insertOneRecord(sql);
  }

  /**
   * Add a new order with vendor object, ItemInOrder object, and store object
   * @param ven
   * @param store
   * @param order_date
   * @return The newly created supply_order ID or -1 if creation failed.
   */
  public int addNewOrder1(Vendor ven, RetailStore store,
                          Date order_date, List<ItemInOrder> items) {
    int key = -1;
    int store_id = dbu.findIntTerm("retail_store", "store_id", "store_id",
            store.getStoreId());
    if (store_id == -1) {
      //do not allow adding to the non-exist store.
      return key;
    }
    int ven_id = dbu.findTerm("vendor", "ven_id",
            "ven_name", ven.getVendorName());
    //vendor not exists in the table "vendor", insert
    if (ven_id == -1) {
      addVendor(ven);
    }
    SupplyOrder order = new SupplyOrder(ven_id, store_id, order_date, items);

    key = addNewOrder(order);
    addOrderHasItem(key, items);
    return key;
  }

  /**
   * Update the dilevery_date of an supply_order to current date
   * @param order_id order_id which needs to be updated.
   */
  public void UpdateDeliveryDate(int order_id) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDateTime now = LocalDateTime.now();
    dbu.updateTerm("supply_order", "delivery_date",
            "'"+dtf.format(now).toString()+"'",
            "order_id", Integer.toString(order_id));
  }






///////////////////////////add sale///////////////////////////////////////////
  /**
   * Add a new sale.
   * @param sale sale object.
   * @return The newly created sale_order ID or -1 if creation failed.
   */
  public int addNewSale(Sale sale) {
    return 0;
  }

}
