package IMS.Model;

public interface IDatabaseSQL {
    public int insertItem(int itemId, int categoryID, String itemName, int itemPrice);

    public int insertVendor(String vendorName, String vendorAddress, String vendorState, int vendorZip,String vendorDescription);

    public int insertRetailStore(String storeAddress,String StoreState, int storeZip);

    public int insertCategory(String categoryName, String categoryDescription);

    public int insertCustomer(String customerName);

    public void authenticate(String user, String password);

    public void closeConnection();
}
