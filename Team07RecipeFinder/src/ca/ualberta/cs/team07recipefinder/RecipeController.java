package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
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
		
		HttpClient httpClient = new HttpClient();
		SqlClient client = new SqlClient(context);
		
		// GC: Check if the user is connected to the internet.
		isConnected = checkInternetConnection(context);
		
		// GC: Add the recipe to the recipe database.
		client.addRecipe(recipe);
		
		/*  GC: Add the recipe to the webservice if there is an internet
		 *  connection*/
		if( isConnected )
		{
			httpClient.writeRecipe( recipe );
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
	 * Only searches HTTP server
	 */
	public RecipeBook searchRecipeIngredients(ArrayList<String> ingredients) {
		return null;
	}

	/*
	 * Only searches HTTP server
	 */
	public RecipeBook searchRecipeTitles(String title) {
		return null;
	}
	
	/*
	 * GC: Check for an active internet connection. Follows format from 
	 * stack overflow post: http://stackoverflow.com/questions/4238921/
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
