<%@ page import="org.example.capestone_group_02.gabes_code.User" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="org.example.capestone_group_02.gabes_code.DBAdapter" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %><%--
  Created by IntelliJ IDEA.
  User: gabelee
  Date: 2024-06-03
  Time: 5:11 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Taskify</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<%
    response.setContentType("text/html;charset=UTF-8");
    User user = (User)request.getAttribute("user");
    String row = (String)request.getAttribute("row");
    PrintWriter writer = response.getWriter();
    String path = "images";
    String[] vals = new String[5];
    if (user != null && row != null) {

        // Using Singleton pattern to get DBAdapter instance
        DBAdapter db = null;

        try {
            db = DBAdapter.getInstance(); // Get the singleton instance
            ResultSet results;
            results = db.readTable("CALL Get_Post_Vals(?)", row);
            if(results != null) {
                results.next();
                vals[0] = results.getString(1);
                vals[1] = results.getString(2);
                vals[2] = results.getString(3);
                vals[3] = results.getString(4);
                vals[4] = results.getString(5);
            }
        } catch (SQLException | ClassNotFoundException e) {
            writer.println("<p>Error accessing database: " + e.getMessage() + "</p>");
        }

        session.setAttribute("passedUser", user);
        session.setAttribute("uploadType", "update");
        session.setAttribute("row",vals[4]);
    }
%>

<div class="main_part">
    <fieldset>
        <form method="post" action="img-servlet" enctype="multipart/form-data">
            <label for="title">Title: </label>
            <input type="text" id="title" name="title" aria-required="false" value="<%=vals[1]%>">
            <br>
            <input type="file" name="upload" id="upload" accept=".jpeg, .jpg, .png" required aria-required="true"
                   >
            <br>
            <a href="<%=path+"/"+vals[0]%>" download>Original Image upload: </a><img src="<%=path+"/"+vals[0]%>" alt="original image">
            <br>
            <label for="description">Description: </label>
            <input type="text" id="description" name="description" aria-required="false" value="<%=vals[2]%>">
            <br>
            <table>
                <tr>
                    <td><label for="urgent">Urgent </label></td>
                    <td><input type="radio" id="urgent" name="status" value="urgent"
                        <%if(vals[3].equals("urgent")){%>
                    checked <%}%>></td>
                </tr>
                <tr>
                    <td><label for="in-progress">In Progress </label></td>
                    <td><input type="radio" id="in-progress" name="status" value="in-progress"
                        <%if(vals[3].equals("in-progress")){%>
                               checked <%}%>></td>
                </tr>
                <tr>
                    <td><label for="resolved">Resolved </label></td>
                    <td><input type="radio" id="resolved" name="status" value="resolved"
                        <%if(vals[3].equals("resolved")){%>
                               checked <%}%>></td>
                </tr>
                <tr>
                    <td><label for="draft">Draft </label></td>
                    <td> <input type="radio" id="draft" name="status" value="draft"
                        <%if(vals[3].equals("draft")){%>
                                checked <%}%>></td>
                </tr>
            </table>
            <br>
            <input type="submit" value="Update" id="submit" name="submit">
        </form>
    </fieldset>
</div>

</body>
</html>
