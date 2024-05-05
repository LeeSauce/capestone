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

import java.io.*;


@MultipartConfig
@WebServlet(name="imgServlet", value="/img-servlet")
public class ImgProcess extends HttpServlet {
    File dir;
    User user;
    String absolutePath;
    RequestDispatcher rd;

    @Override
    public void init() throws ServletException {
        //This sets up a directory to be loaded into the project folder for simplicity’s sake

        this.absolutePath = getServletContext().getRealPath("/");
        File root = new File(this.absolutePath);
        String parentName;
        String[] files;
        do{
            root = root.getParentFile();
            files = root.getAbsoluteFile().toString().trim().split("/");
            parentName = files[files.length - 1];
        }while(!parentName.equals("capestone_group_02"));

        dir = new File(root.getAbsolutePath() + File.separator + "src/main/webapp/images");
        if(!this.dir.exists()){
            this.dir.mkdir();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.user = (User)req.getSession().getAttribute("passedUser");
        String userId = Integer.toString(this.user.getId());

        // Part is used to get part from a html multi/data-form
        Part img = req.getPart("upload");
        // gets the file name based on the file submitted in the form
        // getName() will get the parameter name
        String imgName = img.getSubmittedFileName();

        // sets the upload path
        String uploadPath = this.dir.getAbsolutePath()+"/"+imgName;
        // Writes the data to the upload path
        try{
            OutputStream out = new FileOutputStream(uploadPath);
            InputStream in = img.getInputStream();
            int data;
            while((data = in.read()) != -1){
                out.write(data);
            }
            out.close();
            in.close();
        }catch (IOException e){
            System.out.println("File not found");
        }
        insertPhotoToDB(userId, imgName);

        // return user back to the session
        req.setAttribute("user", user);
        this.rd = req.getRequestDispatcher("user_page.jsp");
        this.rd.forward(req, resp);
    }

    //inserts the image file name to database
    private void insertPhotoToDB (String userID, String imgName){
        DBAdapter db = new DBAdapter();
        db.modifyTable("CALL Add_Img(?,?);", userID, imgName);
    }
}