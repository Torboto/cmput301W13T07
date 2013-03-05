package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;

/*
 * GC:
 * A model class that contains a list of the user's cached recipes, and
 * methods for saving recipes, deleting recipes, and searching for recipes
 */

public class RecipeBook extends Model<View> {
	// GC: a list that contains all of the user's locally cached recipes.
	private ArrayList<Recipe> recipes;
	
	public void createRecipe(String name, 
			String description, 
			ArrayList<String> ingredients, 
			ArrayList<String> images, 
			int recipeId, 
			int creatorId){
		
		Recipe recipe = new Recipe(
				name, 
				description, 
				ingredients, 
				images, 
				recipeId, 
				creatorId);
		
		recipes.add(recipe);
		writeRecipeToCache();
		writeRecipeToServer();
	}
	
	/*
	 * GC:
	 * Adds a recipe to the user's locally cached recipes
	 */
	public void writeRecipeToCache(){

	}
	
	/*
	 * ET:
	 * Writes recipe to the server
	 */
	public void writeRecipeToServer(){
	
	}
	
	/* 
	 * GC:
	 * Deletes a recipe from the user's locally cached recipes
	 */
	public void deleteRecipe(){
			
	}
}
