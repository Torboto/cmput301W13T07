package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.os.AsyncTask;

/**
 * @author Torboto
 * Async task to get search results from elastic search server. 
 * Has a DataDownloadListener type to allow the passing of data back to the activity.
 * DataDownloadListener calls DownloadSuccessful method when the task is
 * complete, which then calls a method inside of Main Activity.
 */
public class SearchRecipeTask extends AsyncTask<String, List<String>, Void> {
	/**
	 * type that is overridden so as to call a method inside the calling activity to pass results back.
	 */
	DataDownloadListener dataListener;
	/**
	 *  list of recipes that is returned from the call to the elastic search server.
	 */
	ArrayList<Recipe> recipeResults;
	/**
	 * keyword to search for in titles of recipes
	 */
	String recipeTitleKeyword;
	/**
	 * list of ingredients to search for in recipes
	 */
	List<String> recipeIngredients;
	UUID recipeId;

	
	/**
	 * Constructor that sets member variables
	 * @param recipeTitleKeyword keyword to search for in recipe titles
	 * @param ingredients list of ingredients to search for in recipe
	 */
	SearchRecipeTask(String recipeTitleKeyword, List<String> ingredients) {
		this.recipeTitleKeyword = recipeTitleKeyword;
		this.recipeIngredients = ingredients;
	}

	public SearchRecipeTask(UUID recipeId) {
		this.recipeId = recipeId;
	}

	/**
	 * Create a DataDownloadListener type to pass data out of async task.
	 * @param downloadListener object to trigger when async task is done and pass data back out
	 */
	public void setDataDownloadListener(DataDownloadListener downloadListener) {
		this.dataListener = downloadListener;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Void doInBackground(String... arg0) {
		ArrayList<Recipe> results = new ArrayList<Recipe>();
		HttpClient httpClient = new HttpClient();
		if (recipeTitleKeyword != null ) {
			results = httpClient.searchRecipes(recipeTitleKeyword, null);
		}
		else if (recipeIngredients != null){
			results = httpClient.searchRecipes(recipeIngredients);
		}
		else if (recipeId != null){
			results.add(httpClient.readRecipe(recipeId));
		}
		recipeResults = results;

		return null;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Void result) {
		dataListener.dataDownloadedSuccessfully(this.recipeResults);
	}

}
