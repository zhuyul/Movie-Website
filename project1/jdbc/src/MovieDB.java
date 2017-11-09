import java.sql.*;                              // Enable SQL processing
import java.util.Scanner;

public class MovieDB {
	
    public static void main(String[] arg) throws Exception {

        // Incorporate mySQL driver
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        
        System.out.println("Welcome to MovieDB!");
    	Scanner scanner = new Scanner(System.in);

        while (true) {
	        System.out.println("Enter a following command: ");
	        System.out.println("[1] Log In");
	        System.out.println("[0] Quit");
	        
	        String command = scanner.nextLine().trim();
	        switch (command) {
		        case "1":
		        	break;
		        case "0":
		        	scanner.close();
		        	return;
		        default:
		        	System.out.println("Invalid input, try again!");
		        	continue;
	        }
	        System.out.println("Please enter your username: ");
	        String username = scanner.nextLine();
	        System.out.println("password: ");
	        String password = scanner.nextLine();
	        
	        Connection connection = null;
	        
	        try {
		         // Connect to the moviedb database
		        connection = DriverManager.getConnection("jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false", username, password);
		        boolean isLoggedIn = true;
		        System.out.println("You are logged in.");
		        while (isLoggedIn) {
			        printMenu();
			        command = scanner.nextLine().trim();
			        switch (command) {
				        case "0":
				        	System.out.println("You are logged out.");
				        	isLoggedIn = false;
				        	break;
				        case "1":
				        	try {
				        		searchStar(connection);
				        	} catch (SQLException e) {
				        		System.err.println(e.getMessage());
				        	}
				        	break;
				        case "2":
				        	try {
				        		insertStar(connection);
				        	} catch (SQLException e) {
				        		System.err.println(e.getMessage());
				        	}
				        	break;
				        case "3":
				        	try {
				        		insertCustomer(connection);
				        	} catch (SQLException e) {
				        		System.err.println(e.getMessage());
				        	}
				        	break;
				        case "4":
				        	try {
				        		deleteCustomer(connection);
				        	} catch (SQLException e) {
				        		System.err.println(e.getMessage());
				        	}
				        	break;
				        case "5":
				        	try {
				        		getMetaData(connection);
				        	} catch (SQLException e) {
				        		System.err.println(e.getMessage());
				        	}
				        	break;
				        case "6":
				        	try {
				        		sqlQuery(connection);
				        	} catch (SQLException e) {
				        		System.err.println(e.getMessage());
				        	}
				        	break;
			        	default:
			        		System.out.println("Invalid input, try again.");
			        		break;
			        }
		        }
	        } catch (SQLException sqle) {
	        	//Handle errors for JDBC
	        	System.err.println("ERROR: " + sqle.getMessage());
	        } finally {
	        	try {
	        		if (connection != null) {
	        			connection.close();
	        		}
	        	} catch (SQLException se) {
	                se.printStackTrace();
	            }
	        }
	    }
    }
    
    private static void printMenu() {
    	System.out.println();
        System.out.println("Please enter a command:");
        System.out.println("[1] Search movies featuring a star");
        System.out.println("[2] Insert a new star");
        System.out.println("[3] Insert a new customer");
        System.out.println("[4] Delete a customer");
        System.out.println("[5] Check database metadata");
        System.out.println("[6] Enter a SQL query");
        System.out.println("[0] Log out");
    }

    private static void searchStar(Connection connection) throws SQLException {
    	Statement select = connection.createStatement();
    	Scanner s = new Scanner(System.in);
    	String firstName, lastName, id;
    	ResultSet result;
    	
    	// Print Menu
    	System.out.println("Choose a search method:");
    	System.out.println("[1] Search by first name and/or last name");
    	System.out.println("[2] Search by id");
    	String command = s.nextLine().trim();
    	
    	String queryString = "SELECT * FROM movies WHERE movies.id in "
			+ "(select movie_id from stars_in_movies "
			+ "where star_id in "
			+ "(select id from stars ";
    	
    	if(command.equals("1")) {
    		System.out.println("Enter the first name:");
        	firstName = s.nextLine().trim();
        	System.out.println("Enter the last name:");
        	lastName = s.nextLine().trim();
        	
        	if (!firstName.isEmpty() && lastName.isEmpty()) {
	        	queryString += "where stars.first_name = \""+ firstName + "\"))";
        	} else if (firstName.isEmpty() && !lastName.isEmpty()) {
        		queryString += "where stars.last_name = \""+ lastName + "\"))";
        	} else if (!(firstName.isEmpty() || lastName.isEmpty())) {
        		queryString += "where stars.first_name = \""+ firstName + "\" "
        						+ "and stars.last_name = \"" + lastName + "\"))";
        	} else {
        		System.out.println("You must enter either first name or last name");
        		select.close();
        		return;
        	}
        	
    	} else if (command.equals("2")) {
    		System.out.println("Enter id:");
    		id = s.nextLine().trim();
    		
    		queryString += "where stars.id = \""+ id + "\"))";
    		
    	} else {
    		System.out.println("Invalid input, return to main menu.");
    	}
    	
    	result = select.executeQuery(queryString);
    	int resultCount = 0;
    	
    	while (result.next())
        {
    		resultCount++;
            System.out.println("Id = " + result.getInt(1));
            System.out.println("title = " + result.getString(2));
            System.out.println("year = " + result.getInt(3));
            System.out.println("director = " + result.getString(4));
            System.out.println("banner_url = " + result.getString(5));
            System.out.println("trailer_url = " + result.getString(6));
            System.out.println();
        }
    	System.out.println("Total result count = " + resultCount);
    	System.out.println();
    	
    	select.close();
    }
    
    private static void insertStar(Connection connection) throws SQLException {
    	Statement insert = connection.createStatement();
    	String firstName, lastName, dob, url;
    	Scanner s = new Scanner(System.in);
    	
    	System.out.println("Enter the first name (if the star has a single name, skip this field):");
    	firstName = s.nextLine().trim();

    	System.out.println("Enter the last name (*):");
    	lastName = s.nextLine().trim();
    	if (lastName.isEmpty()) {
    		System.out.println("column last_name is required.");
    		insert.close();
    		return;
    	}
    	
    	String notRequiredAttributes = "";
    	String notRequiredEntries = "";
    	
    	System.out.println("Enter the date of birth (yyyy-mm-dd):");
    	dob = s.nextLine().trim();
    	if (!dob.isEmpty()) {
    		notRequiredAttributes += ", dob";
    		notRequiredEntries += String.format(", \"%s\"", dob);
    	}
    	
    	System.out.println("Enter the photo url:");
    	url = s.nextLine().trim();
    	if (!url.isEmpty()) {
    		notRequiredAttributes += ", photo_url";
    		notRequiredEntries += String.format(", \"%s\"", url);
    	}
    	
    	String queryString = String.format("insert into stars (first_name, last_name%s) values (\"%s\", \"%s\"%s)", notRequiredAttributes, firstName, lastName, notRequiredEntries);

    	int rowUpdated = insert.executeUpdate(queryString);
    	System.out.println("Inserted " + rowUpdated + " star.");
    	
    	insert.close();
    }
    
    private static void insertCustomer(Connection connection) throws SQLException {
    	Statement statement = connection.createStatement();
    	String firstName, lastName, cc_id, address, email, password;
    	Scanner s = new Scanner(System.in);
    	
    	System.out.println("Enter the first name:");
    	firstName = s.nextLine().trim();
    	System.out.println("Enter the last name (*):");
    	lastName = s.nextLine().trim();
    	System.out.println("Enter the credit card id (*):");
    	cc_id = s.nextLine().trim();
    	System.out.println("Enter the address (*):");
    	address = s.nextLine().trim();
    	System.out.println("Enter the email address (*):");
    	email = s.nextLine().trim();
    	System.out.println("Enter the password (*):");
    	password = s.nextLine().trim();
    	
    	if ( lastName.isEmpty() || cc_id.isEmpty() 
    			|| address.isEmpty() || email.isEmpty() || password.isEmpty()) {
    		System.out.println("Required field is not completed.");
    	}
    	
    	// check if credit card exists
    	ResultSet ccResult = statement.executeQuery("select * from creditcards where creditcards.id = " + cc_id);
    	if (!ccResult.next()) {
    		System.out.println("Credit card id does not exist.");
    		statement.close();
    		return;
    	}
    	
    	// insert Customer into customers
    	String queryString = String.format("insert into customers (first_name, last_name, cc_id, address, email, password)"
    	+ " values (\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\")", firstName, lastName, cc_id, address, email, password);

    	int rowUpdated = statement.executeUpdate(queryString);
    	
    	System.out.println("Inserted " + rowUpdated + " customer.");
    	statement.close();
    }
    
    private static void deleteCustomer(Connection connection) throws SQLException {
		Statement delete = connection.createStatement();
		
		Scanner s = new Scanner(System.in);
		String id;
		
		System.out.println("Enter id:");
		id = s.nextLine().trim();
		
		int retID = delete.executeUpdate("delete from customers where id = " + id);
		System.out.println("Deleted " + retID + " customer(s).");
		
		delete.close();
    }
    
    private static void getMetaData(Connection connection) throws SQLException {
    	String   catalog          = null;
        String   schemaPattern    = null;
        String   tableNamePattern = null;
        String[] types            = null;
        
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet result = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);
        
        while(result.next()){
		    String tableName = result.getString(3);
		    System.out.println("Table: " + tableName);

		    ResultSet tableResult = databaseMetaData.getColumns(null, null, tableName, null);
		    while (tableResult.next()) {
		    	String columnName = tableResult.getString("COLUMN_NAME");
		    	String columnType = tableResult.getString("TYPE_NAME");
		    	
		    	System.out.println("  " + columnName + ", type: " + columnType);
		    }
        }
    }
    
    private static void sqlQuery(Connection connection) throws SQLException {
    	Statement statement = connection.createStatement();
    	
    	Scanner s = new Scanner(System.in);
    	String query;
    	System.out.println("Enter the query: ");
    	query = s.nextLine().trim();
    	String command = query.substring(0,6).toUpperCase();
    	
    	if (command.equals("SELECT")) {
    		ResultSet qrResult = statement.executeQuery(query);
    		ResultSetMetaData meta = qrResult.getMetaData();
    		int Colnumber = meta.getColumnCount();
    		
    		int resultCount = 0;
    		
    		while(qrResult.next()){
    			for(int i = 1; i <= Colnumber; i++){
    				if(i > 1){
    					System.out.print(", ");
    				}
    				String Colvalue = qrResult.getString(i);
    				System.out.print(meta.getColumnName(i) + ": " + Colvalue);
    			}
    			System.out.println();
    			resultCount += 1;
    		}
    		
    		System.out.println("Total # of result: " + resultCount);
    	} else {
    		int n = statement.executeUpdate(query);
    		if (n == 1) {
    			System.out.println("Successfully "+command+" a row");	
    		}
    		else{
    			System.out.println("Successfully "+command+" "+n+" rows");
    		}
    	}
    }
}
