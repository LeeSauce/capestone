package org.example.capestone_group_02;

import java.io.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.capestone_group_02.gabes_code.DBAdapter;
import org.example.capestone_group_02.gabes_code.UserDAO;

import java.sql.SQLException;

/**
 * The Register servlet handles user registration functionality.
 */
@WebServlet(name = "registerServlet", value = "/register-servlet")
public class Register extends HttpServlet {

    // Declarations
    DBAdapter db;

    /**
     * Initializes the servlet and sets up database connection.
     */
    @Override
    public void init() throws ServletException {
//        try {
//            // Use Singleton instance for database connection
////            db = DBAdapter.getInstance();
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new ServletException("Failed to initialize database connection", e);
//        }
    }

    /**
     * Handles registration POST requests.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Retrieve user details from request parameters
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("passwd");

        try {
            // Attempt to create a new user in the database
            if (new UserDAO().insertUser(username,email,password)) {
                // If successful, set status attribute to success
                request.setAttribute("status", "success");
            } else {
                // If failed, set status attribute to fail
                request.setAttribute("status", "fail");
            }
            // Forward the request to register.jsp
            RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
            rd.forward(request, response);
        } catch (IOException | ServletException ex) {
            // Handle servlet errors
            System.out.println("Servlet error: " + ex.getMessage());
            // Send internal server error response
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Servlet exception in Register");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Destroys the servlet and closes database connection.
     */
    @Override
    public void destroy() {
        super.destroy();
        try {
            // Close the database connection
            if (db != null) {
                db.disconnectFromDB();
            }
        } catch (SQLException e) {
            // Handle errors when closing the database connection
            System.out.println("Error closing database connection: " + e.getMessage());
        }
    }
}
