package IMS.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import IMS.IMSException;

public class CustomerDAO {
  public String getName(Integer cus_id) {
    if (cus_id == null) {
      return null;
    }
    try (Connection con = DatabaseUtil.createConnection();
         Statement stmt = con.createStatement()) {
      String sql = "SELECT * FROM customer WHERE cus_id =" + cus_id;
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return rs.getString(2);
      } else {
        throw new IMSException("CustomerId: %d doesn't exist", cus_id);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IMSException(e.getMessage());
    }
  }
}
