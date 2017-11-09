package servlet;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ConnectionFactory;

public class AddMovie extends HttpServlet {

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
        	String title, year, director, banner_url, trailer_url, firstName, lastName, dob, photo_url, genre;
        	
        	title = request.getParameter("title");
        	year = request.getParameter("year");
        	director = request.getParameter("director");
        	banner_url = request.getParameter("banner_url");
        	trailer_url = request.getParameter("trailer_url");
        	firstName = request.getParameter("first_name");
    		lastName = request.getParameter("last_name");
    		dob = request.getParameter("dob");
    		photo_url = request.getParameter("purl");
    		genre = request.getParameter("genre");
    		
    		CallableStatement insert = dbcon.prepareCall("{CALL moviedb.add_movie(?,?,?,?,?,?,?,?,?,?,?)}");
        	
        	insert.setString(1, title);
        	insert.setInt(2, Integer.parseInt(year));
        	insert.setString(3, director);
        	insert.setString(4, banner_url);
        	insert.setString(5, trailer_url);
        	insert.setString(6, firstName);
        	insert.setString(7, lastName);
        	insert.setString(8, dob);
        	insert.setString(9, photo_url);
        	insert.setString(10, genre);
    		insert.registerOutParameter(11, Types.VARCHAR);

    		insert.execute();

    		returnMessage = insert.getString(11);

        	insert.close();
            dbcon.close();
        } catch (ClassNotFoundException e) {
    		e.printStackTrace();
		} catch (SQLException e) {
			// add SQL Exception message to returnMessage
			returnMessage = "Your input caused a SQL Exception: " + e.getMessage();
			e.printStackTrace();
		}
		request.setAttribute("returnMessage", returnMessage);
		request.getRequestDispatcher("WEB-INF/add_movie.jsp").forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		doGet(request, response);
	}
}
