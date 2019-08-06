package IMS.Model;

import IMS.IMSException;

import java.sql.*;


public class DatabaseUtil {

//  private static String url = "jdbc:mysql://database-1.c6ltym5semvf.us-east-2.rds.amazonaws.com/ims_SKU";
//  private static String user = "admin";
//  private static String password = "cs5200proj";

    private static String url = "jdbc:mysql://localhost:3306/ims_sku?serverTimezone=EST5EDT";
    private static String user = "root";
    private static String password = "Y123456h";


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

    static int insertOneRecord (String insertSQL) {
        try (Connection con = DatabaseUtil.createConnection();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(insertSQL, Statement.RETURN_GENERATED_KEYS);
        return DatabaseUtil.getGeneratedId(stmt);
        } catch (SQLException e) {
        e.printStackTrace();
        throw new IMSException(e.getMessage());
        }
    }

    static int getGeneratedId(Statement stmt) throws SQLException {
        ResultSet rs = stmt.getGeneratedKeys();
        return rs.next() ? rs.getInt(1) : -1;
    }


}




