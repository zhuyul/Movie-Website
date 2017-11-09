package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import data.Star;

public class StarDAO {

	Connection connection;
	Statement statement;
	private int noOfRecords;
	
//	private static Connection getConnection() throws SQLException, ClassNotFoundException {
//		Connection conn = ConnectionFactory.getInstance().getConnection();
//		return conn;
//	}
	
	private static Connection getConnectionPool() throws SQLException, NamingException {
		Connection conn = ConnectionFactory.getInstance().getConnectionPool();
		return conn;
	}
	
	public int starNameToId(String firstName, String lastName) {
		int id = -1;
		try {
			connection = getConnectionPool();
//			connection = getConnection();
			statement = connection.createStatement();
			String query = "select id from stars where first_name = \"" + firstName 
					+ "\" and last_name = \"" + lastName + "\";";
			ResultSet rs = statement.executeQuery(query);
			
			if (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		} catch (Exception e) {
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
	
	public List<Integer> batchInsertStar(List<Star> stars) {
		ResultSet rs = null;
		List<Integer> starIds = new ArrayList<Integer>();
		try {
//			connection = getConnection();
			connection = getConnectionPool();
			connection.setAutoCommit(false);
			PreparedStatement ps = connection.prepareStatement("INSERT INTO stars (first_name,last_name) VALUES (?,?)",
					Statement.RETURN_GENERATED_KEYS);
			for (Star star : stars) {
				ps.setString(1, star.getFirstName());
				ps.setString(2, star.getLastName());
				ps.addBatch();
			}
			
			ps.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);
			rs = ps.getGeneratedKeys();
			
			while (rs.next()) {
				starIds.add(rs.getInt(1));
			}
			
		} catch (Exception e) {
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
		return starIds;
	}
}
