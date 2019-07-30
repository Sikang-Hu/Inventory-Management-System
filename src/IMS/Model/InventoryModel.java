package IMS.Model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface InventoryModel {

  void insertItem(String name, String category, double unitPrice);

  Item getItem(String name);

  void insertVendor(String name, String address, String state, String zip, String description);

  List<Vendor> getVendor(String name);

  void insertStore(String address, String state, int zipCode);

  List<RetailStore> getStores();

  int insertOrder(String vendor, Date date, List<Item> items);

  int insertSale(String customer, Date date, Map<String, Integer> items);

  // TODO: can same customer make several order in one day
  List<SaleOrder> getSales(String customer, Date date);

  SaleOrder getSale(int saleID);

  void insertCustomer(String name);


}
