package IMS.Model;


import java.util.Date;

public class TestConnection {

  private static SQL sql = new SQL();

  public static void main(String[] args) throws Exception {
    sql.authenticate("weihan", "lwh@123456");
    System.out.println("Connected.");
    sql.UpdateDeliveryDate(6);
    SupplyOrder o = new SupplyOrder(2, 2, new Date());
    sql.addNewOrder(o);
    Sale s = new Sale(1, 4, new Date());
    sql.addNewSale(s);
    sql.closeConnection();
  }

}


