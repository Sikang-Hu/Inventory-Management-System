package IMS.Model;

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
    this.categoryID = -1;
    this.categoryName = categoryName;
    this.categoryDescription = categoryDescription;
  }

  @Override
  public String toString() {
    return "category{" +
            "categoryID=" + categoryID +
            ", categoryName='" + categoryName + '\'' +
            ", categoryDescription='" + categoryDescription + '\'' +
            '}';
  }

  public int getcategoryID() {
    return categoryID;
  }

  public void setcategoryID(int categoryID) {
    this.categoryID = categoryID;
  }

  public String getCategoryName() {
    return categoryName;
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