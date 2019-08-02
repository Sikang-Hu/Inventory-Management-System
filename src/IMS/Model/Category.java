package IMS.Model;


import IMS.IMSException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// TODO: Is there any usage of this class?
public class Category {
  private int categoryID;
  private String categoryName;
  private String categoryDescription;

  public Category(int categoryID, String categoryName, String categoryDescription) {
    this.categoryID = categoryID;
    this.categoryName = categoryName;
    this.categoryDescription = categoryDescription;
  }

  public Category(String categoryName, String categoryDescription) {
    this(-1, categoryName, categoryDescription);
  }


  @Override
  public String toString() {
    return "category{" +
            "categoryID=" + categoryID +
            ", categoryName='" + categoryName + '\'' +
            ", categoryDescription='" + categoryDescription + '\'' +
            '}';
  }

  public int getCategoryID() {
    return categoryID;
  }

  public void setCategoryID(int categoryID) {
    this.categoryID = categoryID;
  }

  public String getCategoryName() {
    return categoryName;
  }

  int insertCat() {
    try (Connection con = DatabaseUtil.createConnection();
         Statement stmt = con.createStatement()) {
      this.categoryID = stmt.executeUpdate(
              "INSERT INTO item_category (cat_name, cat_description) VALUES (\'"
                      + this.categoryName + "\', \'"
                      + this.categoryDescription + "\');", Statement.RETURN_GENERATED_KEYS);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IMSException(e.getMessage());
    }
    return this.categoryID;
  }

  static int getCatId(Statement stmt, String name) throws SQLException {
    String sql = String.format("SELECT cat_id FROM item_category WHERE cat_name = \'%s\';", name);
    ResultSet rs = stmt.executeQuery(sql);
    if (rs.next()) {
      return rs.getInt(1);
    } else {
      throw new IMSException("Category %s doesn't exist", name);
    }
  }

  static String getCatName(Statement stmt, int catId) throws SQLException{
    String sql = String.format("SELECT cat_name FROM item_category WHERE cat_id = %d;", catId);
    ResultSet rs = stmt.executeQuery(sql);
    if (rs.next()) {
      return rs.getString(1);
    }
    throw new IMSException("Invalid Category ID: %d", catId);
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getCategoryDescription() {
    return categoryDescription;
  }

  public void setCategoryDescription(String categoryDescription) {
    this.categoryDescription = categoryDescription;
  }
}