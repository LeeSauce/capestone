package org.example.capestone_group_02.gabes_code;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.*;
import java.sql.SQLException;
import java.util.Objects;

public class ImgThread extends Thread {
    private final File dir;
    private final File target;
    User user;
    HttpServletRequest req;
    Part img;
    String title;
    String description;
    String status;
    String mode;

    private final DBAdapter db = DBAdapter.getInstance();

    public ImgThread(User user, HttpServletRequest req, File dir, File target, String mode) throws SQLException, ClassNotFoundException {
        this.user = user;
        this.req = req;
        this.dir = dir;
        this.target = target;
        this.mode = mode;
    }

    @Override
    public void run() {
        String userId = Integer.toString(this.user.getId());
        // Set paths for storing the image
        setFields();
        String imgName = img.getSubmittedFileName();
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
            if(mode.equals("update")){
                String row = (String)req.getSession().getAttribute("row");
                updatePost(userId, imgName, row);
            }else{
                insertPhotoToDB(userId, imgName);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            req.setAttribute("status", "fail");
        }
    }

    private void insertPhotoToDB(String userID, String imgName) throws SQLException {
        db.modifyTable("CALL Add_Img(?,?,?,?,?);", userID, imgName, title, description, status);
    }

    private void updatePost(String userID, String imgName, String row) throws SQLException {
        db.modifyTable("CALL Modify_Post(?,?,?,?,?,?);", userID, imgName, title, description, status, row);
    }

    private void setFields(){
        try{
            img = req.getPart("upload");
        }catch (ServletException | IOException e){
            System.out.println("Oh no!");
        }
        title = this.req.getParameter("title");
        description = this.req.getParameter("description");
        status = req.getParameter("status");
    }
}
