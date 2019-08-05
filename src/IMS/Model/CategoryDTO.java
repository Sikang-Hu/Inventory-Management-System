package IMS.Model;

public class CategoryDTO {
  private int categoryID;
  private String categoryName;
  private String categoryDescription;

  public CategoryDTO(int categoryID, String categoryName, String categoryDescription) {
    this.categoryID = categoryID;
    this.categoryName = categoryName;
    this.categoryDescription = categoryDescription;
  }

  public CategoryDTO(String categoryName, String categoryDescription) {
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

  public String getCategoryDescription() {
    return categoryDescription;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryDescription(String categoryDescription) {
    this.categoryDescription = categoryDescription;
  }

  public void setCategoryID(int categoryID) {
    this.categoryID = categoryID;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }
}
