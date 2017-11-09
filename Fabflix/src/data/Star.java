package data;

public class Star {

	private int id;
	private String firstName;
	private String lastName;
	private String dob;
	private String photoUrl;
	private String name; 

	
	public Star () { }
	
	public Star(int id, String firstName, String lastName){
		
		this.id = id; 
		this.firstName = firstName; 
		this.lastName = lastName; 
		this.name = firstName + " " + lastName; 
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	@Override
	public String toString() {
		String result = "Star: id = ";
		result += id;
		if (!firstName.equals("")) {
			result += ", " + firstName + " ";
		}
		result += lastName;
		return result;
	}
	
	
}
