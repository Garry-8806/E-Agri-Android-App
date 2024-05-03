<%-- 
    Document   : logout
    Created on : 25 Jan, 2018, 4:30:34 PM
    Author     : shri
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Logout Page</title>
    </head>
    <body>
        <%
            HttpSession httpSession = request.getSession();
            httpSession.invalidate();
            response.sendRedirect("index.html");
        %>
    </body>
</html>
