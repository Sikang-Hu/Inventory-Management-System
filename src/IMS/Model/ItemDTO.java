package IMS.Model;

import java.util.Objects;

public class ItemDTO {

  private int itemId;
  private String categoryName;
  private String itemName;
  private double itemPrice;
  private int reorderPoint;

  // TODO: access can be package private
  public ItemDTO(int itemId, String categoryName, String itemName, double itemPrice) {
    this.itemId = itemId;
    this.categoryName = categoryName;
    this.itemName = itemName;
    this.itemPrice = itemPrice;
  }

  public ItemDTO(String categoryName, String itemName, double itemPrice) {
    this(-1, categoryName, itemName, itemPrice);
  }

  @Override
  public String toString() {
    return "IMS.Model.Item{" +
            "itemId=" + itemId +
            ", categoryID='" + categoryName + '\'' +
            ", itemName='" + itemName + '\'' +
            ", itemPrice=" + itemPrice +
            '}';
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.itemId, this.categoryName, this.itemName, this.itemPrice);
  }

  // TODO: Definition of Item should be more clear
  @Override
  public boolean equals(Object o) {
    if (o instanceof ItemDTO) {
      ItemDTO item = (ItemDTO) o;
      return item.getItemId() == this.itemId || (item.getCategoryName().equals(this.categoryName)
              && item.getItemName().equals(this.itemName)
              && (Math.abs(item.getItemPrice() - this.itemPrice) < 0.01));
    }
    return false;
  }


  public int getItemId() {
    return itemId;
  }

  public void setItemId(int itemId) {
    this.itemId = itemId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
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

  public void setItemPrice(double itemPrice) {this.itemPrice = itemPrice;}

  public void setReorderPonit(int reorderPoint) {this.reorderPoint = reorderPoint;
  }
}
