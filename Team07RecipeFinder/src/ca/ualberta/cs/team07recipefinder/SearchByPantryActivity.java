package ca.ualberta.cs.team07recipefinder;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SearchByPantryActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_by_pantry);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_search_by_pantry, menu);
		return true;
	}

}
