package IMS.Model;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;

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
    public CategoryDTO getCatByID(int cat_id) {
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
    public void addSoldItem(String vendor, Iterable<ItemDTO> collection) {
      new VendorDAO().addSoldItem(vendor, collection);
    }


    //status
    @Override
    public List<Status> getInvStatus(Integer store_id, String item_name, String cat_name) {
        try (Connection con = DatabaseUtil.createConnection();
             CallableStatement stmt = con.prepareCall("{CALL INV_STATUS(?, ?, ?)}")) {
          if (store_id == null) {
            stmt.setNull(1, Types.INTEGER);
          }
          else {
            stmt.setInt(1, store_id);
          }
          if (item_name == null) {
            stmt.setNull(2, Types.INTEGER);
          }
          else {
            ItemDTO item = new ItemDAO().getItemByName(item_name);
            int item_id = item.getItemId();
            stmt.setInt(2, item_id);
          }
          if (cat_name == null) {
            stmt.setNull(3, Types.INTEGER);
          }
          else {
            int cat_id = new CategoryDAO().getCatByName(cat_name).getCategoryID();
            stmt.setInt(3, cat_id);
          }


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



    //store
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
    public List<RetailStoreDTO> getAllStore() {
      return new RetailStoreDAO().getAllStore();
    }

    //order related
    @Override
    public int insertOrder(String ven_name, int store_id, Date date, Iterable<ItemInTransaction> items) {
        int ven_id = new VendorDAO().getVendorByName(ven_name).getVendorID();
        SupplyOrderDTO order = new SupplyOrderDTO(ven_id, store_id, date);
        return new SupplyOrderDAO().insertSupplyOrder(order, items);
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


    //Sale related
    @Override
    public int insertSale(Integer cus_id, int store_id, Date date, Iterable<ItemInTransaction> items) {
      String cus_name = new CustomerDAO().getName(cus_id);
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

    @Override
    public void returnSale(int sale_id, String item_name, int quantity) {
      int item_id = new ItemDAO().getItemByName(item_name).getItemId();
      new SaleDAO().returnSale(sale_id, item_id, quantity);
    }

}
