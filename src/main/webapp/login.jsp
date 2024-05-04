<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Image Booru!</title>
    <meta charset="UTF-8">
</head>
<body>
<h1><%= "Image Booru!" %>
</h1>
<br/>
    <div class ="main-section">
        <fieldset>
            <form class="upload-form" method="post" action="login.jsp">
                <input type="text" name="username" id="username" placeholder="username" required aria-required="true">
                <br>
                <input type="password" name="passwd" id="passwd" placeholder="password" required aria-required="true">
                <br>
                <input type="submit" value="login" id="login" name="login">
            </form>
            <a href="register.jsp">Register an Account</a>
        </fieldset>
    </div>
</body>
</html>