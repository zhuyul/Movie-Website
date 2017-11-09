package dao;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.*;

public class TransDAO extends HttpServlet {
	
	Connection connection;
	Statement statement;

	public TransDAO() { }
	
//	private static Connection getConnection() throws SQLException, ClassNotFoundException {
//		Connection conn = ConnectionFactory.getInstance().getConnection();
//		return conn;
//	}
	
	private static Connection getConnectionPool() throws SQLException, NamingException {
		Connection conn = ConnectionFactory.getInstance().getConnectionPool();
		return conn;
	}
	
	public int validateCustomer(String cc_id, String firstName, String lastName, String expirationDate){
		int result = -1; // return customer id, if not found: -1
		
		try{
			connection = getConnectionPool();
//			connection = getConnection();
			statement = connection.createStatement();
			String query = "select customers.id from customers " +
							"where customers.first_name = '" + firstName +
							"' and customers.last_name = '" + lastName + "'" +
							" and " + cc_id + " in (" +
							"	select creditcards.id from creditcards" +
							"	where creditcards.first_name = customers.first_name" +
							"	and creditcards.last_name = customers.last_name" +
							"	and creditcards.expiration = '" + expirationDate + "')";
			
			ResultSet rs = statement.executeQuery(query);

			if(rs.next()){
				result = rs.getInt("customers.id");
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
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
		return result;


	}
	
	public void add_to_sale(Integer validate_id, String movieID, LocalDate date){
		try{
			connection = getConnectionPool();
//			connection = getConnection();
			statement = connection.createStatement();
			String query = "INSERT INTO sales (customer_id, movie_id, sale_date) VALUES (?,?,?)";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setObject(1, validate_id);
			ps.setObject(2, movieID);
			ps.setObject(3, date);


			ps.executeUpdate();

			
		} catch (SQLException e) {
			e.printStackTrace();
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
		
	}
	
	
	
}
