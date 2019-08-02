package IMS.Controller;



import java.io.IOException;

public interface Controller {

    void insertItem(String... item);

    void queryItem(String... name) throws IOException;

    void commandUserInterface();


}
