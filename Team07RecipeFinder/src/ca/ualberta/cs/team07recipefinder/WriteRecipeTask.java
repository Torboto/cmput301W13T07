package ca.ualberta.cs.team07recipefinder;

import android.os.AsyncTask;

public class WriteRecipeTask extends AsyncTask<Recipe, Void, Void>{

	@Override
	protected Void doInBackground(Recipe... arg0) {
        HttpClient httpClient = new HttpClient();
        httpClient.writeRecipe(arg0[0]);
		return null;
	}
}
