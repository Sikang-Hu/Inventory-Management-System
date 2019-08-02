package IMS;

import IMS.Controller.Controller;
import IMS.Controller.ControllerImpl;
import IMS.Model.DatabaseUtil;
import IMS.Model.InventoryModel;
import IMS.Model.InventoryModelImpl;

import java.io.*;
import java.sql.SQLException;

/**
 * This is the entrance of the whole project.
 */
public class Main {
  public static void main(String... args) {
    InventoryModel model = new InventoryModelImpl();
    Controller c = new ControllerImpl(model);
    c.commandUserInterface();

  }
}
