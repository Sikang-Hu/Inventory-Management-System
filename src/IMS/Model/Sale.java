package IMS.Model;

import java.util.Date;

public class Sale {
  private int saleID;
  private int storeID;
  private int customerID;
  private Date saleDate;

  public Sale(int storeID, int customerID, Date saleDate) {
    this.saleID = -1;
    this.storeID = storeID;
    this.customerID = customerID;
    this.saleDate = saleDate;
  }

  @Override
  public String toString() {
    return "Sale{" +
            "saleID=" + saleID +
            ", storeID='" + storeID + '\'' +
            ", customerID='" + customerID + '\'' +
            ", saleDate=" + saleDate +
            '}';
  }


  public int getSaleID() {
    return saleID;
  }

  public void setSaleID(int saleID) {
    this.saleID = saleID;
  }

  public int getStoreID() {
    return storeID;
  }

  public void setStoreID(int storeID) {
    this.storeID = storeID;
  }

  public int getCustomerID() {
    return customerID;
  }

  public void setCustomerID(int customerID) {
    this.customerID = customerID;
  }

  public Date getSaleDate() {
    return saleDate;
  }

  public void setSaleDate(Date saleDate) {
    this.saleDate = saleDate;
  }

}