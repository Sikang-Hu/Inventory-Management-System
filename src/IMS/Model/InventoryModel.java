package IMS.Model;

public interface InventoryModel {

  void insertItem(String name, String category, double unitPrice);

  Item getItem(String name);

  void insertVendor(String name, String address, String state, String zip, String description);

  Vendor getVendor(String name);

  void insertStore();

  void insertOrder(String o);


}
