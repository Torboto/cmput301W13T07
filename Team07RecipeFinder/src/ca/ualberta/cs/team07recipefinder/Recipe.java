package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

public class Recipe {
	String name;
	String description;
	ArrayList<String> ingredients;
	ArrayList<String> images;
	UUID recipeId;
	String creatorEmail;
	
	public Recipe(String name, 
			String description, 
			ArrayList<String> ingredients, 
			ArrayList<String> images,  
			String creatorEmail){
		
		this.name = name;
		this.description = description;
		this.ingredients = ingredients;
		this.images = images;
		this.creatorEmail = creatorEmail;
		this.recipeId = UUID.randomUUID();
	}
	
	public ArrayList<String> getImages() {
		return this.images;
	}
	
	public void setImage(String image) {
		this.images.add(image);
	}
	
	public UUID getRecipeId() {
		return this.recipeId;
	}

}
