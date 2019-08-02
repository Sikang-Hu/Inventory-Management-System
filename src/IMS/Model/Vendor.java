package IMS.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import IMS.IMSException;

public class Vendor {
  private int ven_id;
  private String ven_name;
  private String ven_address;
  private String ven_state;
  private int ven_zip;
  private String ven_description;


  public Vendor(int ven_id, String ven_name, String ven_address, String ven_state,
                int ven_zip, String ven_description) {
    this.ven_id = ven_id;
    this.ven_name = ven_name;
    this.ven_address = ven_address;
    this.ven_state = ven_state;
    this.ven_zip = ven_zip;
    this.ven_description = ven_description;
  }

  public Vendor(String vendorName, String vendorAddress, String vendorState,
                int vendorZip, String vendorDescription) {
    this(-1, vendorName, vendorAddress, vendorState, vendorZip, vendorDescription);
  }


  /**
   * Insert this vendor to database with self incremented vendor id.
   */
  public void insertVendor() {
    try (Connection con = DatabaseUtil.createConnection();
         Statement stmt = con.createStatement()) {
      String insertSQL = "INSERT INTO vendor(ven_name, ven_address, ven_state, " +
              "ven_zip, ven_description) VALUE ('"+ ven_name+ "', '"+ven_address + "', '"+ ven_state +
              "', " + ven_zip + ", '" + ven_description + "')";
      this.ven_id = stmt.executeUpdate(insertSQL, Statement.RETURN_GENERATED_KEYS);
    }catch (SQLException e) {
      e.printStackTrace();
      throw new IMSException(e.getMessage());
    }
  }

  /**
   * Return vendor object using given vendor name
   * @param ven_name vendor name
   * @return vendor object
   */
  public static Vendor getVendor(String ven_name) {
    try (Connection con = DatabaseUtil.createConnection();
         Statement stmt = con.createStatement()) {
      String getSql = "SELECT * FROM vendor WHERE ven_name = '"+ ven_name + "'";
      ResultSet rs = stmt.executeQuery(getSql);
      if (rs.next()) {
        return new Vendor(rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getInt(5), rs.getString(6));
      }
      else {
        throw new IMSException("Vendor: %s doesn't exist", ven_name);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IMSException(e.getMessage());
    }
  }

  /**
   * Retrun vendor name using given vendor id
   * @param ven_id vendor id
   * @return vendor name
   */
  public String getVenName(int ven_id) {
    try (Connection con = DatabaseUtil.createConnection();
         Statement stmt = con.createStatement()) {
      String getSql = "SELECT ven_name FROM vendor WHERE ven_id = " + ven_id;
      ResultSet rs = stmt.executeQuery(getSql);
      if (rs.next()) {
        return rs.getString(1);
      } else {
        throw new IMSException("Invalid vendor id: " + ven_id);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IMSException(e.getMessage());
    }
  }



  @Override
  public String toString() {
    return "Vendor{" +
            "vendorID=" + ven_id +
            ", vendorName='" + ven_name + '\'' +
            ", vendorAddress='" + ven_address + '\'' +
            ", vendorState=" + ven_state +
            ", vendorZip='" + ven_zip + '\'' +
            ", vendorDescription='" + ven_description + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Vendor) {
      Vendor otherV = (Vendor)other;
      return otherV.getVendorID() == this.getVendorID() ||
              (otherV.getVendorName() == ven_name && otherV.getVendorAddress() == ven_address
                      && otherV.getVendorState() == ven_state && otherV.getVendorZip() == ven_zip
                      && otherV.getVendorDescription() == ven_description);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(ven_id, ven_name, ven_address, ven_state,ven_zip,ven_description);
  }


  public int getVendorID() {
    return ven_id;
  }

  public void setVendorID(int vendorID) {
    this.ven_id = vendorID;
  }

  public String getVendorName() {
    return ven_name;
  }

  public void setVendorName(String vendorName) {
    this.ven_name = vendorName;
  }

  public String getVendorAddress() {
    return ven_address;
  }

  public void setVendorAddress(String vendorAddress) {
    this.ven_address = vendorAddress;
  }

  public String getVendorState() {
    return ven_state;
  }

  public void setVendorState(String vendorState) {
    this.ven_state = vendorState;
  }

  public int getVendorZip() {
    return ven_zip;
  }

  public void setVendorZip(int ven_zip) {
    this.ven_zip = ven_zip;
  }

  public String getVendorDescription() {
    return ven_description;
  }

  public void setVendorDescription(String vendorDescription) {
    this.ven_description = vendorDescription;
  }
}