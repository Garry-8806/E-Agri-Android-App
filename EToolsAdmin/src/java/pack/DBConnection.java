/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pack;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 *
 * @author Dell
 */
public class DBConnection {

    public Connection con;
    public Statement st;

    public DBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/etools", "root", "root");
            st = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int insert(String query) {
        int rowAffected = 0;
        try {
            rowAffected = st.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowAffected;
    }

    public ResultSet selectData(String query) {
        ResultSet rs = null;
        try {
            rs = st.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

}
