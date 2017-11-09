<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%	String fullName = (String)session.getAttribute("fullname");%>
<!DOCTYPE html>
<HTML>
	<HEAD>
    	<title>Fabflix - Employee</title>
    	<link rel="stylesheet" type="text/css" href="home.css" />
	</HEAD>
	<BODY>
		<div id="websitetitle">
			<h1><img src="image/movielogo.png" alt="logo" style="width:70px; height:70px;" >Fabflix</h1>
		</div>
	
		<nav>
			<div class="container">
				<ul class="navbar-left">
					<%	if (fullName != null) {
						out.println("<li>Hello, " + fullName + "</li>");
					}
					%>
				</ul>
				<ul class="navbar-middle">
					<li><a href="AddStar.jsp">Add Star</a></li>
					<li><a href="add_movie.jsp">Add Movie</a></li>
					<li><a href="ShowMetadata.jsp">Show Metadata</a></li>
				</ul>
			
				<ul class="navbar-right">
					<%	if (fullName != null) {
						out.println("<li><a href='servlet/Logout'>Log Out</a></li>");
					} else {
						out.println("<li><a href='_dashboard.html'>Log In</a></li>");
					} %>
		    	</ul> 
			</div>
		</nav>
		
	</BODY>
</HTML>