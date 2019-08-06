package IMS.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import IMS.IMSException;

public class InventoryAge {

    HashMap<Integer, inventoryAgeObject> map = new HashMap<>();

    class inventoryAgeObject {
        private int itemID;
        private int SKUID;
        private String name;
        private int age;


        inventoryAgeObject(int itemID, int SKUID, String name, int age) {
        this.itemID = itemID;
        this.SKUID = SKUID;
        this.name = name;
        this.age = age;

    }


    int getAge(int age) {
        return this.age;
    }

    @Override
    public String toString() {
        return "itemID: " + this.itemID +
                " SKUID: " + this.SKUID +
                " name: " + this.name +
                " age: " + this.age;
    }

}


    void getAge() {
        String sql =  "CALL get_remaining_inventory_by_age_of_inv(null, null);";
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                this.map.put(rs.getInt(1),
                        new inventoryAgeObject(rs.getInt(4),
                                rs.getInt(1),rs.getString(5),rs.getInt(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }

        for (Map.Entry<Integer, inventoryAgeObject> entry : this.map.entrySet()) {
            System.out.println(entry.getValue());
        }
    }


    public static void main(String[] args) {
        new InventoryAge().getAge();
    }



}
