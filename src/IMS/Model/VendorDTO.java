package IMS.Model;

import java.util.HashSet;

public class VendorDTO {
  private int vendorID;
  private String vendorName;
  private String vendorAddress;
  private String vendorState;
  private int vendorZip;
  private String vendorDescription;



  VendorDTO(int vendorID, String vendorName, String vendorAddress, String vendorState,
         int vendorZip, String vendorDescription) {
    this.vendorID = vendorID;
    this.vendorName = vendorName;
    this.vendorAddress = vendorAddress;
    this.vendorState = vendorState;
    this.vendorZip = vendorZip;
    this.vendorDescription = vendorDescription;
  }

  VendorDTO(String vendorName, String vendorAddress, String vendorState,
         int vendorZip, String vendorDescription) {
    this(-1, vendorName, vendorAddress, vendorState, vendorZip, vendorDescription);
  }

  VendorDTO(String vendorName, String vendorAddress, String vendorState,
         int vendorZip) {
    this(-1, vendorName, vendorAddress, vendorState, vendorZip, null);
  }

  @Override
  public String toString() {
    return "Vendor{" +
            "vendorID=" + vendorID +
            ", vendorName='" + vendorName + '\'' +
            ", vendorAddress='" + vendorAddress + '\'' +
            ", vendorState=" + vendorState +
            ", vendorZip='" + vendorZip + '\'' +
            ", vendorDescription='" + vendorDescription + '\'' +
            '}';
  }


  public int getVendorID() {
    return vendorID;
  }


  public String getVendorName() {
    return vendorName;
  }

  public void setVendorName(String vendorName) {
    this.vendorName = vendorName;
  }

  public String getVendorAddress() {
    return vendorAddress;
  }

  public void setVendorAddress(String vendorAddress) {
    this.vendorAddress = vendorAddress;
  }

  public String getVendorState() {
    return vendorState;
  }

  public void setVendorState(String vendorState) {
    this.vendorState = vendorState;
  }

  public int getVendorZip() {
    return vendorZip;
  }

  public void setVendorZip(int vendorID) {
    this.vendorID = vendorID;
  }

  public String getVendorDescription() {
    return vendorDescription;
  }

  public void setVendorDescription(String vendorDescription) {
    this.vendorDescription = vendorDescription;
  }

}
