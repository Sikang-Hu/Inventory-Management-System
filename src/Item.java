public class Item {
  private int itemId;
  private int categoryID;
  private String itemName;
  private int itemPrice;

  public Item(int itemId, int categoryID, String itemName, int itemPrice){
    this.itemId = itemId;
    this.categoryID = categoryID;
    this.itemName = itemName;
    this.itemPrice = itemPrice;
  }

  @Override
  public String toString() {
    return "Item{" +
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

  public int getItemPrice(){
    return itemPrice;
  }
  public void setItemPrice(int itemPrice){
    this.itemPrice = itemPrice;
  }
}
