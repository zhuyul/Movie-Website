<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import = "java.util.List" %>
<%@page import = "java.util.Iterator" %>
<%@page import = "dao.SearchDAO" %>
<%@page import = "data.Movie" %>
 
<%
	SearchDAO dao = new SearchDAO();
	String query = request.getParameter("q");
	List<Movie> movies = dao.autoComplete(query);
	Iterator<Movie> iterator = movies.iterator();
	while(iterator.hasNext()){
		Movie movie = (Movie) iterator.next();
		out.println(movie.getTitle());
	}
	
%>
