package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

/*
 * GC:
 * methods for saving recipes, deleting recipes, and searching for recipes
 * webservice: http://cmput301.softwareprocess.es:8080/CMPUT301W13T07/
 */
public class HttpClient {

	private DefaultHttpClient httpClient = new DefaultHttpClient();
	private Gson gson = new Gson();

	public void writeRecipe(Recipe recipe) {

	}

	public Recipe readRecipe(UUID uuid) {
		return null;
	}
	
	public void updateRecipe(){
		
	}

	public RecipeBook searchRecipes(ArrayList<String> ingredients) {
		return null;
	}

	public RecipeBook searchRecipes(String title) {
		return null;
	}

}
