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
import org.json.JSONObject;
import pack.DBConnection;

/**
 *
 * @author Dinesh
 */
public class UpdateGarageServices extends HttpServlet {

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
            String service1 = request.getParameter("service1");
            String service2 = request.getParameter("service2");
            String service3 = request.getParameter("service3");
            String service4 = request.getParameter("service4");
            String service5 = request.getParameter("service5");
            JSONObject json = new JSONObject();
            String email = request.getParameter("email");
            String sql = "SELECT * FROM tbl_services WHERE email='" + email + "'";
            DBConnection db = new DBConnection();
            ResultSet rs = db.selectData(sql);
            if (rs.next()) {
                sql = "UPDATE tbl_services SET service1='" + service1 + "',service2='" + service2 + "',service3='" + service3 + "',service4='" + service4 + "',service5='" + service5 + "' WHERE email='" + email + "'";
                int i = db.insert(sql);
                json.put("success", 1);
            } else {
                sql = "INSERT INTO tbl_services VALUES(0,'" + email + "','" + service1 + "','" + service2 + "','" + service3 + "','" + service4 + "','" + service5 + "')";
                int i = db.insert(sql);
                if (i > 0) {
                    json.put("success", 1);
                } else {
                    json.put("failed", 0);
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
