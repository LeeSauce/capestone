<%@ page import="org.example.capestone_group_02.gabes_code.User" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="org.example.capestone_group_02.gabes_code.DBAdapter" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.SQLException" %><%--
  Created by IntelliJ IDEA.
  User: gabelee
  Date: 2024-05-04
  Time: 1:02 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Image Booru!</title>
    <style>
        img{
            max-width: 5%;
            height: auto;
        }
        td{
            margin : 25px 25px 25px 25px
        }
    </style>
</head>
<body>

    <%
        response.setContentType("text/html;charset=UTF-8");
        User user = (User)request.getAttribute("user");
        String path = "images";
        PrintWriter writer = response.getWriter();
        ArrayList<String> ImgNames = new ArrayList<>();
        String username = null;
        if (user != null) {
            // todo: I need to make some sort of connection between the user and the database to get images
            username = user.getUsername();
            writer.println("<h1>"+"Welcome "+username+"!"+"</h1>");

            DBAdapter db = new DBAdapter();
            ResultSet results;
            String userId = Integer.toString(user.getId());
            try{
                results = db.readTable("CALL Get_Img(?)", userId);
                while(!results.isLast()) {
                    results.next();
                    String imgName = results.getString(1);
                    ImgNames.add(imgName);
                }
            }catch (SQLException e){
                db.sqlErrorHandle(e);
            }

            session.setAttribute("passedUser", user);
        }
    %>

    <div class="main_part">
        <fieldset>
            <form method="post" action="img-servlet" enctype="multipart/form-data">
                <input type="file"name="upload" id="upload"
                       accept=".jpeg, .jpg, .png, .gif" required aria-required="true">
                <input type="submit" value="Upload" id="submit" name="submit">
            </form>
        </fieldset>
    </div>

    <div>
        <table>
            <%// I am too lazy to set up another fileInput stream so this will do for the sake of simplicity
                for(String imgName : ImgNames){
                    writer.println("<tr>");
                    writer.println("<td>");
                    writer.println("<img src=\""+path+"/"+imgName+"\" alt=\""+imgName+"\">");
                    writer.println("</td>");
                }
            %>
        </table>
    </div>

</body>
</html>
