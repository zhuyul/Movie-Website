package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ConnectionFactory;
import data.ShoppingCart;

public class AddStar extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		HttpSession session = request.getSession(true);
		
		if (session.getAttribute("login_status") != null) {
			String loginStatus = (String)session.getAttribute("login_status");
			if (!loginStatus.equals("employee")) {
				response.sendRedirect("login.html");
			}
		}
		
		String returnMessage = "";
		try {
        	Connection dbcon = ConnectionFactory.getInstance().getConnection();
        	// Declare our statement
        	Statement insert = dbcon.createStatement();
        	String firstName, lastName, dob, photo_url;
        	
        	String notRequiredAttributes = "";
        	String notRequiredEntries = "";
        	
        	firstName = request.getParameter("fname");
    		lastName = request.getParameter("lname");
    		dob = request.getParameter("dob");
    		photo_url = request.getParameter("purl");

        	if (!dob.equals("")) {
        		notRequiredAttributes += ", dob";
        		notRequiredEntries += String.format(", \"%s\"", dob);
        	}
        	if (!photo_url.equals("")) {
        		notRequiredAttributes += ", photo_url";
        		notRequiredEntries += String.format(", \"%s\"", photo_url);
        	}

        	String queryString = String.format("insert into stars (first_name, last_name%s) values (\"%s\", \"%s\"%s)", notRequiredAttributes, firstName, lastName, notRequiredEntries);

        	int rowUpdated = insert.executeUpdate(queryString);
        	if (rowUpdated != 0) {
        		returnMessage = "Successfully add a star";
        	} else {
        		returnMessage = "Unable to add a star";
        	}
        	
        	insert.close();
            dbcon.close();
        } catch (ClassNotFoundException e) {
    		e.printStackTrace();
		} catch (SQLException e) {
			// add SQL Exception message to returnMessage
			returnMessage = "Your input caused a SQL Exception: " + e.getMessage();
		}
		request.setAttribute("returnMessage", returnMessage);
		request.getRequestDispatcher("WEB-INF/add_star.jsp").forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		doGet(request, response);
	}
}
