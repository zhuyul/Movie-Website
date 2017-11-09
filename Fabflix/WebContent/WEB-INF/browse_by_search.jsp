<%@page import="java.sql.*,
 javax.sql.*,
 java.io.IOException,
 javax.servlet.http.*,
 javax.servlet.*"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="home.css" />
	<title>FabFlix - Search Results</title>
   	<script type="text/javascript"
			src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
	<script src="js/jQuery.autocomplete.js"></script>  
	<script type="text/javascript" src="js/ajax.js"></script>
	<style>
		.ac_results {
		padding: 0px;
		border: 1px solid WindowFrame;
		background-color: Window;
		overflow: hidden;
		}
		
		.ac_results ul {
			width: 100%;
			list-style-position: outside;
			list-style: none;
			padding: 0;
			margin: 0;
		}
		
		.ac_results iframe {
			display:none;/*sorry for IE5*/
			display/**/:block;/*sorry for IE5*/
			position:absolute;
			top:0;
			left:0;
			z-index:-1;
			filter:mask();
			width:3000px;
			height:3000px;
		}
		
		.ac_results li {
			margin: 0px;
			padding: 2px 5px;
			cursor: pointer;
			display: block;
			width: 100%;
			font: menu;
			font-size: 12px;
			overflow: hidden;
		}
		
		.ac_loading {
			background : Window url('./indicator.gif') right center no-repeat;
		}
		
		.ac_over {
			background-color: Highlight;
			color: HighlightText;
		}

		.popbox {
		    display: none;
		    position: absolute;
		    z-index: 99999;
		    width: 400px;
		    padding: 10px;
		    background: #EEEFEB;
		    color: #000000;
		    border: 1px solid #4D4F53;
		    margin: 0px;
		    -webkit-box-shadow: 0px 0px 5px 0px rgba(164, 164, 164, 1);
		    box-shadow: 0px 0px 5px 0px rgba(164, 164, 164, 1);
		}
	</style>	
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
				<c:if test="${not empty userName}">
					<li><a href="servlet/Logout">Log Out</a></li>
				</c:if>
				<c:if test="${empty userName}">
					<li><a href="login.html">Log In</a></li>
				</c:if>
	    	</ul> 
		</div>
	</nav>
	
	<div id="dropDown">
		<h2> Search </h2>
		<form action="search?${parameters}&sort=${sort}" method="post">
	  		<label for="rpp"><font size="3">Listings per page</font></label>
	  		<select id="rpp" name="rpp">
		  		<option>10</option>
	      		<option>25</option>
	      		<option>50</option>
	  		</select>
	  		<input type="submit" value = "submit">
  		</form>
  	</div>
	
	<div class="popbox" id="popbox"></div>
	
	<table border>
		<tbody>
			<tr>
				<th>ID</th>
				<c:choose>
					<c:when test="${sort == 'TITLE'}">
						<th><a href="search?${parameters}&rpp=${rpp}&sort=TITLE DESC">Title</a></th>
					</c:when>
					<c:otherwise>
						<th><a href="search?${parameters}&rpp=${rpp}&sort=TITLE">Title</a></th>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${sort == 'YEAR'}">
						<th><a href="search?${parameters}&rpp=${rpp}&sort=YEAR DESC">Year</a></th>
					</c:when>
					<c:otherwise>
						<th><a href="search?${parameters}&rpp=${rpp}&sort=YEAR">Year</a></th>
					</c:otherwise>
				</c:choose>
				<th>Director</th>
				<th>Genres</th>
				<th>Stars</th>
			</tr>
			
			<% int counter = 0; %>
			
	        <c:forEach var="movie" items="${movieList}">
	            <tr>
	                <td>${movie.id}</td>
	                <td><a href="moviepage?movieid=${movie.id}"
	                		class="poptext"
	                		id="<%= "poptext" + counter%>"
	                		onmouseover="popupWindow(${movie.id}, <%=counter %>)"
	                		>${movie.title}</a></td>
	                <td>${movie.year}</td>
	                <td>${movie.director}</td>
	                <td>
	                	<c:forEach var="genre" items="${movie.genreList}">
	                	${genre.name}
	                	</c:forEach>
	                </td>
	                <td>
	                	<c:forEach var="star" items="${movie.starList}">
	                	<a href="starpage?starid=${star.id}">${star.firstName} ${star.lastName}</a>
	                	</c:forEach>
	                </td>
	            </tr>
	            <% counter++; %>
        	</c:forEach>
		</tbody>
	</table>
	
    <c:if test="${currentPage != 1}">
   		<td><a href="search?${parameters}&rpp=${rpp}&sort=${sort}&page=${currentPage - 1}">Prev</a></td>
	</c:if>
   	<c:if test="${currentPage lt noOfPage}">
   		&nbsp;&nbsp;&nbsp;<td><a href="search?${parameters}&rpp=${rpp}&sort=${sort}&page=${currentPage + 1}">Next</a></td>
	</c:if> 
	
</body>
</html>