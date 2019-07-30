package IMS.Model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestConnection {

  private static DatabaseSQL sql = new DatabaseSQL();

  public static void main(String[] args) throws Exception {
    sql.authenticate("weihan", "lwh@123456");
    List<String> itemInfo = new ArrayList<>();
    itemInfo.add("apple,50,0.4");
    itemInfo.add("dragon fruit,1,1.0");
    sql.addNewOrder("Ward, Shields and Oberbrunner","Thanos Street",
            "2019-07-19", itemInfo);
    sql.closeConnection();
  }

}


