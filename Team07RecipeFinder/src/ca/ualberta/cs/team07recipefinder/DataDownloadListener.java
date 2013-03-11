package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;

/**
 * @author Torboto
 * Abstract type to be used as a trigger for async tasks to return data back to activity.
 */
public abstract interface DataDownloadListener {	
	void dataDownloadedSuccessfully(ArrayList<Recipe> data);
}