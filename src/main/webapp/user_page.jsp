<%@ page import="org.example.capestone_group_02.gabes_code.User" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="org.example.capestone_group_02.gabes_code.DBAdapter" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.lang.ClassNotFoundException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <title>Image Booru!</title>
    <style>
        img{
            max-width: 20%;
            height: auto;
        }
        td{
            margin : 25px 25px 25px 25px;
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
            username = user.getUsername();
            writer.println("<h1>Welcome " + username + "!</h1>");

            // Using Singleton pattern to get DBAdapter instance
            DBAdapter db = null;
            try {
                db = DBAdapter.getInstance(); // Get the singleton instance
                ResultSet results;
                String userId = Integer.toString(user.getId());
                results = db.readTable("CALL Get_Img(?)", userId);
                while (results.next()) {
                    String imgName = results.getString(1);
                    ImgNames.add(imgName);
                }
            } catch (SQLException | ClassNotFoundException e) {
                writer.println("<p>Error accessing database: " + e.getMessage() + "</p>");
            }

            session.setAttribute("passedUser", user);
        }
    %>

    <div class="main_part">
        <fieldset>
            <form method="post" action="img-servlet" enctype="multipart/form-data">
                <input type="file" name="upload" id="upload" accept=".jpeg, .jpg, .png" required aria-required="true">
                <input type="submit" value="Upload" id="submit" name="submit">
            </form>
        </fieldset>
    </div>

    <div>
        <form method="get" action="logout-servlet">
            <input type="submit" value="Logout" name="logout" id="logout">
        </form>
    </div>

    <div>
        <table>
            <% for (String imgName : ImgNames) {
                writer.println("<tr><td><img src=\"" + path + "/" + imgName + "\" alt=\"" + imgName + "\"></td></tr>");
            } %>
        </table>
    </div>

</body>
</html>