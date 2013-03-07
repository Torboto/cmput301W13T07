package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


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
	        

//		findViewById(R.id.buttonAddRecipe).setOnClickListener(
//				new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						verifyUser();
//						// setError should return null if no flags have be set
//						// during verifyUser.
//						if (mEmailView.getError() == null
//								&& mPersonNameView.getError() == null) {
//							user.setEmail(mEmail);
//							user.setName(mPersonName);
//
//							user.Write(getApplicationContext());
//							launchMainActivity();
//						}
//					}
//				});

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
				/*Recipe read_recipe;
				SqlClient client = new SqlClient(MainActivity.this);
				HttpClient httpClient = new HttpClient();

				
				ArrayList<String> ingredients = new ArrayList<String>();
				ArrayList<String> images = new ArrayList<String>();;
				ingredients.add("punpkin");
				ingredients.add("pumpkin");
				images.add("img1");
				images.add("img2");
				String directions = "HULLO?";
				//UUID recipe_id = UUID.randomUUID();
				// UUID user_id = UUID.randomUUID();
				UUID recipe_id =  UUID.fromString("23812c08-02b9-427e-9520-453434f4c892");
				String user_id = "111-1-1-1-1";
				
				Recipe test_r = new Recipe("test1", "test_desc", ingredients, directions, user_id);
				
				
				//Recipe test_r = new Recipe("test1", "test_desc", ingredients, "DIRECTINOS", "ern@bleh.com");
				
				//client.addRecipe(test_r);
				//read_recipe = client.getRecipe(recipe_id);
				//client.updateRecipe(recipe_id, test_r);
				client.deleteRecipe(recipe_id);
				
				//TextView tv_test = (TextView) findViewById(R.id.textView_GCTesting);
				//tv_test.setText(String.valueOf(read_recipe.getRecipeId()));
				 
				
				//httpClient.writeRecipe(test_r);
				*/
			}
		});
		
	}
}
