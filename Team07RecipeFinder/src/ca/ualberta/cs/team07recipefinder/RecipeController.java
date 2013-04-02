package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Writes and deletes recipes to the local database (cache) and can
 * update/write/delete . Also contains the logic that determines if the device
 * is connected to the internet. Without internet access, no Recipes write to
 * the webservice.
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
	 *            Recipe object to be written.
	 * @param context
	 *            Context is required to access database.
	 */
	static public void writeRecipe(Recipe recipe, Context context) {
		SqlClient sqlClient = new SqlClient(context);
		sqlClient.writeRecipe(recipe);

		/*
		 * GC: Add the recipe to the webservice if there is an internet
		 * connection ET: You must call the async task to write as it makes an
		 * outside network call.
		 */
		boolean isConnected = checkInternetConnection(context);
		if (isConnected) {
			new WriteRecipeTask().execute(recipe);
		}
	}

	/**
	 * Deletes Recipes from the local database. Recipes cannot be deleted from
	 * the webservice.
	 * 
	 * @param uuid
	 *            UUID of recipe to be deleted
	 * @param context
	 *            Context is required to access database.
	 */
	static public void deleteLocalRecipe(UUID uuid, Context context) {
		SqlClient client = new SqlClient(context);
		client.deleteRecipe(uuid);
		return;
	}

	/**
	 * Write over old recipe with the same UUID
	 * 
	 * @param uuid
	 *            Recipe id.
	 * @param recipe
	 *            Recipe object holding new data to be updated.
	 * @param context
	 *            Context is required to access database.
	 */
	// TODO: ET- Does this need to take in a uuid? recipe should have same UUID.
	static public void updateRecipe(UUID uuid, Recipe recipe, Context context) {
		boolean isConnected = checkInternetConnection(context);
		SqlClient client = new SqlClient(context);
		client.updateRecipe(uuid, recipe);

		// change the isUpdated boolean within recipe to indicate that changes
		// to the recipe have been made
		recipe.setIsUpdated(true);

		if (isConnected) {
			updateServerRecipe(recipe);
		}
	}

	/**
	 * Retrieves a recipe from the local database based on recipeId.
	 * 
	 * @param uuid
	 *            UUID of recipe to be retrieved
	 * @param context
	 *            Context is required to access database.
	 * @return Recipe object from local device
	 */
	static public Recipe getLocalRecipe(UUID uuid, Context context) {
		SqlClient sqlClient = new SqlClient(context);
		Recipe recipe = sqlClient.readRecipe(uuid);
		return recipe;
	}

	/**
	 * Deletes old recipe with the same UUID, rewrites it with the new one.
	 * 
	 * @param recipe
	 *            Recipe with updated data.
	 */
	static public void updateServerRecipe(Recipe recipe) {
		HttpClient httpClient = new HttpClient();
		httpClient.updateRecipe(recipe);
	}

	/**
	 * GC: Check for an active internet connection. Follows format from stack
	 * overflow post: http://stackoverflow.com/questions/4238921/
	 * android-detect-whether-there-is-an-internet-connection-available
	 * 
	 * @param context
	 *            Context is required to access database.
	 * @return Boolean on whether device is online or not.
	 */
	static public boolean checkInternetConnection(Context context) {
		boolean isConnected = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			isConnected = networkInfo != null && networkInfo.isAvailable()
					&& networkInfo.isConnected();
			return isConnected;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isConnected;
	}

	/**
	 * Check all local recipes to see if they have been updated. If so they are
	 * pushed to the server. Then check to see if any recipes in the local cache
	 * have updated copies on the sever. If so they are pulled down and
	 * overwrite the local copy.
	 * 
	 * @param context
	 *            Context is required to access database.
	 */
	static public void synchronize(Context context) {
		SqlClient sqlClient = new SqlClient(context);
		HttpClient httpClient = new HttpClient();
		ArrayList<Recipe> localRecipes = sqlClient.getAllRecipes();

		for (Recipe localRecipe : localRecipes) {
			if (localRecipe.getIsUpdated() == true) {
				httpClient.updateRecipe(localRecipe);
			}
			Recipe serverRecipe = httpClient.readRecipe(localRecipe.recipeId);
			if (serverRecipe == null) {
				httpClient.writeRecipe(localRecipe);
			} else if (serverRecipe.getIsUpdated() == true) {
				sqlClient.updateRecipe(localRecipe.recipeId, serverRecipe);
			}
		}
	}

}
