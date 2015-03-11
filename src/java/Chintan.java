
import Connect.Connect;

import com.mysql.jdbc.CharsetMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author chintan
 *
 *
 */
@Path("/question")
public class Chintan extends HttpServlet {

    @GET
    @Produces
    Response Get() {
        return Response.ok(getResult("select * from products")).build();

    }

    private String getResult(String query, String... parameters) {
        StringBuilder sb = new StringBuilder();
        JSONObject jObj = new JSONObject();

        try (Connection con = Connect.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(query);
            for (int i = 1; i <= parameters.length; i++) {
                pstmt.setString(i, parameters[i - 1]);
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                jObj.put("productid", rs.getInt("productid"));
                jObj.put("name", rs.getString("name"));
                jObj.put("description", rs.getString("description"));
                jObj.put("quantity", rs.getInt("quantity"));

                sb.append(jObj.toJSONString());
            }

        } catch (SQLException ex) {
            System.err.println("Error" + ex.getMessage());
        }

        return sb.toString();
    }

    /**
     * Provides POST /product?name=XXX&description=XXX&quantity=XXX
     *
     * @param request - the request object
     * @param response - the response object
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Set<String> keySet = request.getParameterMap().keySet();

        try (PrintWriter out = response.getWriter()) {
            if (keySet.contains("name") && keySet.contains("description") && keySet.contains("quantity")) {
                // There are some parameters                
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                String quantity = request.getParameter("quantity");
                doUpdate("INSERT INTO product (name,description,quantity) VALUES (?,?,?)", name, description, quantity);

            } else {
                // There are no parameters at all

                response.setStatus(500);
            }
        } catch (IOException ex) {
            Logger.getLogger(Chintan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int doUpdate(String query, String... params) {
        int numChanges = 0;
        try (Connection con = Connect.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            numChanges = pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Chintan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numChanges;
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        Set<String> keySet = request.getParameterMap().keySet();
        try (PrintWriter out = response.getWriter()) {
            if (keySet.contains("id") && keySet.contains("name") && keySet.contains("description") && keySet.contains("quantity")) {
                // There are some parameters
                String id = request.getParameter("id");
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                String quantity = request.getParameter("quantity");
                doUpdate("update product set name=?, description=?,quantity=? where productid=?", name, description, quantity, id);
            } else {
                // There are no parameters at all

                response.setStatus(500);
            }
        } catch (IOException ex) {
            Logger.getLogger(Chintan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        Set<String> keySet = request.getParameterMap().keySet();
        try (PrintWriter out = response.getWriter()) {
            if (keySet.contains("id")) {
                // There are some parameters
                String id = request.getParameter("id");

                doUpdate("delete from product where productid=?", id);
            } else {
                // There are no parameters at all

                response.setStatus(500);
            }
        } catch (IOException ex) {
            Logger.getLogger(Chintan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
