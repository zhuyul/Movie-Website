<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%	String userName = (String)session.getAttribute("first_name");
	Integer userId = (Integer)session.getAttribute("user_id"); %>
<!DOCTYPE html>
<HTML>
	<HEAD>
    	<title>Fabflix - Search</title>
	<link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css" />
    	<link rel="stylesheet" type="text/css" href="home.css" />
    	<script type="text/javascript" 
				src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
		<script src="js/jQuery.autocomplete.js"></script>  
	</HEAD>
	<BODY>
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
					<li><a href="ShoppingCart">Check Out</a></li>
					<%	if (userName != null) {
						out.println("<li><a href='servlet/Logout'>Log Out</a></li>");
					} else {
						out.println("<li><a href='login.html'>Log In</a></li>");
					} %>
		    	</ul> 
			</div>
		</nav>
		
		<h1 align = "center"> Search for movies</h1>
    	<DIV align="center">
			<FORM action="search" method="get" class = "search-bar" align = "center">
				<table align="center">
					<tbody>
						<tr>
							<td>Title:</td>
							<td><input type = "text" name = "title"></td>
						</tr>
						<tr>
							<td>Year:</td>
							<td><input type = "text" name = "year"></td>
						</tr>
						<tr>
							<td>Director:</td>
							<td><input type = "text" name = "director"></td>
						</tr>
						<tr>
							<td>Star's First Name:</td>
							<td><input type = "text" name = "fname"></td>
						</tr>
						<tr>
							<td>Star's Last Name:</td>
							<td><input type = "text" name = "lname"></td>
						</tr>
						<tr>
							<td align="right"><INPUT type = "submit" value = "Search" id = "search"></td>
						</tr>
					</tbody>
				</table>
			</FORM>
        </DIV>
	</BODY>
</HTML>