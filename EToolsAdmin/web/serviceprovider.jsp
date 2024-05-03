<%-- 
    Document   : serviceprovider
    Created on : 11 Apr, 2018, 12:25:53 PM
    Author     : shri
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="pack.DBConnection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">

    <head>
        <title>Service Providers</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
        <!-- VENDOR CSS -->
        <link rel="stylesheet" href="assets/vendor/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/vendor/font-awesome/css/font-awesome.min.css">
        <link rel="stylesheet" href="assets/vendor/linearicons/style.css">
        <link rel="stylesheet" href="assets/vendor/chartist/css/chartist-custom.css">
        <!-- MAIN CSS -->
        <link rel="stylesheet" href="assets/css/main.css">
        <!-- FOR DEMO PURPOSES ONLY. You should remove this in your project -->
        <link rel="stylesheet" href="assets/css/demo.css">
        <!-- GOOGLE FONTS -->
        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700" rel="stylesheet">
        <!-- ICONS -->
        <link rel="apple-touch-icon" sizes="76x76" href="assets/img/apple-icon.png">
        <link rel="icon" type="image/png" sizes="96x96" href="assets/img/favicon.png">
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
            httpSession.setAttribute("username", username);
            String uri = request.getRequestURI();
            httpSession.setAttribute("uri", uri);
        %>
    </head>

    <body>
        <!-- WRAPPER -->
        <div id="wrapper">
            <!-- NAVBAR -->
            <nav class="navbar navbar-default navbar-fixed-top">
                <div class="brand">
                    <a href="adminhome.jsp">Tools on Rent</a>
                </div>
                <div class="container-fluid">
                    <div class="navbar-btn">
                        <button type="button" class="btn-toggle-fullwidth"><i class="lnr lnr-arrow-left-circle"></i></button>
                    </div>

                    <div id="navbar-menu">
                        <ul class="nav navbar-nav navbar-right">
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown"><img src="assets/img/user.png" class="img-circle" alt="Avatar"> <span>Admin</span> <i class="icon-submenu lnr lnr-chevron-down"></i></a>
                                <ul class="dropdown-menu">
                                    <li><a href="logout.jsp"><i class="lnr lnr-exit"></i> <span>Logout</span></a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
            <!-- LEFT SIDEBAR -->
            <div id="sidebar-nav" class="sidebar">
                <div class="sidebar-scroll">
                    <nav>
                        <ul class="nav">
                            <li ><a href="adminhome.jsp" ><i class="lnr lnr-home"></i> <span>Dashboard</span></a></li>
                            <li ><a href="user.jsp" class=""><i class="lnr lnr-users"></i> <span>Show Users</span></a></li>
                            <li class="active"><a href="serviceprovider.jsp" class="active"><i class="lnr lnr-users"></i> <span>Show Service Provider</span></a></li>
                        <li><a href="transactions.jsp" class=""><i class="lnr lnr-clock"></i> <span>Show Transactions</span></a></li>
                        </ul>
                    </nav>
                </div>
            </div>
            <!-- END LEFT SIDEBAR -->
            <div class="main">
                <!-- MAIN CONTENT -->
                <div class="main-content">
                    <div class="container-fluid">

                        <!-- TABLE NO PADDING -->
                        <div class="panel">
                            <div class="panel-heading">
                                <h3 class="panel-title">Show Service Provider Details</h3>
                            </div>
                            <div class="panel-body no-padding">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Sr. No.</th>
                                            <th>Username</th>
                                            <th>Email</th>
                                            <th>Mobile</th>
                                            <th>Status</th>
                                            <th>Edit</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            DBConnection db = new DBConnection();
                                            String sql = "Select * from tbl_user_reg where utype='Service Provider'";
                                            ResultSet rs = db.selectData(sql);
                                            int i = 1;
                                            while (rs.next()) {
                                        %>
                                        <tr>

                                            <td><%=i%></td>
                                            <td><%=rs.getString("username")%></td>
                                            <td><%=rs.getString("email")%></td>
                                            <td><%=rs.getString("mobile")%></td>
                                            <td><%=rs.getString("status_")%></td>
                                            <td><a href="edit.jsp?id=<%=rs.getString("email")%>" onclick="return confirm('Are you sure?');"><i class="fa  fa-pencil-square" aria-hidden="true"></i></a></td>
                                        </tr>
                                        <%
                                                i++;
                                            }%>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <!-- END TABLE NO PADDING -->
                    </div>
                    <!-- END MAIN CONTENT -->
                </div>
                <!-- END MAIN -->
                <div class="clearfix"></div>
                <footer>
                    <div class="container-fluid">
                        <p class="copyright">&copy; 2017 <a href="https://www.themeineed.com" target="_blank">Theme I Need</a>. All Rights Reserved.</p>
                    </div>
                </footer>
            </div>
            <!-- END WRAPPER -->
            <!-- Javascript -->
            <script src="assets/vendor/jquery/jquery.min.js"></script>
            <script src="assets/vendor/bootstrap/js/bootstrap.min.js"></script>
            <script src="assets/vendor/jquery-slimscroll/jquery.slimscroll.min.js"></script>
            <script src="assets/scripts/klorofil-common.js"></script>
    </body>

</html>
