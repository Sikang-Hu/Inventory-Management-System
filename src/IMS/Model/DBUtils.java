package IMS.Model;

import java.sql.*;

public class DBUtils {


    private String url;
    private String user;
    private String password;
    private Connection con = null;

    public DBUtils(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.con = getConnection();
    }

    public Connection getConnection()
    {
        if (con == null) {
            try {
                con = DriverManager.getConnection(url, user, password);
                return con;
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }

        return con;
    }

    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }


    public int insertOneRecord(String insertSQL)
    {
        // System.out.println("INSERT STATEMENT: "+insertSQL);
        int key = -1;
        try {

            // get connection and initialize statement
            Connection con = getConnection();
            Statement stmt = con.createStatement();

            stmt.executeUpdate(insertSQL, Statement.RETURN_GENERATED_KEYS);

            // extract auto-incremented ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) key = rs.getInt(1);

            // Cleanup
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Could not insert record: "+insertSQL);
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return key;
    }


    /**
     * For a table of terms consisting of an id and string value pair, get the id of the term
     * adding a new term if it does not yet exist in the table
     * @param table The table of terms
     * @param term The term value
     * @return The id of the term
     */
    public int getOrInsertTerm(String table, String keyColumn, String valueColumn, String term)
    {

        int key = -1;

        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            String sqlGet = "SELECT "+keyColumn+" FROM "+table+" WHERE "+valueColumn+" = '"+term.toUpperCase()+"'";
            ResultSet rs = stmt.executeQuery(sqlGet);
            if (rs.next())
                key = rs.getInt(1);
            else {
                String sqlInsert = "INSERT INTO "+table+" ("+valueColumn+") VALUES ('"+term.toUpperCase()+"')";
                stmt.executeUpdate(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                rs = stmt.getGeneratedKeys();
                if (rs.next()) key = rs.getInt(1);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return key;

    }

    /**
     * update term using sql: UPDATE table SET setCol = setVal WHERE whereCol = whereVal
     *
     * @return -1 if update didn't process, otherwise return either (1) the row count for SQL Data
     * Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
     */
    public int updateTerm(String table, String setCol, String setVal, String whereTerm) {

        int key = -1;

        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            String sqlGet = "UPDATE " + table + " SET " + setCol + " = " + setVal + " WHERE " + whereTerm;
            System.out.println(sqlGet);
            key = stmt.executeUpdate(sqlGet);
            stmt.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return key;
    }


    /**
     * Find and return the term id(PK) by it's value. Return -1 if not found
     * @param table
     * @param keyColumn
     * @param whereTerm conditional term after WHERE
     * @return term id (PK) if found, -1 otherwise.
     */
    public int findTerm(String table, String keyColumn, String whereTerm) {
        int key = -1;

        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            String sqlGet = "SELECT " + keyColumn + " FROM " + table + " WHERE " + whereTerm;
            ResultSet rs = stmt.executeQuery(sqlGet);
            if (rs.next())
                key = rs.getInt(1);

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return key;
    }


    public ResultSet findRow(String table, String whereTerm) {
        ResultSet result = null;
        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            String sqlGet = "SELECT *" + " FROM " + table + " WHERE " + whereTerm;
            ResultSet rs = stmt.executeQuery(sqlGet);
            if (rs.next())
                result = rs;

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}

