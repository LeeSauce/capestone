package org.example.capestone_group_02;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "logoutServlet", value = "/logout-servlet")
public class Logout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalidate the current session and remove any attributes
        HttpSession session = request.getSession(false);
        // false means do not create a new session if one does not exist
        if (session != null) {
            request.logout();
            session.invalidate();
        }

        // Redirect to the login page or home page
        response.sendRedirect("logout.jsp"); // 'login.jsp' is the login page
    }
}