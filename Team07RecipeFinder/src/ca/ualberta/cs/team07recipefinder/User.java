package ca.ualberta.cs.team07recipefinder;


public class User {
	private static User instance = null;
	private String name;
	private String email;
	private Pantry pantry;
	
	protected User() {
		
	}
	
	public static User getInstance() {
		if (instance == null) {
			instance = new User();
		}
		return instance;
	}
	
	public void emailRecipe(int recipeId) {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Pantry getPantry() {
		return pantry;
	}

	public void setPantry(Pantry pantry) {
		this.pantry = pantry;
	}
	
}
