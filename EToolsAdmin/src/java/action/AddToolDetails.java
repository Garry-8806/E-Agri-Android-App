/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import pack.DBConnection;

/**
 *
 * @author PhoenixZone
 */
public class AddToolDetails extends HttpServlet {

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
            String cost = request.getParameter("cost");
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String email = request.getParameter("id");

            String imageDataString = request.getParameter("imageData");
            byte[] imageByteArray = decodeImage(imageDataString);
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            String timeStamp = format.format(cal.getTime());
            String savefile = "" + System.currentTimeMillis() + ".jpg";
            String path = request.getSession().getServletContext().getRealPath("/");
            String patt = path.replace("\\build", "");

            FileOutputStream imageOutFile = new FileOutputStream(patt + "\\posts\\" + savefile);
            imageOutFile.write(imageByteArray);
            imageOutFile.close();
            DBConnection db = new DBConnection();
            Connection con = db.con;
            Statement st = con.createStatement();
            JSONObject json = new JSONObject();

            String sql = "SELECT * FROM tbl_tools_info WHERE title='" + title + "' and email='" + email + "'";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                json.put("success", "true");
                json.put("message", "Tool already added");
            } else {
                sql="INSERT INTO tbl_tools_info(title,cost,description,email,image_name,added_on) VALUES('"+title+"','"+cost+"','"+description+"','"+email+"','"+savefile+"','"+timeStamp+"')";
                int row_affected = st.executeUpdate(sql);
                if (row_affected > 0) {
                    json.put("success", "true");
                    json.put("message", "Tool added successfully..");
                }else
                {
                    json.put("success", "true");
                    json.put("message","Error");
                }
            }
            con.close();
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

    public static byte[] decodeImage(String imageDataString) {
        byte[] data = Base64.decodeBase64(imageDataString.getBytes());
        return data;
    }
}
