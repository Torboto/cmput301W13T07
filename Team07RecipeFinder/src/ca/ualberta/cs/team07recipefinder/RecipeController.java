package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.List;
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
	 */
	static public void writeRecipe(Recipe recipe, Context context) {
		boolean isConnected = checkInternetConnection(context);

		SqlClient sqlClient = new SqlClient(context);

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
		boolean isConnected = checkInternetConnection(context);
		SqlClient client = new SqlClient(context);
		client.updateRecipe(uuid, recipe);

		// change the isUpdated boolean within recipe to indicate that changes
		// tot he recipe have been made
		recipe.setIsUpdated(true);

		if (isConnected) {
			updateServerRecipe(recipe);
		}
		return;
	}

	/**
	 * Retrieves a recipe from the local database based on recipeId.
	 * 
	 * @param uuid
	 * @param context
	 * @return Recipe object from local device
	 */
	static public Recipe getLocalRecipe(UUID uuid, Context context) {
		SqlClient sqlClient = new SqlClient(context);
		Recipe recipe = sqlClient.readRecipe(uuid);
		return recipe;
	}

	static public void getServerRecipe(UUID uuid, Context context) {
		List<Recipe> recipeResults = new ArrayList<Recipe>();

		SearchRecipeTask search = new SearchRecipeTask(uuid);

		search.setDataDownloadListener(new DataDownloadListener() {
			public void dataDownloadedSuccessfully(ArrayList<Recipe> data) {
				HttpClient httpClient = new HttpClient();
				httpClient.deleteRecipe(data.get(0).recipeId);
				httpClient.writeRecipe(data.get(0));
			}
		});
		search.execute("");
	}

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
	 * @return Boolean on whether device is online or not.
	 */
	static public boolean checkInternetConnection(Context context) {
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

	/**
	 * 
	 * @param context
	 */
	static public void synchronize(Context context) {
		SqlClient sqlClient = new SqlClient(context);
		HttpClient httpClient = new HttpClient();
		ArrayList<Recipe> localRecipes = sqlClient.getAllRecipes();

		for (Recipe localRecipe : localRecipes) {
			Recipe serverRecipe = httpClient.readRecipe(localRecipe.recipeId);
			if (serverRecipe.getIsUpdated() == true) {
				sqlClient.updateRecipe(localRecipe.recipeId, serverRecipe);
			}
			if (localRecipe.getIsUpdated() == true) {
				httpClient.updateRecipe(localRecipe);
			}
		}
	}

	/*
	 * Update the integer value that represents the number of saved images for a
	 * locally saved recipe.
	 */
	static public void updateImageNumber(Recipe recipe) {
		int imageNumber = ImageController.getNumberImages(recipe.getRecipeId(),
				Recipe.Location.LOCAL);
		recipe.setImageNumber(imageNumber);
	}

}
