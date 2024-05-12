package org.example.capestone_group_02.gabes_code;

import java.sql.*;
/**
 * The DBAdapter class manages the database connection and provides methods for executing SQL queries
 * and handling exceptions.
 */
public class DBAdapter {
    private static DBAdapter instance;
    private Connection connection;
    private final String URL = "jdbc:mysql://localhost:3306/CAPSTONE?useSSL=false";
    private final String USER = "groupmem";
    private final String PASS = "111";
    
    /**
     * Private constructor to ensure singleton pattern. Establishes a database connection
     * when the instance is created.
     *
     * @throws SQLException            if a database access error occurs
     * @throws ClassNotFoundException if the class cannot be found
     */
    
    private DBAdapter() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection(URL, USER, PASS);
    }
    
    /**
     * Returns the instance of the DBAdapter class. If the instance is null, it creates a new one.
     *
     * @return the DBAdapter instance
     * @throws SQLException            if a database access error occurs
     * @throws ClassNotFoundException if the class cannot be found
     */
    

    public static DBAdapter getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new DBAdapter();
        }
        return instance;
    }
    
    /**
     * Closes the database connection.
     *
     * @throws SQLException if a database access error occurs
     */
    
    public void disconnectFromDB() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    
    /**
     * Handles SQL exceptions by printing error details.
     *
     * @param e the SQLException object
     */
    
    public void sqlErrorHandle(SQLException e) {
        System.out.println("Something happened!");
        System.out.println("Error Code : " + e.getErrorCode());
        System.out.println("Error : " + e.getMessage());
    }

    /**
     * Executes a modification SQL query.
     *
     * @param storedProcedure the SQL stored procedure
     * @param args            the arguments for the stored procedure
     * @return true if the query is successful, false otherwise
     */
    
    public boolean modifyTable(String storedProcedure, String... args) {
        String sql = storedProcedure;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setString(i + 1, args[i]);
            }
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            sqlErrorHandle(e);
            return false;
        }
    }
    
    /**
     * Executes a read SQL query.
     *
     * @param storedProcedure the SQL stored procedure
     * @param args            the arguments for the stored procedure
     * @return a ResultSet object containing the data produced by the query
     */
    
    public ResultSet readTable(String storedProcedure, String... args) {
        String sql = storedProcedure;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql,
                     ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    preparedStatement.setString(i + 1, args[i]);
                }
            }
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            sqlErrorHandle(e);
            return null;
        }
    }
    
    /**
     * Main method for testing the DBAdapter class.
     *
     * @param args the command-line arguments
     * @throws SQLException if a database access error occurs
     */
    
    public static void main(String[] args) throws SQLException {
        DBAdapter db = null;
        try {
            db = DBAdapter.getInstance();
            String createUser = "CALL Create_User(?,?,?);";

            ResultSet resultSet = db.readTable("SELECT * FROM USER WHERE userId = ?;", "1");
            resultSet.next();
            StringBuilder builder = new StringBuilder();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                builder.append(resultSet.getString(i));
                if (i != metaData.getColumnCount()) {
                    builder.append(", ");
                }
            }
            String row = builder.toString();
            System.out.println(row);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.disconnectFromDB();
            }
        }
    }
}
