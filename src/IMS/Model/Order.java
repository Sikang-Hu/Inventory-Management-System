package IMS.Model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sikang on 2019-07-25.
 */
class Order {
  private File data = new File("./order.dbt");
  String supplier;
  List<Item> items = new ArrayList<>();

  protected void write() {
    try(BufferedWriter wt = new BufferedWriter(new FileWriter(data, true))) {
      wt.append(this.supplier).append(",");
    } catch (IOException e) {
      // do nothing
    }
  }

  protected void write(Appendable ap) {

  }
}
