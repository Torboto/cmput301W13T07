package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;

import android.os.AsyncTask;

public class SearchRecipeTask extends
		AsyncTask<String, ArrayList<String>, Void> {

	@Override
	protected Void doInBackground(String... arg0) {
		HttpClient httpClient = new HttpClient();
		if (arg0[0] == "") {
			httpClient.searchRecipes(arg0[1]);
		} else {
			httpClient.searchRecipes(arg0[0]);
		}
		return null;
	}
}
