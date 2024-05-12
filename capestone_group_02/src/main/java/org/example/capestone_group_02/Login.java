package org.example.capestone_group_02;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.capestone_group_02.gabes_code.DBAdapter;
import org.example.capestone_group_02.gabes_code.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * The Login servlet handles user login functionality.
 */
@WebServlet(name = "loginServlet", value = "/login_servlet")
public class Login extends HttpServlet {

    // Declarations
    DBAdapter db;
    ResultSet resultSet;
    RequestDispatcher rd;

    /**
     * Initializes the servlet and sets up database connection.
     */
    @Override
    public void init() throws ServletException {
        try {
            // Use Singleton instance for database connection
            this.db = DBAdapter.getInstance();
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Failed to initialize database connection", e);
        }
    }

    /**
     * Handles login POST requests.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Retrieve username and password from request
        String username = req.getParameter("username");
        String passwd = req.getParameter("passwd");

        try {
            // Check user credentials in the database
            this.resultSet = this.db.readTable("CALL User_Login(?,?)", username, passwd);
            if (resultSet != null && resultSet.next()) {
                // If user exists, retrieve user details
                Object[] objects = sortUserCredentials(this.resultSet);

                int id = (int) objects[0];
                String user = (String) objects[1];
                String email = (String) objects[2];
                String pass = (String) objects[3];

                // Forward user to user_page.jsp
                req.setAttribute("user", new User(id, user, email, pass));
                this.rd = req.getRequestDispatcher("user_page.jsp");
                this.rd.forward(req, resp);
            } else {
                // If login fails, forward user to login.jsp with fail attribute set to true
                req.setAttribute("fail", "true");
                this.rd = req.getRequestDispatcher("login.jsp");
                this.rd.forward(req, resp);
            }
        } catch (SQLException e) {
            // Handle database errors
            this.db.sqlErrorHandle(e);
            req.setAttribute("fail", "true");
            this.rd = req.getRequestDispatcher("login.jsp");
            this.rd.forward(req, resp);
        }
    }

    /**
     * Retrieves user credentials from ResultSet.
     */
    private Object[] sortUserCredentials(ResultSet resultSet) throws SQLException {
        Object[] objects = new Object[4];
        ResultSetMetaData metaData = resultSet.getMetaData();

        // Iterate through ResultSet and store user credentials in an object array
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            if (i == 1) {
                objects[i - 1] = resultSet.getInt(i);
            } else {
                objects[i - 1] = resultSet.getString(i);
            }
        }
        return objects;
    }
}
