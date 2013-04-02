package ca.ualberta.cs.team07recipefinder;

import java.util.UUID;

import android.os.AsyncTask;

/**
 * Async task that deletes recipe from elasticsearch.
 * 
 * @author Torboto
 */
public class DeleteRecipeTask extends AsyncTask<UUID, Void, Void> {

	@Override
	protected Void doInBackground(UUID... arg0) {
		HttpClient httpClient = new HttpClient();
		httpClient.deleteRecipe(arg0[0]);
		return null;
	}
}
