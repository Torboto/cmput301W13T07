package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

/**
 * Data class that contains the components of a Recipe.
 * @author gcoomber
 *
 */
public class Recipe {

	String name;
	String description;
	ArrayList<String> ingredients;
	String directions;
	ArrayList<String> images;
	UUID recipeId;
	String creatorEmail;

	/*
	 * Creates recipe with inputed data, and also generates unique UUID for
	 * access.
	 */
	public Recipe(String name, String description,
			ArrayList<String> ingredients, String directions, String creatorEmail) {

		this.name = name;
		this.description = description;
		this.ingredients = ingredients;
		this.directions = directions;
		this.creatorEmail = creatorEmail;
		this.recipeId = UUID.randomUUID();
	}

	public ArrayList<String> getImages() {
		return this.images;
	}

	/*
	 * Add an image to the collection of images for the recipe
	 */
	public void setImage(String image) {
		this.images.add(image);
	}

	public UUID getRecipeId() {
		return this.recipeId;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDirections() {
		return this.directions;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public ArrayList<String> getIngredients() {
		return this.ingredients;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setIngredients(ArrayList<String> ingredients) {
		this.ingredients = ingredients;
	}

	public void setDirections(String directions) {
		this.directions = directions;
	}
	
	@Override
	public String toString() {
		return this.name + " by " + this.creatorEmail + "\n" +
				this.description;
	}
	
	
}
