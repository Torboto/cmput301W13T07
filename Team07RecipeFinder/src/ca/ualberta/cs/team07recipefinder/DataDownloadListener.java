package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;

/**
 * Abstract type to be used as a trigger for async tasks to return data back to
 * activity.
 * 
 * @author Torboto
 */
public abstract interface DataDownloadListener {
	/**
	 * Overridable method that will be called when a download is successful from
	 * the server. This is how we pass data back to the activity when an async
	 * task finishes.
	 * 
	 * @param data
	 *            Array of recipes that match the search terms.
	 */
	void dataDownloadedSuccessfully(ArrayList<Recipe> data);
}