/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import datalib.JavaCode;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import pack.DBConnection;

/**
 *
 * @author Dinesh
 */
public class ConfirmBooking extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            /* TODO output your page here. You may use following sample code. */
            JSONObject json = new JSONObject();
            String vehicleno = request.getParameter("vehicleno");
            String mobile = request.getParameter("mobile");
            String pickup = request.getParameter("pickup");
            String email = request.getParameter("email");
            String userid = request.getParameter("userid");
            String lat = request.getParameter("lat");
            String lon = request.getParameter("lon");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String today = simpleDateFormat.format(date);
            String sql = "SELECT * FROM tbl_confirm_booking WHERE vehicle_no='" + vehicleno + "' and date='" + today + "' and status='Confirm' or status='Requested'";
            DBConnection db = new DBConnection();
            ResultSet rs = db.selectData(sql);
            if (rs.next()) {
                json.put("failed", 1);
            } else {
                rs.close();
                sql = "INSERT INTO tbl_confirm_booking VALUES(0,'" + userid + "','" + email + "','" + vehicleno + "','" + mobile + "','" + lat + "','" + lon + "','" + pickup + "','" + today + "','Requested','','','')";
                int i = db.insert(sql);
                if (i > 0) {
                    json.put("success", 1);
                    sql = "SELECT * FROM tbl_user_reg WHERE email='" + email + "'";
                    rs = db.selectData(sql);
                    if (rs.next()) {
                        String register_token = rs.getString("mobile");
                        String message = "Dear Service Provider, new service request from customer. Contact on "+mobile;
                       try{
                        JavaCode javaCode=new JavaCode();
                        javaCode.sendMessage(register_token, message);
                       }catch(Exception e)
                       {
                           e.printStackTrace();
                       }
                        //PushNotifictionHelper.sendPushNotification(register_token,title, message);
                    }
                }
            }
            response.setContentType("application/json");
            response.getWriter().write(json.toString());
        } catch (Exception e) {
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
