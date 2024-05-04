package org.example.capestone_group_02;

import java.io.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.capestone_group_02.gabes_code.DBAdapter;

@WebServlet(name = "registerServlet", value = "/register-servlet")
public class Register extends HttpServlet {

    DBAdapter db;
    public void init() {
        db = new DBAdapter();
    }
// this makes the post request and registers a new user on the database
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("passwd");

        if(db.modifyTable("CALL Create_User(?,?,?)",
                username, email, password)) {
            // sets a url parameter
            request.setAttribute("status", "success");
        }else{
            request.setAttribute("status", "fail");
        }
        try{
            //loads the register jsp file and should display a new message
            RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
            rd.forward(request,response);
        }catch(ServletException e){
            System.out.println("Error! " + e.getMessage());
        }
    }

    public void destroy() {
    }
}