package IMS.Controller;

import IMS.Model.InventoryModel;

import java.io.IOException;

public interface Controller {

    void insertItem(String... item);

    void queryItem(String... name) throws IOException;

    void commandUserInterface();


}
