package IMS.Model;

public interface IDatabaseSQL {
    public int insertItem(Item item);

    public int insertSupplyOrder(SupplyOrder supplyOrder);

    public int insertVendor(Vendor vendor);

    public int insertRetailStore(RetailStore retailStore);

    public int insertSale(Sale sale);

    public int insertCategory(Category category);

    public int insertCustomer(Customer customer);

    public void authenticate(String user, String password);

    public void closeConnection();
}
