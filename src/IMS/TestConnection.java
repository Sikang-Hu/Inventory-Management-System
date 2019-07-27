package IMS;

public class TestConnection {
  public static void main(String[] args) throws Exception {
    Class.forName("com.mysql.jdbc.Driver");
    IMSUtil imsu = new IMSUtil("jdbc:mysql://localhost:3306/ims?serverTimezone=EST5EDT"
            , "weihan", "lwh123456");
  }

}
