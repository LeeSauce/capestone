package org.example.capestone_group_02;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.capestone_group_02.gabes_code.DBAdapter;
import org.example.capestone_group_02.gabes_code.User;
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
        String userId = Integer.toString(this.user.getId());

        // Retrieve uploaded image
        Part img = req.getPart("upload");
        String imgName = img.getSubmittedFileName();

        // Set paths for storing the image
        String uploadPath = this.dir.getAbsolutePath() + "/" + imgName;
        String targetUploadPath = this.target.getAbsolutePath() + "/" + imgName;

        // Write image data to disk
        try (OutputStream out = new FileOutputStream(uploadPath);
             OutputStream targetOut = new FileOutputStream(targetUploadPath);
             InputStream in = img.getInputStream()) {
            int data;
            while ((data = in.read()) != -1) {
                out.write(data);
                targetOut.write(data);
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }

        // Insert image data into the database
        try {
            insertPhotoToDB(userId, imgName);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            req.setAttribute("status", "fail");
        }

        // Forward user to user_page.jsp
        req.setAttribute("user", user);
        this.rd = req.getRequestDispatcher("user_page.jsp");
        this.rd.forward(req, resp);
    }

    /**
     * Inserts image data into the database.
     */
    private void insertPhotoToDB(String userID, String imgName) throws SQLException {
        db.modifyTable("CALL Add_Img(?,?);", userID, imgName);
    }
}
