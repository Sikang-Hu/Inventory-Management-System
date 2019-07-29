package IMS;

import IMS.Controller.Controller;
import IMS.Controller.ControllerImpl;
import IMS.Model.DummyModel;
import IMS.Model.InventoryModel;

import java.io.*;

/**
 * This is the entrance of the whole project.
 */
public class Main {
  public static void main(String... args) {
    InventoryModel model = new DummyModel();
    Controller c = new ControllerImpl(model);
    c.commandUserInterface();

  }
}
