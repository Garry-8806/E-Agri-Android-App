/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

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
 * @author PhoenixZone
 */
public class AddToolRequest extends HttpServlet {

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

            String email = request.getParameter("email");
            String sp_email = request.getParameter("sp_email");
            String tool_name = request.getParameter("tool_name");
            String latitude = request.getParameter("latitude");
            String longitude = request.getParameter("longitude");
            double cost_per_hour = Double.parseDouble(request.getParameter("cost"));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String timeStamp = simpleDateFormat.format(date);
            DBConnection db = new DBConnection();
            String sql = "SELECT * FROM tbl_tool_requests WHERE cust_email='" + email + "' and tool_name='" + tool_name + "' and request_status='Not Viewed'";
            ResultSet rs = db.selectData(sql);
            if (rs.next()) {
                json.put("message", "Tool Request Already placed..");

            } else {
                sql = "INSERT INTO tbl_tool_requests(tool_name,sp_email,cust_email,request_on,cost_per_hour,latitude,longitude) VALUES('" + tool_name + "','" + sp_email + "','" + email + "','" + timeStamp + "'," + cost_per_hour + ",'"+latitude+"','"+longitude+"')";

                int row_affected = db.insert(sql);
                if (row_affected > 0) {
                    json.put("success", "true");
                    json.put("message", "Tool Request sent..");

                } else {
                    json.put("success", "true");
                    json.put("message", "Tool Request failed..");
                }
            }
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
