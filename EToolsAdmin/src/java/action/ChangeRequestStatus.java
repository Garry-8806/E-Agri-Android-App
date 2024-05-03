/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

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
public class ChangeRequestStatus extends HttpServlet {

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
            PrintWriter out = response.getWriter();
            String id = request.getParameter("id");
            String status = request.getParameter("status");
            JSONObject json = new JSONObject();
            DBConnection db = new DBConnection();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String timeStamp = simpleDateFormat.format(date);
                if (status.equals("Completed")) {
                //tool returned to service provider and payment done by farmer to service provider
                String hours = request.getParameter("hours");
                String amount = request.getParameter("amount");
                String return_on = request.getParameter("return_on");
                String cust_email = request.getParameter("cust_email");
                String sp_email = request.getParameter("sp_email");
                String sql = "UPDATE tbl_tool_requests set return_on='" + return_on + "', request_status='" + status + "',total_payment='" + amount + "',time_in_hour='" + hours + "' WHERE sr=" + id + "";
                int i = db.insert(sql);
                if (i > 0) {
//send message to farmer and service provider amout updation of valet amount
                    sql = "UPDATE tbl_valet_info SET valet_amount=valet_amount-" + amount + " WHERE user_id='" + cust_email + "'";
                    db.insert(sql);
                    sql = "UPDATE tbl_valet_info SET valet_amount=valet_amount+" + amount + " WHERE user_id='" + sp_email + "'";
                     db.insert(sql);
                     
                    json.put("success", 1);

                } else {
                    json.put("failed", 2);
                }
            } else if (status.equals("Delivered")) {
                String sql = "UPDATE tbl_tool_requests set request_status='" + status + "',delivered_on='" + timeStamp + "' WHERE sr=" + id + "";
                int i = db.insert(sql);
                if (i > 0) {
                    //tool delivered to farmer successfully..
                    json.put("success", 1);
                } else {
                    json.put("failed", 2);
                }

            } else if (status.equals("Return Requested")) {
                String returntime = request.getParameter("returntime");
                SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("dd/MM/yyyy");
                Date date1=new Date();
                String time=simpleDateFormat1.format(date1);
                time=time+" "+returntime;
                String sql = "UPDATE tbl_tool_requests set request_status='" + status + "',return_on='" + time + "' WHERE sr=" + id;
                int i = db.insert(sql);
                if (i > 0) {
                    //send message to service provider to pickup tool 
                    json.put("success", 1);
                } else {
                    json.put("failed", 2);
                }
            } else {
                String sql = "UPDATE tbl_tool_requests set request_status='" + status + "' WHERE sr=" + id + "";
                int i = db.insert(sql);
                if (i > 0) {
                    json.put("success", 1);
                } else {
                    json.put("failed", 2);
                }
            }
            response.setContentType("application/json");
            response.getWriter().write(json.toString());

        } catch (Exception r) {
            r.printStackTrace();
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
