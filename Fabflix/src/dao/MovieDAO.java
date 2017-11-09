package dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;

import data.Genre;
import data.Movie;
import data.Star;
import data.StarInMovie;

import java.io.IOException;
import java.sql.*;

public class MovieDAO {

	Connection connection;
	Statement statement;
	private int noOfRecords;
	
	public MovieDAO() { }
	
//	private static Connection getConnection() throws SQLException, ClassNotFoundException {
//		Connection conn = ConnectionFactory.getInstance().getConnection();
//		return conn;
//	}
	
	private static Connection getConnectionPool() throws SQLException, NamingException {
		Connection conn = ConnectionFactory.getInstance().getConnectionPool();
		return conn;
	}
	
	public ArrayList<Star> getStarList(String movieID) {
		
		ArrayList<Star> listOfStars = new ArrayList<Star>();
		
		try {
			connection = getConnectionPool();
//			connection = getConnection();
			String star_query = "SELECT stars.id, first_name, last_name FROM (movies JOIN stars_in_movies NATURAL JOIN stars ON movies.id = stars_in_movies.movie_id AND stars_in_movies.star_id = stars.id) WHERE movies.id = " + movieID;
			statement = connection.createStatement(); 
			ResultSet result = statement.executeQuery(star_query); 
			
			while(result.next())
				listOfStars.add(new Star(result.getInt(1), result.getString(2), result.getString(3))); 
			
			result.close(); 
			statement.close(); 
			connection.close(); 
			
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
		return listOfStars; 
	}
	
	public void batchInsertStarsInMovies(List<StarInMovie> simList) {
		try {
//			connection = getConnection();
			connection = getConnectionPool();
			connection.setAutoCommit(false);
			PreparedStatement ps = connection.prepareStatement("INSERT INTO stars_in_movies (star_id,movie_id) VALUES (?,?)");
			for (StarInMovie sim : simList) {
				if (sim.getStarId() > -1 && sim.getMovieId() > -1) {
					ps.setInt(1, sim.getStarId());
					ps.setInt(2, sim.getMovieId());
					ps.addBatch();
				}
			}
			
			ps.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);
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
	}
	
	public void batchInsertGenresInMovies(List<Movie> movies) {
		try {
//			connection = getConnection();
			connection = getConnectionPool();
			connection.setAutoCommit(false);
			PreparedStatement ps = connection.prepareStatement("INSERT INTO genres_in_movies (genre_id,movie_id) VALUES (?,?)");
			for (Movie movie : movies) {
				int movieId = movie.getId();
				for (Genre genre : movie.getGenreList()) {
					int genreId = genre.getId();
					ps.setInt(1, genreId);
					ps.setInt(2, movieId);
					ps.addBatch();
				}
			}
			
			ps.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);
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
	}
	
	public List<Integer> batchInsertMovie(List<Movie> movies) {
		ResultSet rs = null;
		List<Integer> movieIds = new ArrayList<Integer>();
		try {
//			connection = getConnection();
			connection = getConnectionPool();
			connection.setAutoCommit(false);
			PreparedStatement ps = connection.prepareStatement("INSERT INTO movies (title,year,director) VALUES (?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			for (Movie movie : movies) {
				ps.setString(1, movie.getTitle());
				ps.setInt(2, movie.getYear());
				ps.setString(3, movie.getDirector());
				ps.addBatch();
			}
			
			ps.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);
			rs = ps.getGeneratedKeys();
			while (rs.next()) {
				movieIds.add(rs.getInt(1));
			}
			rs.close();
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
		
		return movieIds;
	}
	
	public int movieTitleToId(String title) {
		int id = -1;
		try {
//			connection = getConnection();
			connection = getConnectionPool();
			statement = connection.createStatement();
			String query = "select id from movies where title = \"" + title + "\";";
			ResultSet rs = statement.executeQuery(query);
			
			if (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
		finally {
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
		return id;
	}
	
	public Movie movieIdToObject(int movieId) {
		Movie movie = new Movie();
		try {
//			connection = getConnection();
			connection = getConnectionPool();
			statement = connection.createStatement();
			String query = "select * from movies where id = " + movieId;
			ResultSet rs = statement.executeQuery(query);
			
			if (rs.next()) {
				int id = rs.getInt("id");
				movie.setId(id);
				movie.setTitle(rs.getString("title"));
				movie.setYear(rs.getInt("year"));
				movie.setDirector(rs.getString("director"));
				movie.setBannerUrl(rs.getString("banner_url"));
				movie.setTrailerUrl(rs.getString("trailer_url"));
				
				Statement genreStat = connection.createStatement();
				String getMovieGenreQuery = "select * from genres" +
											" where genres.id in (" +
											"	select genre_id from genres_in_movies" +
											"    where movie_id = " +
											id +
											");";
				ResultSet genreRs = genreStat.executeQuery(getMovieGenreQuery);
				Genre genre = null;
				while (genreRs.next()) {
					genre = new Genre();
					genre.setId(genreRs.getInt("id"));
					genre.setName(genreRs.getString("name"));
					movie.addToGenreList(genre);
				}
				
				Statement starStat = connection.createStatement();
				String getMovieStarQuery = "select * from stars" +
											" where stars.id in (" +
											"	select stars_in_movies.star_id from stars_in_movies" +
											"    where stars_in_movies.movie_id = " +
											id +
											");";
				ResultSet starRs = genreStat.executeQuery(getMovieStarQuery);
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
			}
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
		return movie;
	}
	
	public List<Movie> viewMoviesByGenre(int offset, int noOfRecords, String genreRequest, String sort) {
		List<Movie> movies = new ArrayList<Movie>();
		Movie movie = null;
		
		try {
//			connection = getConnection();
			connection = getConnectionPool();
			statement = connection.createStatement();
			String selectMovieQuery = "select SQL_CALC_FOUND_ROWS * from movies where movies.id in " 
	    			+ "(select movie_id from genres_in_movies where genre_id = "
	    			+ "(select genres.id from genres where genres.name = '" + genreRequest +
	    			"'))" + " order by movies." + sort +
	    			" limit " + offset + "," + noOfRecords;
			ResultSet rs = statement.executeQuery(selectMovieQuery);
			Statement countStat = connection.createStatement();
			ResultSet countRs = countStat.executeQuery("select FOUND_ROWS()");
			if(countRs.next()) {
                this.noOfRecords = countRs.getInt(1);
			}
			countRs.close();
			countStat.close();
			while (rs.next()) {
				movie = new Movie();
				
				int id = rs.getInt("id");
				movie.setId(id);
				movie.setTitle(rs.getString("title"));
				movie.setYear(rs.getInt("year"));
				movie.setDirector(rs.getString("director"));
				movie.setBannerUrl(rs.getString("banner_url"));
				movie.setTrailerUrl(rs.getString("trailer_url"));
				
				Statement genreStat = connection.createStatement();
				String getMovieGenreQuery = "select * from genres" +
											" where genres.id in (" +
											"	select genre_id from genres_in_movies" +
											"    where movie_id = " +
											id +
											");";
				ResultSet genreRs = genreStat.executeQuery(getMovieGenreQuery);
				Genre genre = null;
				while (genreRs.next()) {
					genre = new Genre();
					genre.setId(genreRs.getInt("id"));
					genre.setName(genreRs.getString("name"));
					movie.addToGenreList(genre);
				}
				
				Statement starStat = connection.createStatement();
				String getMovieStarQuery = "select * from stars" +
											" where stars.id in (" +
											"	select stars_in_movies.star_id from stars_in_movies" +
											"    where stars_in_movies.movie_id = " +
											id +
											");";
				ResultSet starRs = genreStat.executeQuery(getMovieStarQuery);
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
			
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
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
	
	public List<Movie> viewMoviesByTitle(int offset, int noOfRecords, String initial, String sort) {
		List<Movie> movies = new ArrayList<Movie>();
		Movie movie = null;
		
		try {
//			connection = getConnection();
			connection = getConnectionPool();
			statement = connection.createStatement();
			String selectMovieQuery = "select SQL_CALC_FOUND_ROWS * from movies where movies.title like '" +
			initial + "%' ";
			if (initial.equals("NUMBER")) {
				selectMovieQuery = "select SQL_CALC_FOUND_ROWS * from movies where movies.title regexp '^[0-9]+' ";
			}
			selectMovieQuery += "order by movies." + sort + " limit " + offset + "," + noOfRecords;
			ResultSet rs = statement.executeQuery(selectMovieQuery);
			Statement countStat = connection.createStatement();
			ResultSet countRs = countStat.executeQuery("select FOUND_ROWS()");
			if(countRs.next()) {
                this.noOfRecords = countRs.getInt(1);
			}
			countRs.close();
			countStat.close();
			while (rs.next()) {
				movie = new Movie();
				
				int id = rs.getInt("id");
				movie.setId(id);
				movie.setTitle(rs.getString("title"));
				movie.setYear(rs.getInt("year"));
				movie.setDirector(rs.getString("director"));
				movie.setBannerUrl(rs.getString("banner_url"));
				movie.setTrailerUrl(rs.getString("trailer_url"));
				
				Statement genreStat = connection.createStatement();
				String getMovieGenreQuery = "select * from genres" +
											" where genres.id in (" +
											"	select genre_id from genres_in_movies" +
											"    where movie_id = " +
											id +
											");";
				ResultSet genreRs = genreStat.executeQuery(getMovieGenreQuery);
				Genre genre = null;
				while (genreRs.next()) {
					genre = new Genre();
					genre.setId(genreRs.getInt("id"));
					genre.setName(genreRs.getString("name"));
					movie.addToGenreList(genre);
				}
				
				Statement starStat = connection.createStatement();
				String getMovieStarQuery = "select * from stars" +
											" where stars.id in (" +
											"	select stars_in_movies.star_id from stars_in_movies" +
											"    where stars_in_movies.movie_id = " +
											id +
											");";
				ResultSet starRs = genreStat.executeQuery(getMovieStarQuery);
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
			
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
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
	
	public int getNoOfRecords() {
		return noOfRecords;
	}
}
