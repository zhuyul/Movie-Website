<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%	String employeeName = (String)session.getAttribute("employee_name");%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
	<HEAD>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    	<title>Fabflix - Employee Dashboard</title>
    	<link rel="stylesheet" type="text/css" href="home.css" />
	</HEAD>
	<BODY>
		<div id="websitetitle">
			<h1><img src="image/movielogo.png" alt="logo" style="width:70px; height:70px;" >Fabflix</h1>
		</div>
	
		<nav>
			<div class="container">
				<ul class="navbar-left">
					<%	if (employeeName != null) {
						out.println("<li><a href='_dashboard'>Hello, " + employeeName + "</a></li>");
					}
					%>
				</ul>
				<ul class="navbar-middle">
					<li><a href="_dashboard?action=add_star">Add Star</a></li>
					<li><a href="_dashboard?action=add_movie">Add Movie</a></li>
					<li><a href="_show_meta">Show Metadata</a></li>
				</ul>
			
				<ul class="navbar-right">
					<%	if (employeeName != null) {
						out.println("<li><a href='servlet/Logout'>Log Out</a></li>");
					} else {
						out.println("<li><a href='_dashboard'>Log In</a></li>");
					} %>
		    	</ul> 
			</div>
		</nav>
		
		<div align="center">
			<h2><a href="_dashboard?action=add_star">Add a Star</a></h2>
			<h2><a href="_dashboard?action=add_movie">Add a Movie</a></h2>
			<h2><a href="_show_meta">Show Metadata</a></h2>
		</div>
		
	</BODY>
</HTML>