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
    <title>Taskify</title>
    <style>
        img{
            width: auto;
            max-height: 200px;
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
        String search;
        search = (String) request.getAttribute("search");
        String path = "images";
        PrintWriter writer = response.getWriter();
        ArrayList<String[]> posts = new ArrayList<>();
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
                if(search != null) {
                    results = db.readTable("CALL Get_Post_Search(?,?)", userId, search);
                }
                else {
                    results = db.readTable("CALL Get_Post(?)", userId);
                }
                if(results != null) {
                    while (results.next()) {
                        String imgName = results.getString(1);
                        String title = results.getString(2);
                        String description = results.getString(3);
                        String status = results.getString(4);
                        String id = results.getString(5);
                        posts.add(new String[]{imgName,title,description,status, id});
                    }
                }
            } catch (SQLException | ClassNotFoundException e) {
                writer.println("<p>Error accessing database: " + e.getMessage() + "</p>");
            }

            session.setAttribute("passedUser", user);
            session.setAttribute("uploadType", "insert");
        }
    %>

    <div class="main_part">
        <fieldset>
            <form method="post" action="img-servlet" enctype="multipart/form-data">
                <label for="title">Title: </label>
                <input type="text" id="title" name="title" aria-required="false">
                <br>
                <input type="file" name="upload" id="upload" accept=".jpeg, .jpg, .png" required aria-required="true">
                <br>
                <label for="description">Description: </label>
                <input type="text" id="description" name="description" aria-required="false">
                <br>
                <table>
                    <tr>
                        <td><label for="urgent">Urgent </label></td>
                        <td><input type="radio" id="urgent" name="status" value="urgent"></td>
                    </tr>
                    <tr>
                        <td><label for="in-progress">In Progress </label></td>
                        <td><input type="radio" id="in-progress" name="status" value="in-progress"></td>
                    </tr>
                    <tr>
                        <td><label for="resolved">Resolved </label></td>
                        <td><input type="radio" id="resolved" name="status" value="resolved"></td>
                    </tr>
                    <tr>
                        <td><label for="draft">Draft </label></td>
                        <td> <input type="radio" id="draft" name="status" value="draft"></td>
                    </tr>
                </table>
                <br>
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
            <tr>
                <td>Search: </td>
                <td><a href="img-servlet?action=all">All</a></td>
                <td><a href="img-servlet?action=urgent">Urgent</a></td>
                <td><a href="img-servlet?action=in-progress">In-progress</a></td>
                <td><a href="img-servlet?action=resolved">Resolved</a></td>
                <td><a href="img-servlet?action=draft">Draft</a></td>
            </tr>
        </table>
        <table>

            <% int count = 4;
                for (String[] post : posts) {
                String source = path + "/" + post[0];

            %>
                <%if (count == 4){%>
                <tr>
                    <%}%>
                <td>
                    <table>
                        <tr><td><img alt='<%= post[0]%>' src='<%= source%>' ></td></tr>
                        <tr><td>Title: <%= post[1]%></td></tr>
                        <tr><td>Description: <%= post[2]%></td></tr>
                        <tr><td>Status: <%= post[3]%></td></tr>
                        <tr><td><a href="img-servlet?action=delete&row=<%=post[4]%>">Delete</a></td></tr>
                        <tr><td><a href="img-servlet?action=edit&row=<%=post[4]%>">Edit</a></td></tr>
                    </table>
                </td>
                <%
                    count --;
                    if(count == 0){
                %>
                </tr>
                <%count = 4; }%>
            <%}%>

        </table>
    </div>

</body>
</html>