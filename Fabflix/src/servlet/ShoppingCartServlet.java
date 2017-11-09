package servlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.ShoppingCart;

public class ShoppingCartServlet extends HttpServlet {

	private static final long serialVersionUID = 548466052600028744L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		
		response.setContentType("text/css");    // Response mime type
        
        // Get login information with Session
        HttpSession session = request.getSession(true);
        String userName = (String) session.getAttribute("first_name");
        Integer userId = (Integer) session.getAttribute("user_id");
        ShoppingCart sc = (ShoppingCart) session.getAttribute("shopping_cart");
        
        
        if (session.getAttribute("first_name") == null) {
        	response.sendRedirect("login.html");
        	return;
        }
        
        int addItem = -1;
        int deleteItem = -1;
        int updateItem = -1;
        int quantity = -1;
        
        if (request.getParameter("addItem") != null)
        	addItem = Integer.parseInt(request.getParameter("addItem"));
        if (request.getParameter("deleteItem") != null)
        	deleteItem = Integer.parseInt(request.getParameter("deleteItem"));
        if (request.getParameter("updateItem") != null)
        	updateItem =Integer.parseInt(request.getParameter("updateItem"));
        if (request.getParameter("quantity") != null)
        	quantity = Integer.parseInt(request.getParameter("quantity"));
                
        if (addItem != -1) {
    		sc.addItem(addItem, 1);
        } else if (deleteItem != -1) {
        	sc.deleteItem(deleteItem);
        } else if (updateItem != -1) {
        	sc.updateItem(updateItem, quantity);
        }
        
        if (userName != null) {
        	request.setAttribute("userName", userName);
        	request.setAttribute("userId", userId);
        	request.setAttribute("shoppingCart", sc);
        	session.setAttribute("shopping_cart", sc);
        }
        
        request.getRequestDispatcher("WEB-INF/shopping_cart.jsp").forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {
		doGet(request, response);
	}
}
