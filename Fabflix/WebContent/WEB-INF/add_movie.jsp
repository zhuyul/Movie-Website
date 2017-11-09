<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Dashboard - Add movie</title>
   	<link rel="stylesheet" type="text/css" href="home.css" />
</head>
<body>
	<%
		String loginStatus = (String)session.getAttribute("login_status");
		if (!loginStatus.equals("employee")){
			response.sendRedirect(request.getContextPath()+"/login.html");
		}
		String employeeName = (String)session.getAttribute("employee_name");
	%>
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
	<h1 align = "center"> Add a Movie</h1>
   	<DIV align="center">
		<FORM action="_add_movie" method="post" align = "center">
			<table align="center">
				<tbody>
					<tr>
						<td><strong>Movie Info:</strong></td>
					</tr>
					<tr>
						<td>Title*:</td>
						<td><input type = "text" name = "title" required></td>
					</tr>
					<tr>
						<td>Year*:</td>
						<td><input type = "text" name = "year" required></td>
					</tr>
					<tr>
						<td>Director*:</td>
						<td><input type = "text" name = "director" required></td>
					</tr>
					<tr>
						<td>Banner URL:</td>
						<td><input type = "text" name = "banner_url"></td>
					</tr>
					<tr>
						<td>Trailer URL:</td>
						<td><input type = "text" name = "trailer_url"></td>
					</tr>
					<tr>
						<td><strong>Star Info(Optional):</strong></td>
					</tr>
					<tr>
						<td>First Name:</td>
						<td><input type="text" name="first_name"></td>
					</tr>
					<tr>
						<td>Last Name*:</td>
						<td><input type="text" name="last_name"></td>
					</tr>
					<tr>
						<td>Date of birth:</td>
						<td><input type = "text" name = "dob" placeholder="yyyy-mm-dd"></td>
					</tr>
					<tr>
						<td>Photo URL:</td>
						<td><input type = "text" name = "purl"></td>
					</tr>
					<tr>
						<td><strong>Genre Info(Optional):</strong></td>
					</tr>
					<tr>
						<td>Genre Name*:</td>
						<td><input type="text" name="genre"></td>
					</tr>
					<tr>
						<td align="right"><INPUT type = "submit" value = "Add" id = "add"></td>
					</tr>
				</tbody>
			</table>
		</FORM>
	</DIV>
	
	<c:if test="${not empty returnMessage}">
		<DIV align="center">
			<h3><strong>${returnMessage}</strong></h3>
		</DIV>
	</c:if>
</body>
</html>