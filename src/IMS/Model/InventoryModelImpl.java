package IMS.Model;

import IMS.IMSException;

import java.sql.*;

import java.util.*;
import java.util.Date;

public class InventoryModelImpl implements InventoryModel {



  //Category related
    @Override
    public void insertCategory(String categoryName, String categoryDescription) {
      CategoryDTO cat = new CategoryDTO(categoryName, categoryDescription);
      new CategoryDAO().insertCat(cat);
    }

    @Override
    public CategoryDTO getCatByName(String cat_name) {
      return new CategoryDAO().getCatByName(cat_name);
    }

    @Override
    public CategoryDTO getCatByName(int cat_id) {
      return new CategoryDAO().getCatByID(cat_id);
    }

    //item related
    @Override
    public void insertItem(String category, String name, double unitPrice) {
        ItemDTO item = new ItemDTO(category, name, unitPrice);
        new ItemDAO().insertItem(item);
    }

    @Override
    public ItemDTO getItemByName(String name) {
        return new ItemDAO().getItemByName(name);
    }

    @Override
    public ItemDTO getItemByID(int id) {
      return new ItemDAO().getItemByID(id);
    }


    //Vendor related
    @Override
    public void insertVendor(String name, String address, String state, int zip, String description) {
        VendorDTO v = new VendorDTO(name, address, state, zip, description);
        new VendorDAO().insertVendor(v);
    }

    @Override
    public VendorDTO getVendorByName(String name) {
      return new VendorDAO().getVendorByName(name);
    }

    @Override
    public VendorDTO getVendorByID(int id) {
      return new VendorDAO().getVendorByID(id);
    }

    @Override
    public Set<ItemDTO> getSoldItems(String name) {
        VendorDAO vendorDAT = new VendorDAO();
        VendorDTO v = vendorDAT.getVendorByName(name);
        return vendorDAT.getSoldItem(v);
    }

    @Override
    public void addSoldItem(String vendorName, List<String> itemInfo) {
      Set<ItemDTO> items = processItemForVendor(itemInfo);
      new VendorDAO().addSoldItem(vendorName, items);
    }

    public void addSoldItem(String vendor, Iterable<ItemDTO> collection) {
      // May need it
    }

    /**
     * Phrase itemInfo and return a list of ItemDAO.
     * @param itemInfo format of "cat_id,item_name,item_unit_price"
     * @return a list of ItemDAO
     */
    private Set<ItemDTO> processItemForVendor(List<String> itemInfo) {
      Set<ItemDTO> result = new HashSet<>();
      int cat_id;
      String cat_name;
      String item_name;
      Double unit_cost;
      for (String i : itemInfo) {
        String[] i_array = i.split(",");
        cat_id = Integer.parseInt(i_array[0]);
        cat_name = new CategoryDAO().getCatByID(cat_id).getCategoryName();
        item_name = i_array[1];
        unit_cost = Double.parseDouble(i_array[2]);
        // TODO: how to handle illegalArgument exception? e.g. can't phrase to double.
        ItemDTO item = new ItemDTO(cat_name, item_name, unit_cost);
        result.add(item);
      }
      return result;
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
      RetailStoreDTO store = new RetailStoreDTO(address, state, zipCode);
      new RetailStoreDAO().insertStore(store);
    }

    @Override
    public RetailStoreDTO getStoresByID(int storeID) {
        return new RetailStoreDAO().getStoreByID(storeID);
    }

    @Override
    public RetailStoreDTO getStoresByName(String name) {
      return new RetailStoreDAO().getStoreByName(name);
    }

    //order related
    @Override
    public int insertOrder(String ven_name, int store_id, Date date, List<String> itemInfo) {
      int ven_id = new VendorDAO().getVendorByName(ven_name).getVendorID();
      List<ItemInTransaction> items= processItemInTransaction(itemInfo);
      SupplyOrderDTO orderDTO = new SupplyOrderDTO(ven_id,store_id,date);
      SupplyOrderDAO order = new SupplyOrderDAO();
      return order.insertSupplyOrder(orderDTO, items);
    }

  @Override
  public int insertOrder(String ven_name, int store_id, Date date, Iterable<ItemInTransaction> itemInfo) {
      return 0;
  }

    @Override
    public List<ItemInTransaction> getOrderItems(int order_id) {
      SupplyOrderDAO order = new SupplyOrderDAO();
      return order.getOrderItems(order_id);
    }


    @Override
    public List<SupplyOrderDTO> getOrdersByVen_Date(String ven_name, Date date) {
      int ven_id = new VendorDAO().getVendorByName(ven_name).getVendorID();
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
    private List<ItemInTransaction> processItemInTransaction(List<String> itemInfo) {
      List<ItemInTransaction> result = new ArrayList<>();
      String item_name;
      int item_id;
      Double unit_cost;
      int quantity;
      for (String i : itemInfo) {
        String[] i_array = i.split(",");
        item_name = i_array[0];
        item_id = new ItemDAO().getItemByName(item_name).getItemId();
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
    public int insertSale(Integer cus_id, String cus_name, int store_id, Date date, List<String> itemInfo) {
      List<ItemInTransaction> items = processItemInTransaction(itemInfo);
      SaleDTO saleDTO = new SaleDTO(store_id, cus_id, date);
      SaleDAO sale = new SaleDAO();
      return sale.insertSale(saleDTO, items, cus_name);
    }

  @Override
  public int insertSale(Integer cus_id, int store_id, Date date, List<ItemInTransaction> itemInfo) {
    return 0;
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
