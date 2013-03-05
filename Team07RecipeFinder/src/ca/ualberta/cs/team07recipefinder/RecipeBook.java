package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;


import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/*
 * GC:
 * A model class that contains a list of the user's cached recipes, and
 * methods for saving recipes, deleting recipes, and searching for recipes
 * webservice: http://cmput301.softwareprocess.es:8080/CMPUT301W13T07/
 */

public class RecipeBook extends Model<View> {
	// GC: a list that contains all of the user's locally cached recipes.
	private ArrayList<Recipe> recipes;
	
	// GC: Http Connector
	private HttpClient httpclient = new DefaultHttpClient();

	// GC: JSON Utilities
	private Gson gson = new Gson();
	
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
		writeRecipeToCache(recipe);
		writeRecipeToServer();
	}
	
	/*
	 * GC:
	 * Adds a recipe to the user's locally cached recipes
	 */
	public void writeRecipeToCache(Recipe recipe){
		
	}
	
	/*
	 * ET:
	 * Writes recipe to the webservice. Follows format of lab ESClient Demo.
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public void writeRecipeToServer(){
		/*HttpPost httpPost = new HttpPost(
				"http://cmput301.softwareprocess.es:8080/CMPUT301W13T07/"
								+recipe.getId());
		StringEntity stringentity = null;
		*/
	}
	
	/* 
	 * GC:
	 * Deletes a recipe from the user's locally cached recipes
	 */
	public void deleteRecipe(){
			
	}
}
