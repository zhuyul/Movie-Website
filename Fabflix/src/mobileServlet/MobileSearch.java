package mobileServlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import dao.SearchDAO;
import data.Movie;

public class MobileSearch extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		JSONObject jsonObject = new JSONObject();
		
		String title = request.getParameter("title");
		SearchDAO dao = new SearchDAO();
		List<Movie> movies = dao.mobileSearch(title);
		
		List<String> titles = new ArrayList<>();
		for (Movie movie : movies) {
			titles.add(movie.getTitle());
		}
		
		jsonObject.put("titles", titles);
        response.getWriter().write(jsonObject.toString());

	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		doGet(request, response);
	}
}
