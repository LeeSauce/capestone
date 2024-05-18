package org.example.capestone_group_02.gabes_code;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.*;
import java.sql.SQLException;

public class ImgThread extends Thread {
    private File dir;
    private File target;
    User user;
    HttpServletRequest req;
    Part img;

    private final DBAdapter db = DBAdapter.getInstance();

    public ImgThread(User user, HttpServletRequest req, File dir, File target) throws SQLException, ClassNotFoundException {
        this.user = user;
        this.req = req;
        this.dir = dir;
        this.target = target;
    }

    @Override
    public void run() {
        String userId = Integer.toString(this.user.getId());
        // Set paths for storing the image
        try{
            img = req.getPart("upload");
        }catch (ServletException | IOException e){
            System.out.println("Oh no!");
        }
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
            insertPhotoToDB(userId, imgName);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            req.setAttribute("status", "fail");
        }
    }

    private void insertPhotoToDB(String userID, String imgName) throws SQLException {
        db.modifyTable("CALL Add_Img(?,?);", userID, imgName);
    }
}
