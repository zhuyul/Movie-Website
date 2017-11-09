<%@page import="java.sql.*,
 javax.sql.*,
 java.io.IOException,
 javax.servlet.http.*,
 javax.servlet.*"
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%	String userName = (String)session.getAttribute("first_name");
	Integer userId = (Integer)session.getAttribute("user_id");%>
<!DOCTYPE html>
<HTML>
	<HEAD>
    	<title>Fabflix - Search</title>
    	<link rel="stylesheet" type="text/css" href="home.css" />
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
				</ul>
			
				<ul class="navbar-right">
					<li><a href="checkout.jsp">Check Out</a></li>
					<%	if (userName != null) {
						out.println("<li><a href='servlet/Logout'>Log Out</a></li>");
					} else {
						out.println("<li><a href='login.html'>Log In</a></li>");
					} %>
		    	</ul> 
			</div>
		</nav>
		
		<h2 align="center">Shopping Cart</h2>
		
		<table align="center">
			<tbody>
				<tr>
					<td>Item</td>
					<td>Quantity</td>
				</tr>

				<c:forEach var="item" items="${shoppingCart.items}">
					<tr>
						<td><a href="moviepage?movieid=${item.key}">${item.value.movie.title}</a></td>
						<td>
							<form action="ShoppingCart" method="post">
								<input type='hidden' name='updateItem' value='${item.key}' />
								<input type="text" name="quantity" value="${item.value.quantity}" size="4" />
								<input class='form-button' type='submit' value='Update' />
							</form>
						</td>
						<td>
							<form action="ShoppingCart" method="post">
								<input type='hidden' name='deleteItem' value='${item.key}' />
								<input class='form-button' type='submit' value='Delete' />
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<p align="center">Total Price for items:  $${shoppingCart.total}</p>
		<a href="browseByGenre"><h7><center>Continue To Browse</center></h7></a>
		
	</BODY>
</HTML>