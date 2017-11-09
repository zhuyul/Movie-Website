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

public class EmployeeLogin extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {

        response.setContentType("text/css");    // Response mime type
        HttpSession session = request.getSession(true);
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        
        // Get ReCaptcha response
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
//    	System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
    	// Verify CAPTCHA.
    	boolean valid = VerifyUtils.verify(gRecaptchaResponse);
    	if (!valid) {
    	    //errorString = "Captcha invalid!";
    		response.sendRedirect("../login_error.html");
    	    return;
    	}

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
            	String fullName = rs.getString("fullname");
            	session.setAttribute("fullname", fullName);
//            	String firstName = rs.getString("first_name");
//            	Integer userId = rs.getInt("ID");
//            	ShoppingCart sc = new ShoppingCart();
//            	session.setAttribute("first_name", firstName);
//            	session.setAttribute("user_id", userId);
//            	session.setAttribute("shopping_cart", sc);
                response.sendRedirect("../employee.jsp");
            } else {
            	response.sendRedirect("../login_error.html");
            }
            
//            ShoppingCart debug = (ShoppingCart) session.getAttribute("shopping_cart");

            rs.close();
            statement.close();
            dbcon.close();
          } catch (SQLException ex) {
            while (ex != null) {
                  System.out.println ("SQL Exception:  " + ex.getMessage ());
                  ex = ex.getNextException ();
              }  // end while
          } catch (java.lang.Exception ex) {
              out.println("<HTML>" +
                          "<HEAD><TITLE>" +
                          "MovieDB: Error" +
                          "</TITLE></HEAD>\n<BODY>" +
                          "<P>SQL error in doPost: " +
                          ex.getMessage() + "</P></BODY></HTML>");
              return;
          }
      out.close();
      }

}
