package IMS.Model;

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


  public int getCustomerID() {
    return customerID;
  }

  public void setDoctorID(int customerID) {
    this.customerID = customerID;
  }

  public String getName() {
    return customerName;
  }

  public void setName(String name) {
    this.customerName = name;
  }


}
