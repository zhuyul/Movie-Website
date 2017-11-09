package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import data.Movie;

public class AutoCompleteDAO {
	Connection connection;
	PreparedStatement statement;
	
//	private static Connection getConnection() throws SQLException, ClassNotFoundException {
//		Connection conn = ConnectionFactory.getInstance().getConnection();
//		return conn;
//	}
	
	private static Connection getConnectionPool() throws SQLException, NamingException {
		Connection conn = ConnectionFactory.getInstance().getConnectionPool();
		return conn;
	}

	public List<Movie> getData(String input){
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

}


