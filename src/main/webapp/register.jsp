<%@ page import="java.io.PrintWriter" %><%--
  Created by IntelliJ IDEA.
  org.example.capestone_group_02.gabes_code.User: gabelee
  Date: 2024-05-03
  Time: 10:29 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Image Booru!</title>
</head>
<body>
<h1><%= "Image Booru!" %>
</h1>
<br/>
<div class ="main-section">

    <%
        // gets a URL parameter
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        String status = (String) request.getAttribute("status");

        // if the URL parameter is not null it will display either of the two messages
        if(status != null){
            if (status.equals("success")) {
                writer.write("<p>Success! Your account has been registered.</p>");
            }else if (status.equals("fail")) {
                writer.write("<p>Fail! Your account has not been registered.</p>");
            }
        }
    %>

    <fieldset>
        <form class="upload-form" method="post" action="register-servlet">
            <input type="text" name="username" id="username" placeholder="username" required aria-required="true">
            <br>
            <input type="email" name="email" id="email" placeholder="email" required aria-required="true">
            <br>
            <input type="password" name="passwd" id="passwd" placeholder="password" required aria-required="true">
            <br>
            <input type="submit" value="register" id="register" name="register">
        </form>
        <a href="login.jsp">Back to login</a>
    </fieldset>
</div>
</body>
</html>
