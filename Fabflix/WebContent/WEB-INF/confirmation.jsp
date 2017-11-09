<%@page import="java.sql.*,
 javax.sql.*,
 java.io.IOException,
 javax.servlet.http.*,
 javax.servlet.*,
 java.util.HashMap"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="home.css" />
	<title> Confirmation </title>
</head>
<body>
	<div id="websitetitle">
		<h1><img src="image/movielogo.png" alt="logo" style="width:70px; height:70px;" >Fabflix</h1>
	</div>

	<nav>
		<div class="container">
			<ul class="navbar-left">
				<c:if test="${not empty userName}">
					<li>Hello, ${userName}</li>
				</c:if>
			</ul>
			<ul class="navbar-middle">
				<li><a href="browseByGenre">Genre</a></li>
				<li><a href="browseByTitle">Title</a></li>
				<li><a href="search.jsp">Search</a></li>
			</ul>
		
			<ul class="navbar-right">
				<li><a href="checkout.html">Check Out</a></li>
				<c:if test="${not empty userName}">
					<li><a href="servlet/Logout">Log Out</a></li>
				</c:if>
				<c:if test="${empty userName}">
					<li><a href="login.html">Log In</a></li>
				</c:if>
	    	</ul> 
		</div>
	</nav>
	
	<center><h3>Confirmation</h3></center>
	
	<%
	int check = (Integer)session.getAttribute("validate_id");
	if(check == -1){
	%>
	<a href="checkout">Wrong Input! Please Re-enter Card Information</a>
	<% }
	else{ %>
	<p>Thank you for submitting order!</p>
	<p>Your order is recorded in our database and will be processed soon.</p>
	
	<% }%>
	
	
	

</body>
</html>


















