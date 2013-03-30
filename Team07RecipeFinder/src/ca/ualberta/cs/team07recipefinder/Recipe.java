package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

/**
 * Data class that contains the components of a Recipe.
 * 
 * @author gcoomber
 * 
 */
public class Recipe {

	String name;
	String description;
	ArrayList<String> ingredients;
	String directions;
	UUID recipeId;
	String creatorEmail;
	Location location;
	Boolean isUpdated;
	int imageNumber;

	/*
	 * Creates recipe with inputed data, and also generates unique UUID for
	 * access.
	 */
	public Recipe(String name, String description,
			ArrayList<String> ingredients, String directions,
			String creatorEmail, Location location) {

		this.name = name;
		this.description = description;
		this.ingredients = ingredients;
		this.directions = directions;
		this.creatorEmail = creatorEmail;
		this.location = location;
		this.recipeId = UUID.randomUUID();
		this.isUpdated = false;
		this.imageNumber = 0;
	}

	public Recipe() {
		this.recipeId = UUID.randomUUID();
		this.isUpdated = false;
		this.imageNumber = 0;
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

	public String getCreatorEmail() {
		return this.creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	public void setIsUpdated(Boolean isUpdate) {
		this.isUpdated = isUpdate;
	}

	public Boolean getIsUpdated() {
		return this.isUpdated;
	}

	public void setImageNumber(int number) {
		this.imageNumber = number;
	}

	public int getImageNumber() {
		return this.imageNumber;
	}

	@Override
	public String toString() {
		return this.name + " by " + this.creatorEmail + "\n" + this.description;
	}

	static public enum Location {
		LOCAL, SERVER;
	}
}
