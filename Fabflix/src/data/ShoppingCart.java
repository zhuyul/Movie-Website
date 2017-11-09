package data;
import java.util.HashMap;

import dao.MovieDAO;

public class ShoppingCart {
	HashMap<Integer, CartItem> items; //key: movie object value: quantity of the movie
	double total; //total price of items in shopping cart
	
	
	public ShoppingCart(){
		this.items = new HashMap<Integer, CartItem>();
		int total = 0;
	}
	
	public static Movie getMovieFromId(int movieId) {
		MovieDAO dao = new MovieDAO();
		return dao.movieIdToObject(movieId);
	}
	
	//Add certain number of a movie into shopping cart
	//And update the total price
	public void addItem(int movieId, int quantity) {
		CartItem item = new CartItem(getMovieFromId(movieId), quantity);
		double addPrice = item.getPrice();
		if(!items.containsKey(movieId)) {
			items.put(movieId, item);
		} else {
			item = items.get(movieId);
			item.addQuantity(quantity);
			items.put(movieId, item);
		}
		setTotal(this.getTotal() + addPrice);
	}

	//Delete the certain movie from the shopping cart
	//And update the total price
	public void deleteItem(int movieId){
		if (items.containsKey(movieId)) {
			CartItem removedItem = items.remove(movieId);
			setTotal(this.getTotal() - removedItem.getPrice());
		}
	}
	
	//Update the shopping cart when the quantity of a movie is changed
	//And update the total price
	public void updateItem(int movieId, int quantity){
		//The update indicates an increase/unchanged 
		if(quantity >= items.get(movieId).getQuantity()){
			addItem(movieId, quantity - items.get(movieId).getQuantity());		
		}
		//The update indicates a decrement
		else{
			CartItem oldItem = items.get(movieId);
			CartItem newItem = new CartItem(getMovieFromId(movieId), quantity);
			double difference = oldItem.getPrice() - newItem.getPrice();
			items.put(movieId, newItem);
			setTotal(this.getTotal() - difference);
			System.out.println(difference);
		}
	}
	
	public void setTotal(double total){
		this.total = total;
	}
	
	public double getTotal(){
		return total;
	}
	
	public HashMap<Integer, CartItem> getItems() {
		return this.items;
	}
}
