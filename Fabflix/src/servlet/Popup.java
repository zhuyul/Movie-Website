package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.sql.*;

import dao.ConnectionFactory;
import dao.MovieDAO;
import data.Movie;
import data.Star;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.ArrayList;

public class Popup extends HttpServlet{
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    	
        response.setContentType("text/html;charset=UTF-8");
            	
        PrintWriter out = response.getWriter();
        
        try{
            Connection dbcon = ConnectionFactory.getInstance().getConnection();
            
            String movieId = request.getParameter("movieid");

            String movie_query = "SELECT * from movies WHERE id = ?";
            PreparedStatement movie_ps = dbcon.prepareStatement(movie_query);
            movie_ps.setInt(1, Integer.parseInt(movieId));
            ResultSet movie_rs = movie_ps.executeQuery();
            movie_rs.next();
            
            //banner, stars, release year, shopping cart button
            out.println("<table>");
            
            String bannerURL = movie_rs.getString("banner_url"); 
			if (bannerURL.equals("") || bannerURL == null)
				bannerURL = ""; 
			out.println("<tr>\n\t<td>Banner: </td>\n<td><img src=\"" + bannerURL +  "\" alt=\"banner\" width=50 height=50></td></tr>"); 

			out.println("<tr>\n\t<td>Movie: </td>\n<td>" + movie_rs.getString("title").trim() + "</td></tr>");
			out.println("<tr>\n\t<td>Release Year: </td>\n<td>" + Integer.toString(movie_rs.getInt("year")) + "</td>\n</tr>\n"); 
			
			MovieDAO dao = new MovieDAO();
			ArrayList<Star> StarList = dao.getStarList(movieId); 
			if (StarList.size() > 0)
				out.println("<tr>\n\t<td>Stars: </td>\n\t<td>\n\t<ul>\n"); 
			for (Star i : StarList)
				out.println("<li>" + i.getFirstName() + " " + i.getLastName() + "</li>");
			if (StarList.size() > 0)
				out.println("</ul></td>\n</tr>\n"); 
			
			out.println("<tr><td>");
			out.println("<form action='ShoppingCart' method = 'post' />");
			out.println("<input type='hidden' name='addItem' value='"+ movieId+"'/>" );
            out.println("<input class='form-button' type='submit' value='Add to cart'/><br/><br/>");
            out.println("</td></tr>");
            out.println("</table>");
            out.println("<button type='button' onclick='mouseout()'>Close</button>");
            
			movie_rs.close();
			movie_ps.close();
			dbcon.close();
			
        }catch (SQLException e) {
            while (e != null) {
                System.out.println ("SQL Exception: "+e.getMessage());
                e = e.getNextException ();
            }
        }

        catch(java.lang.Exception e){
            out.println("<p>Detail Not Found</p>"
            		+ "<button type='button' onclick='mouseout()'>Close</button>");
            return;
        }
    }

}
