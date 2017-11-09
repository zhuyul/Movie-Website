package dao;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import data.Genre;

import java.sql.*;

public class GenreDAO {

	Connection connection;
	Statement statement;
	private int noOfRecords;
	
	public GenreDAO() { }
	
//	private static Connection getConnection() throws SQLException, ClassNotFoundException {
//		Connection conn = ConnectionFactory.getInstance().getConnection();
//		return conn;
//	}
	
	private static Connection getConnectionPool() throws SQLException, NamingException {
		Connection conn = ConnectionFactory.getInstance().getConnectionPool();
		return conn;
	}
	
	public int insertGenre(String name) {
		int id = -1;
		try {
//			connection = getConnection();
			connection = getConnectionPool();
			statement = connection.createStatement();
			PreparedStatement ps = connection.prepareStatement("insert into genres (name) values (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            
            if (rs.next()) {
            	id = rs.getInt(1);
//            	System.out.println("insert a new genre: " + name);
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
		return id;
	}
	
	public int getIdFromName(String name) {
		int id = -1;
		try {
//			connection = getConnection();
			connection = getConnectionPool();
			statement = connection.createStatement();
			String selectGenreQuery = "select id from genres where name = '" + name + "';";
            ResultSet genreRs = statement.executeQuery(selectGenreQuery);
            
            if (genreRs.next()) {
            	id = genreRs.getInt("id");
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
		return id;
	}
	
	public List<Genre> getAllGenre() {
		List<Genre> genreList = new ArrayList<Genre>();
		Genre genre;
		try {
//			connection = getConnection();
			connection = getConnectionPool();
			statement = connection.createStatement();
			String selectGenreQuery = "select * from genres order by name";
            ResultSet genreRs = statement.executeQuery(selectGenreQuery);
            
            while (genreRs.next()) {
            	genre = new Genre();
            	genre.setId(genreRs.getInt("id"));
            	genre.setName(genreRs.getString("name"));
            	genreList.add(genre);
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
		return genreList;
	}
}
