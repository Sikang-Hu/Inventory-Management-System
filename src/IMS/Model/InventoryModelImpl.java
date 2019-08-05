package IMS.Model;

import IMS.IMSException;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class InventoryModelImpl implements InventoryModel {
    @Override
    public void insertCategory(String categoryName, String categoryDescription) {
      Category cat = new Category(categoryName, categoryDescription);
      cat.insertCat();
    }


    @Override
    public void insertItem(String category, String name, double unitPrice) {
        Item i = new Item(category, name, unitPrice);
        i.insertItem();
    }

    @Override
    public Item getItem(String name) {
        return Item.getItem(name);
    }

    @Override
    public void insertVendor(String name, String address, String state, int zip, String description) {
        Vendor v = new Vendor(name, address, state, zip, description);
        v.insertVendor();
    }


    @Override
    public Vendor getVendor(String name) {
        return Vendor.getVendor(name);
    }

    @Override
    public Set<Item> getSoldItems(String name) {
        Vendor v = Vendor.getVendor(name);
        return v.getItemList();
    }

    @Override
    public List<Status> getInvStatus() {
        try (Connection con = DatabaseUtil.createConnection();
             CallableStatement stmt = con.prepareCall("{CALL INV_STATUS(?, ?, ?)}")) {
            stmt.setNull(1, Types.INTEGER);
            stmt.setNull(2, Types.INTEGER);
            stmt.setNull(3, Types.INTEGER);
            ResultSet rs = stmt.executeQuery();
            List<Status> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new Status(rs.getString(2), rs.getString(4)
                        , rs.getString(6), rs.getInt(7)));
            }
            return result;
        } catch (SQLException e) {
            throw new IMSException(e.getMessage());
        }
    }



    @Override
    public void insertStore(String address, String state, int zipCode) {
      RetailStore store = new RetailStore(address, state, zipCode);
      store.insertStore();
    }

    @Override
    public RetailStore getStores(int storeID) {
        return RetailStore.getStore(storeID);
    }

    //order related
    @Override
    public int insertOrder(String ven_name, int store_id, Date date, List<String> itemInfo) {
      int ven_id = Vendor.getVendor(ven_name).getVendorID();
      List<ItemInTransaction> items= processItemList(itemInfo);
      SupplyOrderDTO orderDTO = new SupplyOrderDTO(ven_id,store_id,date);
      SupplyOrderDAO order = new SupplyOrderDAO();
      return order.insertSupplyOrder(orderDTO, items);
    }

    @Override
    public List<ItemInTransaction> getOrderItems(int order_id) {
      SupplyOrderDAO order = new SupplyOrderDAO();
      return order.getOrderItems(order_id);
    }


    @Override
    public List<SupplyOrderDTO> getOrdersByVen_Date(String ven_name, Date date) {
      int ven_id = Vendor.getVendor(ven_name).getVendorID();
      SupplyOrderDAO order = new SupplyOrderDAO();
      return order.getOrdersByVen_Date(ven_id, date);
    }

    @Override
    public List<SupplyOrderDTO> getOrdersByStore_Date(int store_id, Date date) {
      SupplyOrderDAO order = new SupplyOrderDAO();
      return order.getOrdersByStore_Date(store_id, date);
    }

    @Override
    public SupplyOrderDTO getOrderByID(int order_id) {
      SupplyOrderDAO order = new SupplyOrderDAO();
      return order.getOrderByID(order_id);
    }

    @Override
    public void updateDeliveryDate(int order_id) {
      SupplyOrderDAO order = new SupplyOrderDAO();
      order.updateDeliveryDate(order_id);
    }

  /**
   * Helper function to phrase the order info in to Map of item object and its quantity.
   * @param itemInfo in the format of “iteme_name,item_quantity,unit_cost”
   * @return Map of Item Object and its quantity
   */
    private List<ItemInTransaction> processItemList(List<String> itemInfo) {
    List<ItemInTransaction> result = new ArrayList<>();
    String item_name;
    int item_id;
    Double unit_cost;
    int quantity;
    for (String i : itemInfo) {
      String[] i_array = i.split(",");
      item_name = i_array[0];
      item_id = Item.getItem(item_name).getItemId();
      unit_cost = Double.parseDouble(i_array[2]);
      quantity = Integer.parseInt(i_array[1]);
      // TODO: how to handle illegalArgument exception? e.g. can't phrase to double.
      ItemInTransaction item = new ItemInTransaction(item_id, quantity, unit_cost);
      result.add(item);
    }
    return result;
  }


    //Sale related
    @Override
    public int insertSale(int cus_id, String cus_name, int store_id, Date date, List<String> itemInfo) {
      List<ItemInTransaction> items = processItemList(itemInfo);
      SaleDTO saleDTO = new SaleDTO(store_id, cus_id, date);
      SaleDAO sale = new SaleDAO();
      return sale.insertSale(saleDTO, items, cus_name);
    }


    @Override
    public List<ItemInTransaction> getSaleItems(int sale_id) {
        SaleDAO sale = new SaleDAO();
        return sale.getSaleItems(sale_id);
    }

    @Override
    public SaleDTO getSaleByID(int saleID) {
      SaleDAO sale = new SaleDAO();
      return sale.getOrderByID(saleID);
    }

    @Override
    public List<SaleDTO> getSalesByCus_Date(int customer_id, Date date) {
      SaleDAO sale = new SaleDAO();
      return sale.getSalesByCus_Date(customer_id, date);
    }

    @Override
    public List<SaleDTO> getSalesStore_Date(int store_id, Date saleDate) {
      SaleDAO sale = new SaleDAO();
      return sale.getSalesStore_Date(store_id, saleDate);
    }


}
