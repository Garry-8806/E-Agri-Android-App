/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import pack.DBConnection;

/**
 *
 * @author Dinesh
 */
public class ViewGarageBooking extends HttpServlet {

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
            String username = request.getParameter("username");

            String sql = "SELECT ttr.*,tui.username,tui.mobile FROM tbl_tool_requests ttr inner join tbl_user_reg tui on ttr.cust_email=tui.email WHERE ttr.request_status!='Not Viewed' and ttr.sp_email='" + username + "'";
            JSONObject json = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            DBConnection db = new DBConnection();
            ResultSet rs = db.selectData(sql);
            while (rs.next()) {

                JSONObject object = new JSONObject();
                object.put("username", rs.getString("username"));
                object.put("sp_email", rs.getString("sp_email"));
                object.put("tool_name", rs.getString("tool_name"));
                object.put("cust_email", rs.getString("cust_email"));
                object.put("request_on", rs.getString("request_on"));
                object.put("delivered_on", rs.getString("delivered_on"));
                object.put("return_on", rs.getString("return_on"));
                object.put("time_in_hour", rs.getString("time_in_hour"));
                object.put("total_payment", rs.getString("total_payment"));
                object.put("request_status", rs.getString("request_status"));
                object.put("cost_per_hour", rs.getString("cost_per_hour"));
                object.put("latitude", rs.getString("latitude"));
                object.put("longitude", rs.getString("longitude"));
                object.put("mobile", rs.getString("mobile"));
                object.put("id", rs.getString("sr"));

                jSONArray.put(object);

            }
            json.put("success", 1);
            json.put("BookingInfo", jSONArray);
            response.setContentType("application/json");
            response.getWriter().write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
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
