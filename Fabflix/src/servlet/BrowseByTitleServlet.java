package servlet;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import dao.MovieDAO;
import data.Movie;

public class BrowseByTitleServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		
        response.setContentType("text/css");    // Response mime type
        
        // Get login information with Session
        HttpSession session = request.getSession(true);
        String userName = (String)session.getAttribute("first_name");
        Integer userId = (Integer)session.getAttribute("user_id");
        if (userName != null) {
        	request.setAttribute("userName", userName);
        	request.setAttribute("userId", userId);
        }
        
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        
        int page = 1;
        int recordPerPage = 10;
        String initial = "A";
        String sort = "TITLE";
        if (request.getParameter("page") != null) {
        	page = Integer.parseInt(request.getParameter("page"));
        }
        if (request.getParameter("initial") != null) {
        	initial = (String)request.getParameter("initial");
        }
        if (request.getParameter("sort") != null) {
        	sort = (String)request.getParameter("sort");
        }
        if (request.getParameter("rpp") != null) {
        	recordPerPage = Integer.parseInt(request.getParameter("rpp"));
        }
        MovieDAO movieDAO = new MovieDAO();
        List<Movie> movies = movieDAO.viewMoviesByTitle((page - 1) * recordPerPage, recordPerPage, initial, sort);
        int noOfRecord = movieDAO.getNoOfRecords();
        int noOfPage = (int) Math.ceil(noOfRecord / (float)recordPerPage);
        request.setAttribute("initial", initial);
        request.setAttribute("rpp", recordPerPage);
        request.setAttribute("sort", sort);
        request.setAttribute("currentPage", page);
        request.setAttribute("noOfPage", noOfPage);
        request.setAttribute("movieList", movies);
        
    	request.getRequestDispatcher("WEB-INF/browse_by_title.jsp").forward(request, response);

        out.close();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		doGet(request, response);
	}
	
}
