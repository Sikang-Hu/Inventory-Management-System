package IMS.Model;

public interface IDatabaseSQL {
  int insertItem(Item item);

  int insertSupplyOrder(SupplyOrder supplyOrder);

  int insertVendor(Vendor vendor);

  int insertRetailStore(RetailStore retailStore);

  int insertSale(Sale sale);

  int insertCategory(Category category);

  int insertCustomer(Customer customer);

  void authenticate(String user, String password);

  void closeConnection();
}