package dao;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
public class ConnectionFactory {
	private static ConnectionFactory instance = 
            new ConnectionFactory();
	String url = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";
	String user = "testuser";
	String password = "testpass";
	String driverClass = "com.mysql.jdbc.Driver"; 
	 
	//private constructor
	private ConnectionFactory() {
	    try {
	        Class.forName(driverClass);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	 
	public static ConnectionFactory getInstance()   {
	    return instance;
	}
	
	 
	public Connection getConnection() throws SQLException, ClassNotFoundException {
	    Connection connection = DriverManager.getConnection(url, user, password);
	    return connection;
	}

	
	public Connection getConnectionPool() throws SQLException, NamingException {
		Context initContext = new InitialContext();
		Context envContext = (Context)initContext.lookup("java:comp/env");
		DataSource ds = (DataSource)envContext.lookup("jdbc/MovieDB");
		Connection connection = ds.getConnection();
		return connection;
	}
}
