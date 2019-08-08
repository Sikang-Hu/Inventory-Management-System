package IMS.Model;


import java.util.Date;
import java.util.List;
import java.util.Set;

public interface InventoryModel {
  //Category related
  void insertCategory(String categoryName, String categoryDescription);

  CategoryDTO getCatByName(String cat_name);

  CategoryDTO getCatByID(int cat_name);

  //Item related
  void insertItem(String name, String category, double unitPrice);

  ItemDTO getItemByName(String name);

  ItemDTO getItemByID(int id);

  //Vendor related
  void insertVendor(String name, String address, String state, int zip, String description);

  VendorDTO getVendorByName(String name);

  VendorDTO getVendorByID(int id);

  Set<ItemDTO> getSoldItems(String name);

  void addSoldItem(String vendor, Iterable<ItemDTO> collection);

  //Status related
  List<Status> getInvStatus(Integer store_id, String item_name, String cat_name);

  //store related
  void insertStore(String address, String state, int zipCode);

  RetailStoreDTO getStoresByID(int storeID);


  List<RetailStoreDTO> getAllStore();

  //SupplyOrder related
  int insertOrder(String ven_name, int store_id, Date date, Iterable<ItemInTransaction> itemInfo);

  List<ItemInTransaction> getOrderItems(int order_id);

  List<SupplyOrderDTO> getOrdersByVen_Date(String ven_name, Date date);

  List<SupplyOrderDTO> getOrdersByStore_Date(int store_id, Date date);

  SupplyOrderDTO getOrderByID(int order_id);

  void updateDeliveryDate(int order_id);

  //Sale related

  int insertSale(Integer cus_id, int store_id, Date date, Iterable<ItemInTransaction> items);

  List<ItemInTransaction> getSaleItems(int sale_id);

  List<SaleDTO> getSalesByCus_Date(int customer, Date date);

  List<SaleDTO> getSalesStore_Date(int store_id, Date saleDate);

  SaleDTO getSaleByID(int saleID);

  void returnSale(int sale_id, String item_name, int quantity);



}