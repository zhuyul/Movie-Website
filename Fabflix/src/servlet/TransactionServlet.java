package servlet;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.TransDAO;
import data.CartItem;
import data.ShoppingCart;

public class TransactionServlet extends HttpServlet{
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String fname = request.getParameter("first_name");
		String lname = request.getParameter("last_name");
		String creditcard = request.getParameter("creditcard");
		String expiration = request.getParameter("expiration");
		ShoppingCart cart = (ShoppingCart) session.getAttribute("shopping_cart");
		TransDAO transaction = new TransDAO();
		Integer validate_id = transaction.validateCustomer(creditcard, fname,lname, expiration);
		
		if(validate_id != -1){
			session.removeAttribute("shopping_cart");
			session.setAttribute("shopping_cart", new ShoppingCart());
			
			session.setAttribute("return_message", "Your transaction has been successfully processed!");
			LocalDate date = LocalDate.now();
			HashMap<Integer, CartItem> items = cart.getItems();
			for(int movieId : items.keySet()){
				int quantity = items.get(movieId).getQuantity();
				for(int i=0; i < quantity; i++ ){
					transaction.add_to_sale(validate_id, Integer.toString(movieId),date);
				}
			}
		}
		else{
			session.setAttribute("return_message", "Your transaction cannot be processed due to information error.");
		}
		response.sendRedirect("confirmation.jsp");
	}
	
	public void doGet(HttpServletRequest request, 
						HttpServletResponse response) throws ServletException{
		doGet(request,response);
	}
}
