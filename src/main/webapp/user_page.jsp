<%@ page import="org.example.capestone_group_02.gabes_code.User" %>
<%@ page import="java.io.PrintWriter" %><%--
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
</head>
<body>

    <%
        response.setContentType("text/html;charset=UTF-8");
        User user = (User)request.getAttribute("user");
        PrintWriter writer = response.getWriter();
        String username = null;
        if (user != null) {
            // todo: I need to make some sort of connection between the user and the database to get images
            username = user.getUsername();
            writer.println("<h1>"+"Welcome "+username+"!"+"</h1>");
        }
    %>

</body>
</html>
