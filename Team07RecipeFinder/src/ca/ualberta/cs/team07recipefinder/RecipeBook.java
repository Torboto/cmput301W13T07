package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class RecipeBook extends Model<View> {
	//ET: Current list of recipes returned from either a search, or grabbed from cache.
	private ArrayList<Recipe> recipes;
	
	public void addRecipe(Recipe recipe){
		recipes.add(recipe);
	}
}
