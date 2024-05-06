package org.example.capestone_group_02.gabes_code;

import java.sql.*;

public class DBAdapter {
    protected final String URL = "jdbc:mysql://localhost:3306/CAPSTONE?useSSL=false";
    protected final String USER = "groupmem";
    protected final String PASS = "111";

    private Connection connection;
    private PreparedStatement prepared;
    private ResultSet results;
    private String sql;


    // connects to a database
    private void connectToDB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(this.URL, this.USER, this.PASS);
    }
    // disconnects from database
    public void disconnectFromDB() throws SQLException {
        connection.close();
    }

    // displays database errors
    public void sqlErrorHandle (SQLException e){
        System.out.println("Something happened!");
        System.out.println("Error Code : " + e.getErrorCode());
        System.out.println("Error : " + e.getMessage());
    }

    // a flexible method that can handle Creating, Updating, and Deleting rows from a database
    public boolean modifyTable(String storedProcedure, String... args){
        this.sql = storedProcedure;
        try{
            connectToDB();
            this.prepared = this.connection.prepareStatement(this.sql);
            for(int i = 0; i < args.length; i++){
                this.prepared.setString(i+1, args[i]);
            }
            this.prepared.executeUpdate();
            disconnectFromDB();
            return true;
        }catch (SQLException e){
            sqlErrorHandle(e);
            return false;
        }catch (ClassNotFoundException c){
            System.out.println("Something happened!");
            System.out.println(c.getMessage());
            return false;
        }
    }

    // returns a result set for reading rows
    public ResultSet readTable(String storedProcedure, String... args){
        this.sql = storedProcedure;
        try {
            connectToDB();

            this.prepared = this.connection.prepareStatement(this.sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // this part will only run if arguments are passed
            if(args.length > 0){
                for(int i = 0; i < args.length; i++){
                    this.prepared.setString(i+1, args[i]);
                }
            }
            this.results = this.prepared.executeQuery();
            return results;
        }catch (SQLException e){
            sqlErrorHandle(e);
            return null;
        }catch (ClassNotFoundException c){
            System.out.println("Something happened!");
            System.out.println(c.getMessage());
            return null;
        }
    }

}

class Main {
    public static void main(String[] args) throws SQLException {
        DBAdapter db = new DBAdapter();
        String createUser = "CALL Create_User(?,?,?);";

        /*if(db.modifyTable(createUser, "Gabe", "gabe@email.com", "111")){
            System.out.println("Inserted Values!");
        }*/

        // this is just for testing purposed. Store procedures should only be passed here
        ResultSet resultSet = db.readTable("SELECT * FROM USER WHERE userId = ?;", "1");
        resultSet.next();
        StringBuilder builder = new StringBuilder();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for(int i = 1; i <= metaData.getColumnCount(); i ++){
            builder.append(resultSet.getString(i));
            if(i != metaData.getColumnCount()){
                builder.append(", ");
            }
        }
        String row = builder.toString();
        System.out.println(row);
    }
}
