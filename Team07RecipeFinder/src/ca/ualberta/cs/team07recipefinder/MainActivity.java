package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	        TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
	        tabHost.setup();

	        TabSpec spec1=tabHost.newTabSpec("tab_pantry");
	        spec1.setContent(R.id.tab1);
	        spec1.setIndicator("Pantry");

	        TabSpec spec2=tabHost.newTabSpec("tab_search");
	        spec2.setIndicator("Search");
	        spec2.setContent(R.id.tab2);

	        TabSpec spec3=tabHost.newTabSpec("tab_myrecipes");
	        spec3.setIndicator("MyRecipes");
	        spec3.setContent(R.id.tab3);

	        tabHost.addTab(spec1);
	        tabHost.addTab(spec2);
	        tabHost.addTab(spec3);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		//GC: MyRecipes Tab
		// This stuff is temporary stuff for testing purposes. Dont freak out.
		// Click listener for the 'cancel' button. The click listener follows
		// the format of that of the LonelyTwitter app. 
		Button addButton = (Button) findViewById(R.id.buttonAddRecipe);

		addButton.setOnClickListener(new View.OnClickListener() {
			// The cancel button ends the NewEntryActivty activity.
			public void onClick(View v) {
				String test_string = "blah";
				Recipe read_recipe;
				SqlClient client = new SqlClient(MainActivity.this);
				
				ArrayList<String> ingredients = new ArrayList<String>();
				ArrayList<String> images = new ArrayList<String>();;
				ingredients.add("fish");
				ingredients.add("cats");
				images.add("img1");
				images.add("img2");
				UUID recipe_id = UUID.randomUUID();
				UUID user_id = UUID.randomUUID();
				
				//Recipe test_r = new Recipe("test1", "test_desc", ingredients, images,
				//		recipe_id, user_id);
				
				//client.addRecipe(test_r);
				//read_recipe = client.getRecipe(recipe_id);
				
			//	TextView tv_test = (TextView) findViewById(R.id.textView_GCTesting);
			//	tv_test.setText(String.valueOf(read_recipe.getRecipeId()));
			}
		});
		
	}
}
