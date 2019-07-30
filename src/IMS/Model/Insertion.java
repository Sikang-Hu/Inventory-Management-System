package IMS.Model;


public class Insertion {

  IMSUtil dbu;


  public int insertCustomer(Customer customer) {
    String sql = "INSERT INTO customer (cus_name) VALUES"
            + " ('" + customer.getName()+"')";
    return dbu.insertOneRecord(sql);
  }


  public int insertItem(Item item) {
    String sql = "INSERT INTO item (itemId,categoryID,itemName,itemPrice) VALUES" +
            "('"+item.getItemId()+"','"+item.getCategoryID()+"','"+item.getItemName()+"','"+ item.getItemPrice() + "')";
    return dbu.insertOneRecord(sql);
  }


  public int insertCategory(Category category) {
    String sql = "INSERT INTO item_category (cat_name, cat_description) VALUES ('"
            + category.getCategoryName()+"', '"+category.getCategoryDescription()+"')";
    return dbu.insertOneRecord(sql);
  }

  public int insertOrderHasItem() {
    return 0;
  }

  public int insertRetailStore(RetailStore retailStore) {
    String sql = "INSERT INTO retail_store(store_address, store_state, store_zip) VALUES"
            + " ('"+retailStore.getStoreAddress()+"', '"+retailStore.getStoreState()+"', "+retailStore.getStoreZip()+")";
    return dbu.insertOneRecord(sql);
  }

  public int insertSale() {
    return 0;
  }


  public int insertSupplyOrder(SupplyOrder supplyOrder) {
    return 0;
  }

  public int insertVendor(Vendor vendor) {
    String sql = "INSERT INTO vendor (ven_name, ven_address, ven_state, ven_zip, ven_description) VALUES"
            + " ('"+vendor.getVendorName()+"', '"+vendor.getVendorAddress()+"', '"+vendor.getVendorState()+"', "
            +vendor.getVendorZip()+", '"+vendor.getVendorDescription()+"')";
    return dbu.insertOneRecord(sql);
  }

  public int insertVendorHasItem(Item i) {
    String sql = "INSERT INTO vendor_has_item (item_id) VALUES"
            + " ("+i.getItemId()+")";
    return dbu.insertOneRecord(sql);
  }


}
