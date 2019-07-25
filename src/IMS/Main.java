package IMS;

import IMS.Model.DummyModel;
import IMS.Model.InventoryModel;
import IMS.Model.Order;

/**
 * This is the entrance of the whole project.
 */
public class Main {
  public static void main(String... args) {
    InventoryModel model = new DummyModel();
    model.insertOrder("./o2.odr");
  }
}
