package servlet;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import dao.GenreDAO;
import dao.MovieDAO;
import data.Genre;
import data.Movie;
import data.ShoppingCart;

public class BrowseByGenreServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		
        response.setContentType("text/css");    // Response mime type
        
        // Get login information with Session
        HttpSession session = request.getSession(true);
        String userName = (String)session.getAttribute("first_name");
        Integer userId = (Integer)session.getAttribute("user_id");
        ShoppingCart sc = new ShoppingCart();
        sc = (ShoppingCart) session.getAttribute("shopping_cart");
        if (userName != null) {
        	request.setAttribute("userName", userName);
        	request.setAttribute("userId", userId);
        	request.setAttribute("shoppingCart", sc);
        	session.setAttribute("shopping_cart", sc);
        }
        
        
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        
        int page = 1;
        int recordPerPage = 10;
        String genre = "Action";
        String sort = "TITLE";
        if (request.getParameter("page") != null) {
        	page = Integer.parseInt(request.getParameter("page"));
        }
        if (request.getParameter("genre") != null) {
        	genre = (String)request.getParameter("genre");
        }
        if (request.getParameter("sort") != null) {
        	sort = (String)request.getParameter("sort");
        }
        if (request.getParameter("rpp") != null) {
        	recordPerPage = Integer.parseInt(request.getParameter("rpp"));
        }
        MovieDAO movieDAO = new MovieDAO();
        GenreDAO genreDAO = new GenreDAO();
        List<Movie> movies = movieDAO.viewMoviesByGenre((page - 1) * recordPerPage, recordPerPage, genre, sort);
        List<Genre> genreList = genreDAO.getAllGenre();
        int noOfRecord = movieDAO.getNoOfRecords();
        int noOfPage = (int) Math.ceil(noOfRecord / (float)recordPerPage);
        request.setAttribute("genre", genre);
        request.setAttribute("sort", sort);
        request.setAttribute("rpp", recordPerPage);
        request.setAttribute("currentPage", page);
        request.setAttribute("noOfPage", noOfPage);
        request.setAttribute("movieList", movies);
        request.setAttribute("genreList", genreList);
        
    	request.getRequestDispatcher("WEB-INF/browse_by_genre.jsp").forward(request, response);

        out.close();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		doGet(request, response);
	}
	
}
