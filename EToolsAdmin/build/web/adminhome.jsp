<%-- 
    Document   : adminhome
    Created on : 11 Apr, 2018, 12:04:31 PM
    Author     : shri
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="pack.DBConnection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">

    <head>
        <title>Home Page</title>
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

        %>
    </head>

    <body>
        <!-- WRAPPER -->
        <div id="wrapper">
            <!-- NAVBAR -->
            <nav class="navbar navbar-default navbar-fixed-top">
                <div class="brand">
                    <a href="adminhome.jsp">Tools On Rent</a>
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
            <!-- END NAVBAR -->
            <!-- LEFT SIDEBAR -->
            <div id="sidebar-nav" class="sidebar">
                <div class="sidebar-scroll">
                    <nav>
                        <ul class="nav">
                            <li class="active"><a href="adminhome.jsp" class="active"><i class="lnr lnr-home"></i> <span>Dashboard</span></a></li>
                            <li><a href="user.jsp" class=""><i class="lnr lnr-users"></i> <span>Show Users</span></a></li>
                            <li><a href="serviceprovider.jsp" class=""><i class="lnr lnr-users"></i> <span>Show Service Provider</span></a></li>
                            <li><a href="transactions.jsp" class=""><i class="lnr lnr-clock"></i> <span>Show Transactions</span></a></li>
                        </ul>
                    </nav>
                </div>
            </div>
            <!-- END LEFT SIDEBAR -->
            <!-- MAIN -->
            <div class="main">
                <!-- MAIN CONTENT -->
                <div class="main-content">
                    <div class="container-fluid">
                        <!-- OVERVIEW -->
                        <div class="panel panel-headline">
                            <div class="panel-heading">

                            </div>
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-md-4">
                                        <div class="metric">
                                            <span class="icon"><i class="fa fa-download"></i></span>
                                                <%                                                    try {
                                                        DBConnection db = new DBConnection();
                                                        String sql = "SELECT COUNT(sr) FROM tbl_user_reg";
                                                        ResultSet rs = db.selectData(sql);
                                                        if (rs.next()) {
                                                %>
                                            <p>
                                                <span class="number"><%=rs.getString(1)%></span>
                                                <span class="title">Downloads</span>
                                            </p>
                                            <%                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            %>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="metric">
                                            <span class="icon"><i class="fa fa-users"></i></span>
                                                <%
                                                    try {
                                                        DBConnection db = new DBConnection();
                                                        String sql = "SELECT COUNT(sr) FROM tbl_user_reg WHERE utype='User'";
                                                        ResultSet rs = db.selectData(sql);
                                                        if (rs.next()) {
                                                %>
                                            <p>
                                                <span class="number"><%=rs.getString(1)%></span>
                                                <span class="title">Users</span>
                                            </p>
                                            <%                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            %>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="metric">
                                            <span class="icon"><i class="fa fa-user-circle-o"></i></span>
                                                <%
                                                    try {
                                                        DBConnection db = new DBConnection();
                                                        String sql = "SELECT COUNT(sr) FROM tbl_user_reg WHERE utype='Service Provider'";
                                                        ResultSet rs = db.selectData(sql);
                                                        if (rs.next()) {
                                                %>
                                            <p>
                                                <span class="number"><%=rs.getString(1)%></span>
                                                <span class="title">Service Provider</span>
                                            </p>
                                            <%                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            %>
                                        </div>
                                    </div>

                                </div>
                                <div class="row">

                                </div>
                            </div>
                        </div>
                        <!-- END OVERVIEW -->
                        <div class="row">
                            <div class="col-md-6">
                                <!-- RECENT PURCHASES -->

                                <!-- END RECENT PURCHASES -->
                            </div>
                            <div class="col-md-6">
                                <!-- MULTI CHARTS -->
                                <div class="panel">

                                </div>
                                <!-- END MULTI CHARTS -->
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-7">
                                <!-- TODO LIST -->

                                <!-- END TODO LIST -->
                            </div>
                            <div class="col-md-5">
                                <!-- TIMELINE -->

                                <!-- END TIMELINE -->
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4">
                                <!-- TASKS -->

                                <!-- END TASKS -->
                            </div>
                            <div class="col-md-4">
                                <!-- VISIT CHART -->

                                <!-- END VISIT CHART -->
                            </div>
                            <div class="col-md-4">
                                <!-- REALTIME CHART -->

                                <!-- END REALTIME CHART -->
                            </div>
                        </div>
                    </div>
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
        <script src="assets/vendor/jquery.easy-pie-chart/jquery.easypiechart.min.js"></script>
        <script src="assets/vendor/chartist/js/chartist.min.js"></script>
        <script src="assets/scripts/klorofil-common.js"></script>

    </body>
</html>
