package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;

//GC: The controller for the RecipeBook model class.
public class RecipeBookController {

	public void createRecipe(String name, 
			String description, 
			ArrayList<String> ingredients, 
			ArrayList<String> images, 
			int recipeId, 
			int creatorId){
		RecipeBook recipeBook = new RecipeBook();
		recipeBook.createRecipe(name, description, ingredients,
				images, recipeId, creatorId);
	}
}
