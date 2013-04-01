package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author gcoomber
 * 
 *         Data class that contains the components of a Recipe.
 */
public class Recipe {

	String title;
	String description;
	String directions;
	UUID recipeId;

	ArrayList<String> ingredients;
	ArrayList<String> quantities;
	ArrayList<String> units;

	String creatorEmail;
	Location location;
	Boolean isUpdated;
	int imageNumber;

	/**
	 * Creates recipe with inputed data, and also generates unique UUID for
	 * access.
	 * 
	 * @param name
	 *            Title of recipe
	 * @param description
	 *            Description of recipe
	 * @param ingredients
	 *            List of ingredients required for recipe
	 * @param directions
	 *            Instructions on how to make recipe
	 * @param creatorEmail
	 *            Who created the recipe
	 * @param location
	 *            Whether recipe is a local or external recipe
	 */
	public Recipe(String name, String description,
			ArrayList<String> ingredients, String directions,
			String creatorEmail, Location location) {

		this.title = name;
		this.description = description;
		this.ingredients = ingredients;
		this.directions = directions;
		this.creatorEmail = creatorEmail;
		this.location = location;
		this.recipeId = UUID.randomUUID();
		this.isUpdated = false;
		this.imageNumber = 0;
	}

	/**
	 * Used when adding images to a recipe that has not been formally created
	 * yet. Be sure to generate a UUID to be used as the filename for photos.
	 */
	public Recipe() {
		this.isUpdated = false;
		this.imageNumber = 0;
	}

	public UUID getRecipeId() {
		return this.recipeId;
	}
	
	public void setRecipeId(UUID uuid) {
		this.recipeId = uuid;
	}

	public String getName() {
		return this.title;
	}

	public void createUUID() {
		this.recipeId = UUID.randomUUID();
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

	public ArrayList<String> getQuantities() {
		return this.quantities;
	}

	public ArrayList<String> getUnits() {
		return this.units;
	}

	public void setName(String name) {
		this.title = name;
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

	public void setQuantities(ArrayList<String> quantities) {
		this.quantities = quantities;
	}

	public void setUnits(ArrayList<String> units) {
		this.units = units;
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
		return this.title + " by " + this.creatorEmail + "\n"
				+ this.description;
	}

	/**
	 * @author Torboto
	 * 
	 *         Enum for where a recipe is location, either in external memory or
	 *         on the elasticsearch server.
	 */
	static public enum Location {
		LOCAL, SERVER;
	}
}
