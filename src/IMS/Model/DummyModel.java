package IMS.Model;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DummyModel implements InventoryModel {

  private File data = new File("./order.dbt");

  @Override
  public void insertOrder(String o) {
    Order order = new Order(new File(o));
    try(BufferedWriter wt = new BufferedWriter(new FileWriter(data, true))) {
      order.write(wt);
      wt.append("\n");
    } catch (IOException e) {
      // do nothing
    }
  }

}
