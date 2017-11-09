<%@page import="java.sql.*,
 javax.sql.*,
 java.io.IOException,
 javax.servlet.http.*,
 javax.servlet.*"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	if (session.getAttribute("first_name") == null) {
		response.sendRedirect("login.html");
	}
	String userName = (String)session.getAttribute("first_name");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="home.css" />
	<title>Checkout</title>
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
				<li>Hello, <%=userName %></li>
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
				<li><a href="servlet/Logout">Log Out</a></li>
	    	</ul> 
		</div>
	</nav>
	
	<center><h3>Checkout</h3></center>
	
	<c:choose>
		<c:when test="${empty sessionScope.shopping_cart || sessionScope.shopping_cart.getTotal() == 0}">
			<h3><center>Your cart is empty.</center></h3>
			<a href="browseByGenre"><h7><center>Continue To Browse</center></h7></a>
		</c:when>
		<c:otherwise>
			<h3 align="center">Enter Credit Card Information:</h3>
			<form action="checkout" method="post" align="center">
				First Name: <input type="text" name="first_name" required />
				<br>
				Last Name: <input type="text" name="last_name" required />
				<br>
				Credit Card Number: <input type="text" name="creditcard" required />
				<br>
				Expiration Date: <input type="text" name="expiration" placeholder="yyyy-mm-dd" required/>
				<br>
				<br>
				<input type="submit" name="Check Out">
			</form>
		</c:otherwise>
	</c:choose>
	
</body>
</html>





