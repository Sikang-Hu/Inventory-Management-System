package IMS.Model;

import java.util.Objects;

public class Customer {

  private int customerID;
  private String customerName;


  public Customer(String name) {
    this.customerID = -1;
    this.customerName = name;
  }

  public Customer(int customerID, String name) {
    this.customerID = customerID;
    this.customerName = name;
  }

  @Override
  public String toString() {
    return "customer{" +
            "customerID=" + customerID +
            ", name='" + customerName + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Customer)) {
      return false;
    }
    Customer otherCus = (Customer)other;
    if (this.customerName == otherCus.getName()) {
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerName);
  }


  public int getCustomerID() {
    return customerID;
  }

  public void setCustomerID(int customerID) {
    this.customerID = customerID;
  }

  public String getName() {
    return customerName;
  }

  public void setName(String name) {
    this.customerName = name;
  }




}