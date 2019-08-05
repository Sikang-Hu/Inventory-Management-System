package IMS.Model;

import java.util.*;

public interface InventoryModel {
  //Category related
  void insertCategory(String categoryName, String categoryDescription);

  //Item related
  void insertItem(String name, String category, double unitPrice);

  Item getItem(String name);

  //Vendor related
  void insertVendor(String name, String address, String state, int zip, String description);

  Vendor getVendor(String name);

  Set<Item> getSoldItems(String name);

  //Status related
  List<Status> getInvStatus();

  //store related
  void insertStore(String address, String state, int zipCode);

  RetailStore getStores(int storeID);

  //SupplyOrder related
  int insertOrder(String ven_name, int store_id, Date date, List<String> itemInfo);

  List<ItemInTransaction> getOrderItems(int order_id);

  List<SupplyOrderDTO> getOrdersByVen_Date(String ven_name, Date date);

  List<SupplyOrderDTO> getOrdersByStore_Date(int store_id, Date date);

  SupplyOrderDTO getOrderByID(int order_id);

  void updateDeliveryDate(int order_id);

  //Sale related
  int insertSale(int cus_id, String cus_name, int store_id, Date date, List<String> itemInfo);

  List<ItemInTransaction> getSaleItems(int sale_id);

  List<SaleDTO> getSalesByCus_Date(int customer, Date date);

  List<SaleDTO> getSalesStore_Date(int store_id, Date saleDate);

  SaleDTO getSaleByID(int saleID);



}