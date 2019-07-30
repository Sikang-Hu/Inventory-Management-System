package IMS.Model;

/**
 * Created by Sikang on 2019-07-25.
 */
public class Item {
    private int itemId;
    private String categoryName;
    private String itemName;
    private double itemPrice;

    // TODO: access can be package private
    public Item(int itemId, String categoryName, String itemName, double itemPrice) {
        this.itemId = itemId;
        this.categoryName = categoryName;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public Item(String categoryName, String itemName, double itemPrice) {
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

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }
}
