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

import java.util.ArrayDeque;
import java.util.HashMap;

//@WebServlet(urlPatterns = {"/moviepage"})
public class Moviepage extends HttpServlet{
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(true);
        String userName = (String)session.getAttribute("first_name");
    	Integer userId = (Integer)session.getAttribute("user_id");
    	request.setAttribute("userName", userName);
    	request.setAttribute("userId", userId);
    	
        PrintWriter out = response.getWriter();
        try{
            
            /* Including .css file and setting the title */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title> Movies </title>");
            out.println("<style>");
            out.println("body{font: 22px Arial, sans-serif;}");
            out.println("#websitetitle{text-align: center;}");
            out.println("nav{position: relative; padding: 0px 0 10px 0; background:  #696969; font-size: 17px; font-family: sans-serif; text-align: center;}");
            out.println("nav ul li {position: auto; display: inline-block; padding-left: 10px;}");
            out.println("nav ul li a {  color: #ffffff;  text-decoration: none;}");
            out.println(".container {margin: auto;width: 90%;}");
            out.println("nav .navbar-left{ position: absolute;  left: 0;  right: 0;  top: 0;  width: 10%;  color: #ffffff;}");
            out.println("nav .navbar-middle {  padding-top: 18px;  margin: 0 auto;  width: 60%;}");
            out.println("nav .navbar-right {  position: absolute;  right: 0;  top: 0;  width: 20%;}");
            out.println("nav .navbar-middle li{  margin-left: 0 auto;  margin-right: 0 auto;  width: 15%;  text-align: left;}");
            out.println("nav .navbar-left li{  white-space: nowrap;  margin-left: 0 auto;  margin-right: 0 auto;  width: 50%;  text-align: left;}");
            out.println("nav .navbar-right li {	white-space: nowrap;}");
            out.println(".productimage{margin-top: 5%; background-color:#FFFFFF; width:200px; float:left; text-align:center;}");
            out.println(".productdescription{margin-left: 30%; background-color:#FFFFFF; float:center;}");
            out.println(".fontproductname{font-size:25px; font-weight:bold;}");
            out.println(".fontproductdescription{font-size:15px;}");
            out.println("</style>");
            out.println("</head>");
            
            out.println("<body>");

            Connection dbcon = ConnectionFactory.getInstance().getConnection();
            
            out.println("<div id='websitetitle'>");
            out.println("<h1><img src='http://clipartsign.com/upload/2016/02/24/movie-reel-film-reel-clip-art-2.png' alt='logo' style='width:70px; height:70px;'>Fabflix</h1>");
    		out.println("</div>");
            
    		out.println("<nav>");
    		out.println("<div class='container'>");
	    		out.println("<ul class='navbar-left'>");
	    		if (userName != null) {
					out.println("<li>Hello, " + userName + "</li>");
				}
	    		out.println("</ul>");
    		
	    		out.println("<ul class='navbar-middle'>");
		    		out.println("<li><a href='browseByGenre'>Genre</a></li>");
		    		out.println("<li><a href='browseByTitle'>Title</a></li>");
		    		out.println("<li><a href='search.jsp'>Search</a></li>");
	    		out.println("</ul>");

	    		out.println("<ul class='navbar-right'>");
	    			out.println("<li><a href='ShoppingCart'>Check Out</a></li>");
	    			if (userName != null) {
						out.println("<li><a href='servlet/Logout'>Log Out</a></li>");
					} else {
						out.println("<li><a href='login.html'>Log In</a></li>");
					}
	    		out.println("</ul>");
    		
    		out.println("</div>");
    		out.println("</nav>");

            
            String movieId = request.getParameter("movieid");
            
            String movie_query = "SELECT * from movies WHERE id = ?";
            PreparedStatement movie_ps = dbcon.prepareStatement(movie_query);
            movie_ps.setString(1, movieId);
            ResultSet movie_rs = movie_ps.executeQuery();
            
            String movie = "";
            
            while (movie_rs.next()){
            	String movies_id = movie_rs.getString("id");
                String title = movie_rs.getString("title");
                movie = title;
                String year = movie_rs.getString("year");
                String director = movie_rs.getString("director");
                String banner = movie_rs.getString("banner_url");
                String trailer = movie_rs.getString("trailer_url");
                
                out.println("<div>");
                out.println("<div class = 'productimage'>");
                out.print("<br>");
                out.println("<img src= \""+banner+"\" alt=\"" +title+" Banner\" " + "width =\"128\" heigth=\"128\"> <br></br>" );
                out.print("<br>");
                out.println("</div>");
                
                out.println("<div class = 'productdescription'>");
                
                out.println("<div class = 'fontproductname'>");
                out.print("<br>");
                out.print(title);
                out.print("<br>");
                out.println("</div>");
                
                out.println("<div class='fontproductdescription'>");
                out.println("<b><font color = '696969'>Movie ID: </font></b>" + movies_id);
                out.println("<br>");
                out.println("<b><font color = '696969'>Director: </font></b>" + director);
                out.println("<br>");
                out.println("<b><font color = '696969'>Year: </font></b>" + year);
                out.println("<br>");
                out.println("<b><a href = \""+ trailer +"\" style='color: #696969'> Watch Trailer </a></b>");
                out.println("</div>");
            }
                        
            String genres_query ="SELECT name FROM (genres JOIN genres_in_movies ON genres.id = genres_in_movies.genre_id) WHERE genres_in_movies.movie_id= ?";
            PreparedStatement genres_ps = dbcon.prepareStatement(genres_query);
            genres_ps.setString(1, movieId);
            ResultSet genres_rs = genres_ps.executeQuery();
            
            out.println("<div class='fontproductdescription'>");
            out.println("<b><font color = '696969'>Genres: </font></b>");
            
            while (genres_rs.next()){
                String genresName = genres_rs.getString("name");
                out.println(genresName);
                out.println("&nbsp;");
            }
            out.println("<br>");
            out.println("</div>");
            
            String stars_query = "SELECT first_name, last_name, stars.id FROM (movies JOIN stars_in_movies NATURAL JOIN stars ON movies.id = stars_in_movies.movie_id AND stars_in_movies.star_id = stars.id) WHERE movies.id = ?";
            PreparedStatement stars_ps = dbcon.prepareStatement(stars_query);
            stars_ps.setString(1, movieId);
            ResultSet stars_rs = stars_ps.executeQuery();
            
            out.println("<div class='fontproductdescription'>");
            out.println("<b><font color = '696969'>Stars in Movie: </font></b>");
            out.println("<br>");
            
            while (stars_rs.next()){
                String star_id = stars_rs.getString("stars.id");
                String star_first_name = stars_rs.getString("first_name");
                String star_last_name = stars_rs.getString("last_name");
                out.println(star_id + "&nbsp;" + star_first_name + "&nbsp;" + star_last_name + "&nbsp;" + "<a href = \"starpage?starid="+ star_id +"\"> Link </a>");
                out.println("<br>");
            }
            out.println("<br>");
            
            out.println("<form action='ShoppingCart' method = 'post' />");
            out.println("<input type='hidden' name='addItem' value='"+ movieId+"'/>" );
            
            out.println("<input class='form-button' type='submit' value='Add to cart'/><br/><br/>");

            
            out.println("</div>");
            out.println("</div>");
            
            
            genres_rs.close();
            stars_rs.close();
            movie_rs.close();
            dbcon.close();
            
            out.println("</body>");
            out.println("</html>");

            
            
        }catch (SQLException e) {
            while (e != null) {
                System.out.println ("SQL Exception: "+e.getMessage());
                e = e.getNextException ();
            }
        }

        catch(java.lang.Exception e){
            out.println("<HTML>" + "<HEAD><TITLE>" + "Movie: Error" + "</TITLE></HEAD>\n<BODY>" +"<p>SQL error in doGet: " + e.getMessage() + "</p></BODY></HTML>");
            return;
        }
    }
}
