package IMS.Model;


import IMS.IMSException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// TODO: Is there any usage of this class?
public class CategoryDAO {
    public int insertCat(CategoryDTO cat) {
        String sql = "INSERT INTO item_category (cat_name, cat_description) VALUES ('"
                + cat.getCategoryName() + "', '" + cat.getCategoryDescription() + "')";
        return DatabaseUtil.insertOneRecord(sql);
    }

    public CategoryDTO getCatByName(String name)  {
        String sql = String.format("SELECT cat_id FROM item_category WHERE cat_name = \'%s\';", name);
        return getCat(sql);
    }

    public CategoryDTO getCatByID(int catId) {
        String sql = String.format("SELECT cat_id FROM item_category WHERE cat_id = %d", catId);
        return getCat(sql);
    }

    private CategoryDTO getCat(String sql) {
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return new CategoryDTO(rs.getInt(1), rs.getString(2), rs.getString(3));
            } else {
                throw new IMSException("Category_doesn't exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }
}