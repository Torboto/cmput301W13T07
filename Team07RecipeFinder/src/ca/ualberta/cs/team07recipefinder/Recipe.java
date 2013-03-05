package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;

public class Recipe {
	String name;
	String description;
	ArrayList<String> ingredients;
	ArrayList<String> images;
	int recipeId;
	int creatorId;
	
	public Recipe(String name, 
			String description, 
			ArrayList<String> ingredients, 
			ArrayList<String> images, 
			int recipeId, 
			int creatorId){
		
		this.name = name;
		this.description = description;
		this.ingredients = ingredients;
		this.images = images;
		this.recipeId = recipeId;
		this.creatorId = creatorId;
	}
	
	public ArrayList<String> getImages() {
		return this.images;
	}
	
	public void setImage(String image) {
		this.images.add(image);
	}
	
	public int getRecipeId() {
		return this.recipeId;
	}

}
