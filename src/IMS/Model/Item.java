package IMS.Model;

import IMS.IMSException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * Created by Sikang on 2019-07-25.
 */
public class Item {
    private int itemId;
    private String categoryName;
    private String itemName;
    private double itemPrice;

    // TODO: access can be package private
    Item(int itemId, String categoryName, String itemName, double itemPrice) {
        this.itemId = itemId;
        this.categoryName = categoryName;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    Item(String categoryName, String itemName, double itemPrice) {
        this(-1, categoryName, itemName, itemPrice);
    }

    // TODO: Use lambda to abstract
    void insertItem() {
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            this.itemId = stmt.executeUpdate(this.insertStmt(Category.getCatId(stmt, this.categoryName))
                    , Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }

    static Item getItem(String name) {
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            String sql = String.format("SELECT * FROM item WHERE item_name = \'%s\';", name);
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                double price = rs.getDouble(4);
                return new Item(rs.getInt(1), Category.getCatName(stmt, rs.getInt(2))
                        , name, price);
            } else {
                throw new IMSException("Item: %s doesn't exist", name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }

    private String insertStmt(int catId) {
        return String.format("INSERT INTO item (cat_id, item_name, item_unit_price) VALUES (%d, \'%s\', %.2f)"
                , catId, this.itemName, this.itemPrice);
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
        if (o instanceof Item) {
            Item item = (Item) o;
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

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }
}
