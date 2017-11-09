package servlet;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import dao.ConnectionFactory;
import data.ShoppingCart;
import reCaptcha.VerifyUtils;

public class Login extends HttpServlet {
	
	private static final long serialVersionUID = 5014489099772037255L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {

        response.setContentType("text/css");    // Response mime type
        HttpSession session = request.getSession(true);
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        
        // Get ReCaptcha response
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
    	// Verify CAPTCHA.
    	boolean valid = VerifyUtils.verify(gRecaptchaResponse);
    	if (!valid) {
    		response.sendRedirect("../login_error.html");
    	    return;
    	}
    	
    	String loginStatus = request.getParameter("login_status");

    	if (loginStatus.equals("customer")) {
	        try {
	        	Connection dbcon = ConnectionFactory.getInstance().getConnection();
	        	// Declare our statement
				Statement statement = dbcon.createStatement();
				
				String email = request.getParameter("eAddress");
				String password = request.getParameter("password");
				String query = "SELECT * from customers where email = '" + email + "' and password = '"
				+ password + "'";
	
				// Perform the query
				ResultSet rs = statement.executeQuery(query);
	            
	            boolean status = rs.next();
	            if (status) {
	            	String firstName = rs.getString("first_name");
	            	Integer userId = rs.getInt("ID");
	            	ShoppingCart sc = new ShoppingCart();
	            	session.setAttribute("login_status", loginStatus);
	            	session.setAttribute("first_name", firstName);
	            	session.setAttribute("user_id", userId);
	            	session.setAttribute("shopping_cart", sc);
	                response.sendRedirect("../browseByGenre");
	            } else {
	            	response.sendRedirect("../login_error.html");
	            }
	            	
	            rs.close();
	            statement.close();
	            dbcon.close();
	        } catch (SQLException | ClassNotFoundException e) {
	    		e.printStackTrace();
    		}
    	} else if (loginStatus.equals("employee")) {
    		try {
	        	Connection dbcon = ConnectionFactory.getInstance().getConnection();
	        	// Declare our statement
				Statement statement = dbcon.createStatement();
				
				String email = request.getParameter("eAddress");
				String password = request.getParameter("password");
				String query = "SELECT * from employees where email = '" + email + "' and password = '"
				+ password + "'";

				// Perform the query
				ResultSet rs = statement.executeQuery(query);
	            
	            boolean status = rs.next();
	            if (status) {
	            	session.setAttribute("login_status", loginStatus);
	            	session.setAttribute("employee_name", rs.getString("fullname"));
	            	response.sendRedirect("../_dashboard");
	            } else {
	            	response.sendRedirect("../login_error.html");
	            }
	            
	            rs.close();
	            statement.close();
	            dbcon.close();
	    	} catch (SQLException | ClassNotFoundException e) {
	    		e.printStackTrace();
    		}
    	} else {
    		System.out.print("login status error: " + loginStatus);
    		response.sendRedirect("error_page");
    	}
      out.close();
      }

}
