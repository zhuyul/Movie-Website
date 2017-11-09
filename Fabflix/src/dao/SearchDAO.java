package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.*;

import data.Genre;
import data.Movie;
import data.Star;

public class SearchDAO {
	
	Connection connection;
	PreparedStatement statement;
	private int noOfRecords;
	
//	private static Connection getConnection() throws SQLException, ClassNotFoundException {
//		Connection conn = ConnectionFactory.getInstance().getConnection();
//		return conn;
//	}
	
	private static Connection getConnectionPool() throws SQLException, NamingException {
		Connection conn = ConnectionFactory.getInstance().getConnectionPool();
		return conn;
	}
	
	public List<Movie> autoComplete(String input){
		List<Movie> movies = new ArrayList<Movie>();
		String[] input_elements = input.split(" ");
		String query = "SELECT * from movies_ft WHERE MATCH (title) AGAINST (? IN BOOLEAN MODE);";
		String keyWord = "";
		if(input_elements.length >= 1){
			for(int i = 0; i < input_elements.length - 1; i++){
				String current_element = "+" +input_elements[i] + " ";
				keyWord += current_element;
			}
			String last_element = "+" + input_elements[input_elements.length - 1] + "*";
			keyWord += last_element;
		}
		try{
//		    connection = getConnection();
			connection = getConnectionPool();
		    statement =  connection.prepareStatement(query);
		    statement.setString(1, keyWord);
		    ResultSet rs = statement.executeQuery();
		    while(rs.next()){
		    	Movie movie = new Movie();
				int id = rs.getInt("id");
				movie.setId(id);
				movie.setTitle(rs.getString("title"));
				movie.setYear(rs.getInt("year"));
				movie.setDirector(rs.getString("director"));
				movie.setBannerUrl(rs.getString("banner_url"));
				movie.setTrailerUrl(rs.getString("trailer_url"));
		    	movies.add(movie);
		    }

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return movies;
	}
	
	public List<Movie> mobileSearch(String title) {
		List<Movie> movies = new ArrayList<Movie>();
		String query = "SELECT * FROM movies_ft WHERE MATCH (title) AGAINST ('?');";
		
		try {
//			connection = getConnection();
			connection = getConnectionPool();
			statement = connection.prepareStatement(query);
			statement.setString(1, title);
			ResultSet result = statement.executeQuery();
			while (result.next()){
				Integer mvid = Integer.parseInt(result.getString("id"));
				String mvtitle = result.getString("title");
				Integer mvyear = Integer.parseInt(result.getString("year"));
				String mvdirector = result.getString("director");
				
				Movie movie = new Movie();
				movie.setId(mvid);
				movie.setTitle(mvtitle);
				movie.setYear(mvyear);
				movie.setDirector(mvdirector);
				
				
				String getMovieGenreQuery = "select * from genres" +
											" where genres.id in (" +
											"	select genre_id from genres_in_movies" +
											"    where movie_id = ?);";
				PreparedStatement genreStat = connection.prepareStatement(getMovieGenreQuery);
				genreStat.setInt(1, mvid);
				ResultSet genreRs = genreStat.executeQuery();
				Genre genre = null;
				while (genreRs.next()) {
					genre = new Genre();
					genre.setId(genreRs.getInt("id"));
					genre.setName(genreRs.getString("name"));
					movie.addToGenreList(genre);
				}
				
				String getMovieStarQuery = "select * from stars" +
											" where stars.id in (" +
											"	select stars_in_movies.star_id from stars_in_movies" +
											"    where stars_in_movies.movie_id = ?);";
				PreparedStatement starStat = connection.prepareStatement(getMovieStarQuery);
				starStat.setInt(1, mvid);
				ResultSet starRs = genreStat.executeQuery();
				Star star = null;
				while (starRs.next()) {
					star = new Star();
					star.setId(starRs.getInt("id"));
					star.setFirstName(starRs.getString("first_name"));
					star.setLastName(starRs.getString("last_name"));
					star.setDob(starRs.getString("dob"));
					star.setPhotoUrl(starRs.getString("photo_url"));
					movie.addToStarList(star);
				}
				
				genreRs.close();
				starRs.close();
				genreStat.close();
				starStat.close();
				
				movies.add(movie);
			}
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return movies;
	}

	public List<Movie> searchMovie(String title,String year, String director,String fname, String lname,
			int offset, int noOfRecords, String sort) {
		
		List<Movie> movies = new ArrayList<Movie>();

		try {
//			connection = getConnection();
			connection = getConnectionPool();
			String query = "SELECT DISTINCT SQL_CALC_FOUND_ROWS m.* "
					+"FROM movies as m JOIN stars_in_movies as sm JOIN stars as s "
					+"ON m.id=sm.movie_id AND s.id=sm.star_id WHERE (1 = 1)";
			if(!title.equals("")) query += " AND (m.title LIKE ?)";//"%" + title + "%"
			if(!year.equals(""))  query += " AND (m.year = ?)";//year
			if(!director.equals("")) query += " AND (m.director LIKE ?)";//"%" + director + "%"
			if(!fname.equals("")) query += " AND (s.first_name LIKE ?)";//"%" + fname + "%"
			if(!lname.equals("")) query += " AND (s.last_name LIKE ?)";//"%" + lname + "%"
			query += " order by m." + sort + " limit " + offset + "," + noOfRecords;

			statement = connection.prepareStatement(query);
			
			int queryOffset = 1;

			if (!title.equals("")) {
				statement.setString(queryOffset, "%" + title + "%");
				offset++;
			}
			if (!year.equals("")) {
				statement.setInt(queryOffset, Integer.parseInt(year));
				offset++;
			}
			if(!director.equals("")){
				statement.setString(queryOffset, "%" + director + "%");
				offset++;
			}
			if(!fname.equals("")) {
				statement.setString(queryOffset, "%" + fname + "%");
				offset++;
			}
			if(!lname.equals("")) {
				statement.setString(queryOffset, "%" + lname + "%");
				offset++;
			}
			ResultSet result = statement.executeQuery();
			
			PreparedStatement countStat = connection.prepareStatement("select FOUND_ROWS()");
			ResultSet countRs = countStat.executeQuery();
			if(countRs.next()) {
	            this.noOfRecords = countRs.getInt(1);
			}
			countRs.close();
			countStat.close();
		
			while (result.next()){
				Integer mvid = Integer.parseInt(result.getString("id"));
				String mvtitle = result.getString("title");
				Integer mvyear = Integer.parseInt(result.getString("year"));
				String mvdirector = result.getString("director");
				
				Movie movie = new Movie();
				movie.setId(mvid);
				movie.setTitle(mvtitle);
				movie.setYear(mvyear);
				movie.setDirector(mvdirector);
				
				String getMovieGenreQuery = "select * from genres" +
											" where genres.id in (" +
											"	select genre_id from genres_in_movies" +
											"    where movie_id = ?);";
				PreparedStatement genreStat = connection.prepareStatement(getMovieGenreQuery);
				genreStat.setInt(1, mvid);
				ResultSet genreRs = genreStat.executeQuery();
				Genre genre = null;
				while (genreRs.next()) {
					genre = new Genre();
					genre.setId(genreRs.getInt("id"));
					genre.setName(genreRs.getString("name"));
					movie.addToGenreList(genre);
				}
				
				String getMovieStarQuery = "select * from stars" +
											" where stars.id in (" +
											"	select stars_in_movies.star_id from stars_in_movies" +
											"    where stars_in_movies.movie_id = ?);";
				PreparedStatement starStat = connection.prepareStatement(getMovieStarQuery);
				starStat.setInt(1, mvid);
				ResultSet starRs = starStat.executeQuery();
				Star star = null;
				while (starRs.next()) {
					star = new Star();
					star.setId(starRs.getInt("id"));
					star.setFirstName(starRs.getString("first_name"));
					star.setLastName(starRs.getString("last_name"));
					star.setDob(starRs.getString("dob"));
					star.setPhotoUrl(starRs.getString("photo_url"));
					movie.addToStarList(star);
				}
				
				genreRs.close();
				starRs.close();
				genreStat.close();
				starStat.close();
				
				movies.add(movie);
			}
			result.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return movies;
	}
	
	public int getNoOfRecords() {
		return this.noOfRecords;
	}
}
