<%-- 
    Document   : edit
    Created on : 11 Apr, 2018, 1:33:59 PM
    Author     : shri
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="pack.DBConnection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            HttpSession httpSession = request.getSession(false);
            try {
                if (httpSession.getAttribute("username") == null) {
                    response.sendRedirect("index.html?message='expired'");
                }
            } catch (Exception e) {
                response.sendRedirect("index.html?message='expired'");
            }
            String username = httpSession.getAttribute("username").toString();
            String uri = httpSession.getAttribute("uri").toString();
            httpSession.setAttribute("username", username);
            String email = request.getParameter("id");

            DBConnection db = new DBConnection();
            String sql = "SELECT * FROM tbl_user_reg WHERE email='" + email + "'";
            ResultSet rs = db.selectData(sql);
            if (rs.next()) {
                String status1 = rs.getString("status_");
                if (status1.equals("active")) {
                    String qry = "UPDATE tbl_user_reg SET status_='deactive' WHERE email='" + email + "'";
                    db.insert(qry);
                    out.println("<script>");
                    out.println("alert('User is deactivated')");
                    out.println("location='" + uri + "'");
                    out.println("</script>");
                } else {
                    String qry = "UPDATE tbl_user_reg SET status_='active' WHERE email='" + email + "'";
                    db.insert(qry);
                    out.println("<script>");
                    out.println("alert('User is activated')");
                    out.println("location='" + uri + "'");
                    out.println("</script>");
                }
            }
        %>
    </body>
</html>
