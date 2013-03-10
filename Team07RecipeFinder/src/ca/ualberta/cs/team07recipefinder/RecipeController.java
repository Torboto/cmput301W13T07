package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RecipeController {

	public RecipeController() {
	}

	/*
	 * Writes to SQL local database, and if it has internet access also writes
	 * to HTTP.
	 */
	public void writeRecipe(Recipe recipe, Context context) {
		boolean isConnected;

		SqlClient client = new SqlClient(context);

		// GC: Check if the user is connected to the internet.
		isConnected = checkInternetConnection(context);

		// GC: Add the recipe to the recipe database.
		client.addRecipe(recipe);

		/*
		 * GC: Add the recipe to the webservice if there is an internet
		 * connection
		 * ET: You must call the async task to write as it makes an outside network call.
		 */
		if (isConnected) {
			// DEBUG
			// ArrayList<String> ingredients = new ArrayList<String>();
			// ingredients.add("fish");
			// ingredients.add("cats");
			// recipe = new Recipe("test1", "test_desc",
			// ingredients, "DIRECTINOS", "ern@bleh.com");
			// ENDDEBUG
			new WriteRecipeTask().execute(recipe);
		}

	}

	/*
	 * Only deletes from SQL local database
	 */
	public void deleteRecipe(UUID uuid) {
	}

	/*
	 * Write over old recipe with the same UUID
	 */
	public void updateRecipe(Recipe recipe) {
	}

	/*
	 * Get the recipe with a given ID
	 */
	public Recipe getRecipe(UUID uuid, Context context) {
		SqlClient client = new SqlClient(context);
		Recipe recipe = client.getRecipe(uuid);
		return recipe;
	}
	
	/*
	 * Only searches HTTP server
	 */
	public List<Recipe> searchRecipeIngredients(ArrayList<String> ingredients) {
		// new SearchRecipeTask().execute(ingredients);
		return null;
	}

	/*
	 * Only searches HTTP server
	 */
	static public List<Recipe> searchRecipeTitle(String title) {
		List<Recipe> recipeResults = new ArrayList<Recipe>();
		//new SearchRecipeTask().execute("test1");

		return recipeResults;
	}

	/*
	 * GC: Check for an active internet connection. Follows format from stack
	 * overflow post: http://stackoverflow.com/questions/4238921/
	 * android-detect-whether-there-is-an-internet-connection-available
	 */
	private boolean checkInternetConnection(Context context) {
		boolean isConnected = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			isConnected = networkInfo != null && networkInfo.isAvailable()
					&& networkInfo.isConnected();
			return isConnected;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isConnected;
	}
}
