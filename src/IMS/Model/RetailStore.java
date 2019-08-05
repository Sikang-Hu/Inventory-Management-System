package IMS.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import IMS.IMSException;

public class RetailStore {


  private int storeId;
  private String storeAddress;
  private String StoreState;
  private int storeZip;


  public RetailStore(int storeId, String storeAddress, String StoreState, int storeZip) {
    this.storeId = storeId;
    this.storeAddress = storeAddress;
    this.StoreState = StoreState;
    this.storeZip = storeZip;
  }

  public RetailStore(String storeAddress, String StoreState, int storeZip) {
    this.storeId = -1;
    this.storeAddress = storeAddress;
    this.StoreState = StoreState;
    this.storeZip = storeZip;
  }

  public void insertStore() {
    try (Connection con = DatabaseUtil.createConnection();
         Statement stmt = con.createStatement()) {
      String sql = "INSERT INTO retail_store (store_address, store_state, store_zip) VALUES ('"
              + this.storeAddress+"', '"+this.StoreState+"', " + this.storeZip + ")";
      stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
      this.storeId = DatabaseUtil.getGeneratedId(stmt);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IMSException(e.getMessage());
    }
  }


  public static RetailStore getStore(int store_id) {
    try (Connection con = DatabaseUtil.createConnection();
         Statement stmt = con.createStatement()) {
      String getSql = "SELECT * FROM retail_store WHERE store_id = '"+ store_id + "'";
      ResultSet rs = stmt.executeQuery(getSql);
      if (rs.next()) {
        return new RetailStore(rs.getInt(1), rs.getString(2),
                rs.getString(3), rs.getInt(4));
      }
      else {
        throw new IMSException("Store_id: %d doesn't exist", store_id);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IMSException(e.getMessage());
    }
  }


  @Override
  public String toString() {
    return "Doctor{" +
            "storeId=" + storeId +
            ", storeAddress='" + storeAddress + '\'' +
            ", StoreState='" + StoreState + '\'' +
            ", storeZip=" + storeZip +
            '}';
  }




  public int getStoreId() {
    return this.storeId;
  }

  private void setStoreId(int storeId) {
    this.storeId = storeId;
  }


  public String getStoreAddress() {
    return storeAddress;
  }

  public void setStoreAddress(String storeAddress) {
    this.storeAddress = storeAddress;
  }

  public String getStoreState() {
    return StoreState;
  }

  public void setStoreState(String StoreState) {
    this.StoreState = StoreState;
  }

  public int getStoreZip() {
    return storeZip;
  }

  public void setStoreZip(int storeZip) {
    this.storeZip = storeZip;
  }


}