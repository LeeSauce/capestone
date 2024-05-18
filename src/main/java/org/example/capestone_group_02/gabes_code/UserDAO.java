package org.example.capestone_group_02.gabes_code;

import java.sql.*;

/**
 * The UserDAO class handles database operations related to the User object.
 */

public class UserDAO {
    private DBAdapter dbAdapter;
    
    /**
     * Constructor for UserDAO class. Initializes the DBAdapter instance.
     *
     * @throws SQLException            if a database access error occurs
     * @throws ClassNotFoundException if the class cannot be found
     */
    
    public UserDAO() throws SQLException, ClassNotFoundException {
        this.dbAdapter = DBAdapter.getInstance();
    }
    
    /**
     * Retrieves a user from the database based on the provided username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the User object if found, null otherwise
     * @throws SQLException if a database access error occurs
     */
    
    public ResultSet getUser(String username, String password) throws SQLException {
        ResultSet resultSet = dbAdapter.readTable("CALL User_Login(?,?)", username, password);
        return resultSet;
    }

    // Insert a new user into the database
    public boolean insertUser(String username,String email, String password) {
        String insertQuery = "CALL Create_User(?,?,?)"; // Example SQL procedure for inserting a new user
        return dbAdapter.modifyTable(insertQuery, username, email, password);
    }

    // Update user information in the database
    public boolean updateUser(int userId, String username, String password, String email) {
        String updateQuery = "CALL Update_User(?,?,?,?)"; // Example SQL procedure for updating user information
        return dbAdapter.modifyTable(updateQuery, String.valueOf(userId), username, password, email);
    }

    // Delete a user from the database
    public boolean deleteUser(int userId) {
        String deleteQuery = "CALL Delete_User(?)"; // Example SQL procedure for deleting a user
        return dbAdapter.modifyTable(deleteQuery, String.valueOf(userId));
    }

    // Retrieve all users from the database
    public ResultSet getAllUsers() {
        String selectQuery = "SELECT * FROM User"; // Example SQL query for selecting all users
        return dbAdapter.readTable(selectQuery);
    }

    // Other database methods...
}
