package IMS.Model;

import java.util.*;

public interface InventoryModel {

  void insertItem(String name, String category, double unitPrice);

  Item getItem(String name);

  void insertVendor(String name, String address, String state, int zip, String description);

  List<Vendor> getVendor(String name);


  Set<Item> getSoldItems(String name);

  List<Status> getInvStatus();

  void insertStore(String address, String state, int zipCode);

  List<RetailStore> getStores(String name);

  int insertOrder(String vendor, Date date, List<Item> items);
  // TODO: Change Object Date to String date?
  int insertOrder(String ven_name, int store_id, Date date, List<String> itemInfo);

  int insertSale(String customer, Date date, Map<String, Integer> items);

  // TODO: can same customer make several order in one day
  List<Sale> getSales(String customer, Date date);

  Sale getSale(int saleID);

  void insertCustomer(String name);


}