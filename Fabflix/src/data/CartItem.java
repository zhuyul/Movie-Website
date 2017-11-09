package data;

public class CartItem {

	private Movie movie;
	private int quantity;
	private double price;
	
	public CartItem(Movie movie, int quantity) {
		this.movie = movie;
		this.quantity = quantity;
		this.price = quantity * 10.99;
	}
	
	public void addQuantity(int quantity) {
		this.quantity += quantity;
		this.price += quantity * 10.99;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	
	
}
