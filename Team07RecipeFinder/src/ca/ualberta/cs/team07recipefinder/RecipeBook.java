package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;


public class RecipeBook extends Model<View> {
	//ET: Current list of recipes returned from either a search, or grabbed from cache.
	private ArrayList<Recipe> recipes;
	
	public void addRecipe(Recipe recipe){
		recipes.add(recipe);
	}
}
