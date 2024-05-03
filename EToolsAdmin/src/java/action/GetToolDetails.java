/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import pack.DBConnection;

/**
 *
 * @author PhoenixZone
 */
public class GetToolDetails extends HttpServlet {

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
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            String email = request.getParameter("email");
            String sql = "SELECT * FROM tbl_tools_info tt inner join tbl_user_reg tu on tt.email=tu.email";
            DBConnection db = new DBConnection();
            Connection con = db.con;

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                JSONObject object = new JSONObject();
                object.put("sr", rs.getString("sr"));
                object.put("title", rs.getString("title"));
                object.put("cost", rs.getString("cost"));
                object.put("description", rs.getString("description"));
                object.put("email", rs.getString("email"));
                object.put("image_name", rs.getString("image_name"));
                object.put("added_on", rs.getString("added_on"));
                object.put("status", rs.getString("tool_status"));
                object.put("spname", rs.getString("username"));
                array.put(object);
            }
            json.put("ToolsInfo", array);
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
