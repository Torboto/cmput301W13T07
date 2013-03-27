package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Writes and deletes recipes to the local database (cache) and the webservice.
 * Also contains the logic that determines if the device is connected to the
 * internet. Without internet access, no Recipes write to the webservice.
 * 
 * @author gcoomber
 * 
 */
public class RecipeController {

	/**
	 * Writes Recipe to SQL local database, and if it has internet access also
	 * writes the Recipe to the webservice.
	 * 
	 * @param recipe
	 * @param context
	 * @author gcoomber
	 */
	static public void writeRecipe(Recipe recipe, Context context) {
		boolean isConnected;

		SqlClient sqlClient = new SqlClient(context);

		isConnected = checkInternetConnection(context);

		// GC: Add the recipe to the recipe database.
		sqlClient.writeRecipe(recipe);

		/*
		 * GC: Add the recipe to the webservice if there is an internet
		 * connection ET: You must call the async task to write as it makes an
		 * outside network call.
		 */
		if (isConnected) {
			new WriteRecipeTask().execute(recipe);
		}

	}

	/**
	 * Deletes Recipes from the local database. Recipes cannot be deleted from
	 * the webservice.
	 * 
	 * @param uuid
	 * @param context
	 */
	static public void deleteRecipe(UUID uuid, Context context) {
		SqlClient client = new SqlClient(context);
		client.deleteRecipe(uuid);
		return;
	}

	/*
	 * Write over old recipe with the same UUID
	 */
	// TODO: ET- Does this need to take in a uuid? recipe should have same UUID.
	static public void updateRecipe(UUID uuid, Recipe recipe, Context context) {
		SqlClient client = new SqlClient(context);
		client.updateRecipe(uuid, recipe);
		return;
	}

	/**
	 * Retrieves a recipe from the local database based on recipeId.
	 * 
	 * @param uuid
	 * @param context
	 * @return
	 */
	static public Recipe getLocalRecipe(UUID uuid, Context context) {
		SqlClient sqlClient = new SqlClient(context);
		Recipe recipe = sqlClient.readRecipe(uuid);
		return recipe;
	}

	/**
	 * @author Torboto Get recipe object from database.
	 * @param uuid
	 * @param context
	 * @return
	 */
	static public void getRecipeHTTP(UUID uuid, final Context context) {

//		SearchRecipeTask search = new SearchRecipeTask(uuid);
//
//		search.setDataDownloadListener(new DataDownloadListener() {
//			@SuppressWarnings("unchecked")
//			public void dataDownloadedSuccessfully(ArrayList<Recipe> data) {
//				context.returnSearchResultstoActivity(data);
//			}
//		});
//		search.execute("");
	}

	/*
	 * Only searches HTTP server
	 */
	static public void searchRecipeIngredients(ArrayList<String> ingredients) {
		// new SearchRecipeTask().execute(ingredients);

	}

	/*
	 * Only searches HTTP server
	 */
	static public void searchRecipeTitle(String title, Context context) {
		// List<Recipe> recipeResults = new ArrayList<Recipe>();
		// new SearchRecipeTask(title, null, null).execute("title");
		// SearchRecipeTask search = new SearchRecipeTask(null, null, uuid);
		//
		// search.setDataDownloadListener(new DataDownloadListener() {
		// @SuppressWarnings("unchecked")
		// public void dataDownloadedSuccessfully(ArrayList<Recipe> data) {
		// //context.
		// }
		// });
		// search.execute("");
	}
	
	/**
	 * GC: Check for an active internet connection. Follows format from stack
	 * overflow post: http://stackoverflow.com/questions/4238921/
	 * android-detect-whether-there-is-an-internet-connection-available
	 * 
	 * @param context
	 * @return
	 */
	static private boolean checkInternetConnection(Context context) {
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
