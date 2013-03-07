package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.net.ConnectivityManager;

public class RecipeController {

	public RecipeController() {
	}


	/*
	 * Writes to SQL local database, and if it has internet access also writes
	 * to HTTP.
	 */
	public void writeRecipe(Recipe recipe) {
		boolean isConnected;
		
		// GC: Check if the user is connected to the internet.
		//isConnected = checkInternetConnection();
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
	 * GC: Check for an active internet connection
	 *
	private boolean checkInternetConnection(){
	    boolean isWifiConnected = false;
	    boolean isMobileConnected = false;

	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}*/
}
