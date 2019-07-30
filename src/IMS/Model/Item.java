package IMS.Model;

public class Item {
  private int itemId;
  private int categoryID;
  private String itemName;
  private double itemPrice;

  public Item(int itemId, int categoryID, String itemName, double itemPrice) {
    this.itemId = itemId;
    this.categoryID = categoryID;
    this.itemName = itemName;
    this.itemPrice = itemPrice;
  }

  public Item(int categoryID, String itemName, double itemPrice) {
    this.itemId = -1;
    this.categoryID = categoryID;
    this.itemName = itemName;
    this.itemPrice = itemPrice;
  }

  @Override
  public String toString() {
    return "IMS.Model.Item{" +
            "itemId=" + itemId +
            ", categoryID='" + categoryID + '\'' +
            ", itemName='" + itemName + '\'' +
            ", itemPrice=" + itemPrice +
            '}';
  }


  public int getItemId() {
    return itemId;
  }

  public void setItemId(int itemId) {
    this.itemId = itemId;
  }

  public int getCategoryID() {
    return categoryID;
  }

  public void setCategoryID(int categoryID) {
    this.categoryID = categoryID;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public double getItemPrice() {
    return itemPrice;
  }

  public void setItemPrice(int itemPrice) {
    this.itemPrice = itemPrice;
  }
}