package IMS.Model;

import IMS.IMSException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtil {

//  private static String url = "jdbc:mysql://database-1.c6ltym5semvf.us-east-2.rds.amazonaws.com/ims";
//  private static String user = "admin";
//  private static String password = "cs5200proj";

    private static String url = "jdbc:mysql://localhost:3306/ims_sku?serverTimezone=EST5EDT";
    private static String user = "weihan";
    private static String password = "lwh@123456";


    public static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new IMSException(e.getMessage());
        }
    }

    static void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new IMSException(e.getMessage());
        }
    }

    static void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            throw new IMSException(e.getMessage());
        }
    }

    static void rollback(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new IMSException(e.getMessage());
        }
    }

    static int insertOneRecord (String insertSQL)
    {
        // System.out.println("INSERT STATEMENT: "+insertSQL);
        int key = -1;
        try (Connection con = createConnection()) {

            // get connection and initialize statement

            Statement stmt = con.createStatement();
            stmt.executeUpdate(insertSQL, Statement.RETURN_GENERATED_KEYS);

            // extract auto-incremented ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) key = rs.getInt(1);

            // Cleanup
            stmt.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Could not insert record: "+insertSQL);
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return key;
    }

    static int getGeneratedId(Statement stmt) throws SQLException {
        ResultSet rs = stmt.getGeneratedKeys();
        return rs.next() ? rs.getInt(1) : -1;
    }


}




