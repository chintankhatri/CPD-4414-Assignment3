package Connect;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author chintan 
 * 
 */
public class Connect {

    public static Connection getConnection() {

        Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            String jdbc = "jdbc:mysql://localhost/db";
            String user = "root";
            String pass = "";

            con = DriverManager.getConnection(jdbc, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Connection Error!"+ex.getMessage());
        }
        return con;
    }
}
