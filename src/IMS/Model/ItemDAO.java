package IMS.Model;

import IMS.IMSException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by Sikang on 2019-07-25.
 */
public class ItemDAO {

    // TODO: Use lambda to abstract
    int insertItem(ItemDTO item) {
        int catID = new CategoryDAO().getCatByName(item.getItemName()).getCategoryID();
        String sql = this.insertStmt(catID, item.getItemName(),item.getItemPrice());
        return new DatabaseUtil().insertOneRecord(sql);
    }

    ItemDTO getItemByName(String name) {
        String sql = String.format("SELECT * FROM item WHERE item_name = \'%s\';", name);
        return getItem(sql);
    }

    ItemDTO getItemByID(int id) {
        String sql = String.format("SELECT * FROM item WHERE item_id = \'%d\';", id);
        return getItem(sql);
    }

    private String insertStmt(int catId, String itemName, double itemPrice) {
        return String.format("INSERT INTO item (cat_id, item_name, item_unit_price) VALUES (%d, \'%s\', %.2f)"
                , catId, itemName, itemPrice);
    }

    private ItemDTO getItem(String sql) {
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return new ItemDTO(rs.getInt(1),
                        new CategoryDAO().getCatByID(rs.getInt(2)).getCategoryName(),
                        rs.getString(3), rs.getDouble(4));
            } else {
                throw new IMSException("Item doesn't exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }
}

