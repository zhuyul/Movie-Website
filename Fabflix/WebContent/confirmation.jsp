<%@page import="java.sql.*,
 javax.sql.*,
 java.io.IOException,
 javax.servlet.http.*,
 javax.servlet.*"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%	String userName = (String)session.getAttribute("first_name");
	Integer userId = (Integer)session.getAttribute("user_id");%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Fablix - Shopping Status</title>
		<link rel="stylesheet" type="text/css" href="home.css" />
    	<script type="text/javascript" 
				src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
		<script src="js/jQuery.autocomplete.js"></script>
	</head>
	<body>
		<div id="websitetitle">
			<h1><img src="image/movielogo.png" alt="logo" style="width:70px; height:70px;" >Fabflix</h1>
		</div>
	
		<nav>
			<div class="container">
				<ul class="navbar-left">
					<%	if (userName != null) {
						out.println("<li>Hello, " + userName + "</li>");
					}
					%>
				</ul>
				<ul class="navbar-middle">
					<li><a href="browseByGenre">Genre</a></li>
					<li><a href="browseByTitle">Title</a></li>
					<li><a href="search.jsp">Search</a></li>
					<li>
						<FORM action="search" method="get" class = "search-bar" align = "center">
							<input type = "text" id="title" name = "title">
							<script>
								$("#title").autocomplete("getData.jsp");
							</script>
							<input type = "text" name = "year" hidden>
							<input type = "text" name = "director" hidden>
							<input type = "text" name = "fname" hidden>
							<input type = "text" name = "lname" hidden>
							
							<INPUT type = "submit" value = "Search" id = "search">
						</FORM>
					</li>
				</ul>
			
				<ul class="navbar-right">
					<li><a href="#">Check Out</a></li>
					<%	if (userName != null) {
						out.println("<li><a href='servlet/Logout'>Log Out</a></li>");
					} else {
						out.println("<li><a href='login.html'>Log In</a></li>");
					} %>
		    	</ul> 
			</div>
		</nav>
		
		
		<%
			String message = (String)session.getAttribute("return_message");
		%>
		<h2><center><%=message%></center><h2>
		<a href="browseByGenre"><h7><center>Home</center></h7></a>
		
	</body>
</html>