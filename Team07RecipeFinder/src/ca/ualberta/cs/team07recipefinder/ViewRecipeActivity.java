package ca.ualberta.cs.team07recipefinder;

import android.app.Activity;
import android.os.Bundle;

public class ViewRecipeActivity extends Activity {
	/* */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipe);
		
		Bundle extras = getIntent().getExtras();
		int code = extras.getInt("code");
		
		if (code == 0){
			// AS: if came from My Recipes
		} else if (code == 1) {
			// AS: if came from Search
		} else{
			// AS: the code should not get here
		}
	}

}
