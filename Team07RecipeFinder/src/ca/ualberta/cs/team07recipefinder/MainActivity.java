package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
		
		// GC: MyRecipes Tab
		
		// GC: Show all locally saved recipes in a ListView.
		populateMyRecipes();
		
		/* Clicklistener for the add button. When the add button is clicked the
		 * NewRecipeActivity is launched.*/
		Button addButton = (Button) findViewById(R.id.buttonAddRecipe);

		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Recipe read_recipe;
				SqlClient client = new SqlClient(MainActivity.this);

				ArrayList<String> ingredients = new ArrayList<String>();
				ArrayList<String> images = new ArrayList<String>();;
				ingredients.add("fish");
				ingredients.add("cats");
				images.add("img1");
				images.add("img2");
				
				Recipe test_r = new Recipe("test1", "test_desc", ingredients, "DIRECTINOS", "ern@bleh.com");
				
				HttpClient httpClient = new HttpClient();
				//httpClient.writeRecipe(test_r);
				
				//client.addRecipe(test_r);
				//read_recipe = client.getRecipe(recipe_id);
				
			//	TextView tv_test = (TextView) findViewById(R.id.textView_GCTesting);
			//	tv_test.setText(String.valueOf(read_recipe.getRecipeId()));
				
				new WriteRecipeTask().execute(test_r);
		
				// GC: The add button starts the NewRecipeActivity
				Intent newRecipeIntent =
						new Intent(getApplicationContext(), 
								NewRecipeActivity.class);
				startActivity(newRecipeIntent);
			}
		});
		
	}
	
	// GC: Retrieves all recipes from the cache and lists their names
	public void populateMyRecipes()
	{
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		ArrayList<String> recipeNames = new ArrayList<String>();
		ListView recipeListView = (ListView) findViewById(R.id.lvMyRecipes);
		
		// GC: Obtain all the recipes in the cache as an ArrayList
		SqlClient client = new SqlClient(this);
		recipes = client.getAllRecipes();
		
		// GC: Do not add recipes to the ListView if the cache is empty
		if (recipes != null) {
			
			for(Recipe recipe : recipes){
				recipeNames.add(recipe.getName());
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.list_item, recipeNames);
			recipeListView.setAdapter(adapter);
		}
	}
}
