package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import dao.SearchDAO;
import data.Movie;

public class BrowseBySearchServlet extends HttpServlet {
		
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {

        response.setContentType("text/css");    // Response mime type
        HttpSession session = request.getSession(true);
        String userName = (String)session.getAttribute("first_name");
    	Integer userId = (Integer)session.getAttribute("user_id");
    	request.setAttribute("userName", userName);
    	request.setAttribute("userId", userId);

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        
        int page = 1;
        int recordPerPage = 10;
        String sort = "TITLE";
        if (request.getParameter("page") != null) {
        	page = Integer.parseInt(request.getParameter("page"));
        }
        if (request.getParameter("sort") != null) {
        	sort = (String)request.getParameter("sort");
        }
        if (request.getParameter("rpp") != null) {
        	recordPerPage = Integer.parseInt(request.getParameter("rpp"));
        }
        
        SearchDAO dao = new SearchDAO();
        String title = request.getParameter("title");
		String year = request.getParameter("year");
		String director = request.getParameter("director");
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		
		String requestParameter = String.format("title=%s&year=%s&director=%s&fname=%s&lname=%s", title, year, director, fname, lname);
		
        List<Movie> movies = dao.searchMovie(title, year, director, fname, lname, (page - 1) * recordPerPage, recordPerPage, sort);
        int noOfRecord = dao.getNoOfRecords();
        int noOfPage = (int) Math.ceil(noOfRecord / (float)recordPerPage);
        request.setAttribute("parameters", requestParameter);
        request.setAttribute("sort", sort);
        request.setAttribute("rpp", recordPerPage);
        request.setAttribute("currentPage", page);
        request.setAttribute("noOfPage", noOfPage);
        request.setAttribute("movieList", movies);
        
    	request.getRequestDispatcher("WEB-INF/browse_by_search.jsp").forward(request, response);

        out.close();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		doGet(request, response);
	}
	

	
}
