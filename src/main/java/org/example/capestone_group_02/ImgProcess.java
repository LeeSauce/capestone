package org.example.capestone_group_02;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.capestone_group_02.gabes_code.DBAdapter;
import org.example.capestone_group_02.gabes_code.User;
import org.example.capestone_group_02.gabes_code.ImgThread;

import java.sql.SQLException;
import java.io.*;

/**
 * The ImgProcess servlet handles image upload functionality.
 */
@MultipartConfig
@WebServlet(name="imgServlet", value="/img-servlet")
public class ImgProcess extends HttpServlet {
    // Declarations
    File dir;
    File target;
    User user;
    String absolutePath;
    RequestDispatcher rd;
    DBAdapter db;

    /**
     * Initializes the servlet and sets up necessary directories.
     */
    @Override
    public void init() throws ServletException {
        try {
            db = DBAdapter.getInstance();
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Failed to initialize database connection", e);
        }

        // Set up directories
        this.absolutePath = getServletContext().getRealPath("/");
        File root = new File(this.absolutePath);
        // Set target directory
        this.target = new File(this.absolutePath + File.separator + "images");
        String parentName;
        String[] files;
        // Navigate up to the root project directory
        do {
            root = root.getParentFile();
            files = root.getAbsoluteFile().toString().trim().split("/");
            parentName = files[files.length - 1];
        } while (!parentName.equals("capestone_group_02"));

        // Create target directory if it doesn't exist
        if (!this.target.exists()) {
            this.target.mkdirs();
        }
        // Set directory for storing images
        this.dir = new File(root.getAbsolutePath() + File.separator + "src/main/webapp/images");
        if (!this.dir.exists()) {
            this.dir.mkdir();
        }
    }

    /**
     * Handles image upload requests.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Retrieve user information from session
        this.user = (User) req.getSession().getAttribute("passedUser");
        String mode = (String) req.getSession().getAttribute("uploadType");
        if(this.user == null){
            this.rd = req.getRequestDispatcher("/session-exp.jsp");
            this.rd.forward(req, resp);
        }

        try {
            new ImgThread(user, req, dir, target, mode).run();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Forward user to user_page.jsp
        req.setAttribute("user", user);
        this.rd = req.getRequestDispatcher("user_page.jsp");
        this.rd.forward(req, resp);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String row = req.getParameter("row");
        this.user = (User) req.getSession().getAttribute("passedUser");
        if(this.user == null){
            this.rd = req.getRequestDispatcher("/session-exp.jsp");
            this.rd.forward(req, resp);
        }
        else{
            // I am sorry for what I am about to do
//            if(action.equals("delete")){
//                delete(row);
//                req.setAttribute("user", user);
//                rd = req.getRequestDispatcher("/user_page.jsp");
//                rd.forward(req, resp);
//            }
//            else if(action.equals("edit")){
//                req.setAttribute("user", user);
//                req.setAttribute("row", row);
//                rd = req.getRequestDispatcher("/edit.jsp");
//                rd.forward(req, resp);
//
//            }
            switch (action.toLowerCase()) {
                case "delete":
                    delete(row);
                    req.setAttribute("user", user);
                    rd = req.getRequestDispatcher("/user_page.jsp");
                    rd.forward(req, resp);
                    break;
                case "edit":
                    req.setAttribute("user", user);
                    req.setAttribute("row", row);
                    rd = req.getRequestDispatcher("/edit.jsp");
                    rd.forward(req, resp);
                    break;
                case "all":
                    req.setAttribute("user", user);
                    rd = req.getRequestDispatcher("/user_page.jsp");
                    rd.forward(req, resp);
                    break;
                case "urgent":
                    req.setAttribute("user", user);
                    req.setAttribute("search", "urgent");
                    rd = req.getRequestDispatcher("/user_page.jsp");
                    rd.forward(req, resp);
                    break;
                case "in-progress":
                    req.setAttribute("user", user);
                    req.setAttribute("search", "in-progress");
                    rd = req.getRequestDispatcher("/user_page.jsp");
                    rd.forward(req, resp);
                    break;
                case "resolved":
                    req.setAttribute("user", user);
                    req.setAttribute("search", "resolved");
                    rd = req.getRequestDispatcher("/user_page.jsp");
                    rd.forward(req, resp);
                    break;
                case "draft":
                    req.setAttribute("user", user);
                    req.setAttribute("search", "draft");
                    rd = req.getRequestDispatcher("/user_page.jsp");
                    rd.forward(req, resp);
                    break;
            }
        }
    }
    private void delete(String row){
        db.modifyTable("CALL Delete_Post(?);", row);
    }
}

