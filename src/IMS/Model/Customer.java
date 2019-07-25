package IMS.Model;

public class Customer {

    private int customerID;
    private String name;


    public Customer(String name) {
      this.customerID = -1;
      this.name = name;
    }

    public Customer(int customerID, String name) {
      this.customerID = customerID;
      this.name = name;
    }

    @Override
    public String toString() {
      return "customer{" +
              "customerID=" + customerID +
              ", name='" + name + '\'' +
              '}';
    }





  public int getCustomerID() {
    return customerID;
  }

  public void setDoctorID(int customerID) {
    this.customerID = customerID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


}
