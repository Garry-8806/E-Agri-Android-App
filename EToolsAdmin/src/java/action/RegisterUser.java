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
import org.json.JSONArray;
import org.json.JSONObject;
import pack.DBConnection;

/**
 *
 * @author SDinesh
 */
public class RegisterUser extends HttpServlet {

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
        try  {
            /* TODO output your page here. You may use following sample code. */
            PrintWriter out = response.getWriter();
            String username = request.getParameter("name");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String mobile = request.getParameter("mobile");
            String address = request.getParameter("address");
            String utype=request.getParameter("utype");
            DBConnection db = new DBConnection();
            String query = "SELECT *FROM tbl_user_reg WHERE email='" + email + "'";
            JSONObject json = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            ResultSet rs = db.selectData(query);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            Date date = new Date();
            String timeStamp = format.format(date);
            if (rs.next()) {
                json.put("failed", 1);
            } else {
                query = "INSERT INTO tbl_user_reg VALUES('" + username + "','" + address + "','" + mobile + "','" + email + "','" + password + "','','','"+utype+"','" + timeStamp + "','Active',0)";
                int rowCount = db.insert(query);
                if (rowCount > 0) {
                    query="DELETE FROM tbl_valet_info WHERE user_id='"+email+"'";
                    db.insert(query);
                    query = "INSERT INTO tbl_valet_info VALUES(0,'" + email + "','1000')";
                    db.insert(query);
                    json.put("success", 1);
                } else {
                    json.put("failed", 1);
                }
            }
            System.out.println(email);
            response.setContentType("application/json");
            response.getWriter().write(json.toString());
        }
        catch(Exception e)
        {
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
