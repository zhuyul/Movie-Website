package servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import reCaptcha.VerifyUtils;

public class Dashboard extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		HttpSession session = request.getSession(true);
		
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

		// If logged in as employee, show main page
		if (session.getAttribute("login_status") != null) {
			String loginStatus = (String)session.getAttribute("login_status");
			if (loginStatus.equals("employee")) {
				String action = "";
				if (request.getParameter("action") != null) {
					action = request.getParameter("action");					
				}
				if (action.equals("add_star")) {
					request.getRequestDispatcher("WEB-INF/add_star.jsp").include(request, response);
				} else if (action.equals("add_movie")) {
					request.getRequestDispatcher("WEB-INF/add_movie.jsp").include(request, response);
				} else if (action.equals("show_meta")) {
					request.getRequestDispatcher("WEB-INF/show_metadata.jsp").include(request, response);
				} else {
					request.getRequestDispatcher("WEB-INF/dashboard.jsp").forward(request, response);
				}
				
			} else {
				response.sendRedirect("login.html");
			}
		} else { 
			
			response.sendRedirect("login.html");
		}
		out.close();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		doGet(request, response);
	}
}
