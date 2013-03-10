package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

public class SearchRecipeTask extends
		AsyncTask<String, List<String>, Void> {
	DataDownloadListener mahDataListenerThing;
	ArrayList<Recipe> recipeResults;
	String recipeName;
	List<String> ingredients;
	

	SearchRecipeTask(String recipeName, List<String> ingredients){
		this.recipeName = recipeName;
		this.ingredients = ingredients;
	}
	
	public void setDataDownloadListener(DataDownloadListener downloadListener) {
		this.mahDataListenerThing = downloadListener;
	}
	
	@Override
	protected Void doInBackground(String... arg0) {
		ArrayList<Recipe> results = new ArrayList<Recipe>();
		HttpClient httpClient = new HttpClient();
		if (recipeName == "") {
			results = httpClient.searchRecipes(ingredients);
		} else {
			results = httpClient.searchRecipes(recipeName);
		}
		recipeResults = results;

		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		mahDataListenerThing.dataDownloadedSuccessfully(this.recipeResults);
	}

	
}
