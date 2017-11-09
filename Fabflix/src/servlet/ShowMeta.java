package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ConnectionFactory;

public class ShowMeta extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		
		HttpSession session = request.getSession(true);
		
		if (session.getAttribute("login_status") != null) {
			String loginStatus = (String)session.getAttribute("login_status");
			if (!loginStatus.equals("employee")) {
				response.sendRedirect("login.html");
			}
		}
		// <TABLE_NAME, <COLUMN_NAME, COLUMN_TYPE>>
		HashMap<String, HashMap<String, String>> tableMap = new HashMap<String, HashMap<String, String>>();
		
		try {
	    	String   catalog          = null;
	        String   schemaPattern    = null;
	        String   tableNamePattern = null;
	        String[] types            = null;
        	Connection dbcon = ConnectionFactory.getInstance().getConnection();
        	DatabaseMetaData databaseMetaData = dbcon.getMetaData();
            ResultSet result = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);
            
            while(result.next()){
    		    String tableName = result.getString(3);

    		    // <COLUMN_NAME, COLUMN_TYPE>
    		    HashMap<String, String> columnMap = new HashMap<String, String>();
    		    ResultSet tableResult = databaseMetaData.getColumns(null, null, tableName, null);
    		    while (tableResult.next()) {
    		    	String columnName = tableResult.getString("COLUMN_NAME");
    		    	String columnType = tableResult.getString("TYPE_NAME");
    		    	columnMap.put(columnName, columnType);
    		    }
    		    tableMap.put(tableName, columnMap);
            }
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		request.setAttribute("tables", tableMap);
		request.getRequestDispatcher("WEB-INF/show_metadata.jsp").forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		doGet(request, response);
	}
}
