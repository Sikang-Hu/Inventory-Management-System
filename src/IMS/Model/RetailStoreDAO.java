package IMS.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import IMS.IMSException;

public class RetailStoreDAO {

  public int insertStore(RetailStoreDTO store) {
    String sql = "INSERT INTO retail_store (store_address, store_state, store_zip) VALUES ('"
            + store.getStoreAddress()+"', '"+store.getStoreState()+"', " + store.getStoreZip() + ")";
    return new DatabaseUtil().insertOneRecord(sql);
  }


  public RetailStoreDTO getStoreByID(int store_id) {
    String sql = "SELECT * FROM retail_store WHERE store_id = "+ store_id + "";
    return getStore(sql);
  }


  public List<RetailStoreDTO> getAllStore() {
    String sql = "SELECT * FROM retail_store";
    try (Connection con = DatabaseUtil.createConnection();
         Statement stmt = con.createStatement()) {
      List<RetailStoreDTO> result = new ArrayList<>();
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        result.add(new RetailStoreDTO(rs.getInt(1), rs.getString(2),
                rs.getString(3), rs.getInt(4)));
      }
      return result;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IMSException(e.getMessage());
    }
  }

  private RetailStoreDTO getStore(String sql) {
    try (Connection con = DatabaseUtil.createConnection();
         Statement stmt = con.createStatement()) {

      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return new RetailStoreDTO(rs.getInt(1), rs.getString(2),
                rs.getString(3), rs.getInt(4));
      }
      else {
        throw new IMSException("Store_id doesn't exist");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IMSException(e.getMessage());
    }
  }





}