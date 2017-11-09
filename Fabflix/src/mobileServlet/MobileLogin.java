package mobileServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ConnectionFactory;
import org.json.simple.JSONObject;


public class MobileLogin extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		JSONObject jsonObject = new JSONObject();
		
		try {
        	Connection dbcon = ConnectionFactory.getInstance().getConnection();
        	// Declare our statement
			Statement statement = dbcon.createStatement();
			
			String email = request.getParameter("eAddress");
			String password = request.getParameter("password");
			String query = "SELECT * from customers where email = '" + email + "' and password = '"
			+ password + "'";

			// Perform the query
			ResultSet rs = statement.executeQuery(query);
            
            boolean status = rs.next();
            if (status) {
            	String firstName = rs.getString("first_name");
            	Integer userId = rs.getInt("ID");
//            	ShoppingCart sc = new ShoppingCart();
//            	session.setAttribute("first_name", firstName);
//            	session.setAttribute("user_id", userId);
//            	session.setAttribute("shopping_cart", sc);
                jsonObject.put("success", true);
                jsonObject.put("first_name", firstName);
                jsonObject.put("user_id", userId);
                
            } else {
            	jsonObject.put("success", false);
            }

            response.getWriter().write(jsonObject.toString());
            
            rs.close();
            statement.close();
            dbcon.close();
        } catch (SQLException | ClassNotFoundException e) {
    		e.printStackTrace();
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		doPost(request, response);
	}
}
