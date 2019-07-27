package IMS.Model;

import org.omg.CORBA.DATA_CONVERSION;

import java.util.Date;

public class SupplyOrder {
  private int orderID;
  private int vendorID;
  private int storeID;
  private Date orderDate;
  private Date deliveryDate;

  public SupplyOrder(int orderID, int vendorID, int storeID, Date orderDate, Date deliveryDate) {
    this.orderID = orderID;
    this.vendorID = vendorID;
    this.storeID = storeID;
    this.orderDate = orderDate;
    this.deliveryDate = deliveryDate;

  }

  @Override
  public String toString() {
    return "SupplyOrder{" +
            "orderID=" + orderID +
            ", vendorID='" + vendorID + '\'' +
            ", storeID='" + storeID + '\'' +
            ", orderDate=" + orderDate +
            ", deliveryDate='" + deliveryDate + '\'' +
            '}';

  }

  public int getOrderID() {
    return orderID;
  }

  public void setOrderID(int orderID) {
    this.orderID = orderID;
  }

  public int getVendorID() {
    return vendorID;
  }

  public void setVendorID(int vendorID) {
    this.vendorID = vendorID;
  }

  public int getStoreID() {
    return storeID;
  }

  public void setStoreID(int storeID) {
    this.storeID = storeID;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public Date getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(Date deliveryDate) {
    this.deliveryDate = deliveryDate;
  }
}
