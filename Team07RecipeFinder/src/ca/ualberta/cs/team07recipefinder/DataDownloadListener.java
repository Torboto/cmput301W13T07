package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;

public abstract interface DataDownloadListener {	
	void dataDownloadedSuccessfully(ArrayList<Recipe> data);
}