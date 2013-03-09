package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


public class MainActivity extends Activity {
	

	private Button addIngredientButton;
	private User user;
	private ListView ingredientsLV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("tab_pantry");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("Pantry");

		TabSpec spec2 = tabHost.newTabSpec("tab_search");
		spec2.setIndicator("Search");
		spec2.setContent(R.id.tab2);

		TabSpec spec3 = tabHost.newTabSpec("tab_myrecipes");
		spec3.setIndicator("MyRecipes");
		spec3.setContent(R.id.tab3);

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);

		findViewById(R.id.buttonSearch).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						ListView listView = (ListView) findViewById(R.id.lvSearchResults);

						//ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(
//								this, android.R.layout.simple_list_item_1,
//								RecipeController.searchRecipeTitle(etSearchName));
						//listView.setAdapter(adapter);
					}
				});

		user = User.getInstance();
		addIngredientButton = (Button) findViewById(R.id.buttonAdd);

		// MA: Call addIngredient() when click on Add button
		addIngredientButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addIngredient(v);
			}
		});

		// findViewById(R.id.buttonAddRecipe).setOnClickListener(
		// new View.OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// verifyUser();
		// // setError should return null if no flags have be set
		// // during verifyUser.
		// if (mEmailView.getError() == null
		// && mPersonNameView.getError() == null) {
		// user.setEmail(mEmail);
		// user.setName(mPersonName);
		//
		// user.Write(getApplicationContext());
		// launchMainActivity();
		// }
		// }
		// });

	}

	// MA: will show a dialog for user to input a ingredient and save it to
	// Pantry.
	protected void addIngredient(final View v) {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Add New Ingredient");
		final EditText input = new EditText(this);
		input.setHint("Please enter the ingredient");
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String ingredientName = input.getText().toString();
				if (TextUtils.isEmpty(ingredientName))
					showInvalidInputWaring();
				else {
					Pantry p = user.getPantry();
					p.addIngredient(ingredientName);
					user.setPantry(p);
					onStart();
				}
			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
		alert.show();		
	}

	// MA: will show a dialog says the input is invalid
	protected void showInvalidInputWaring() {
		TextView tv = new TextView(this);
		tv.setText("Invalid input.\nPlease try again.");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Sorry!");
		alert.setView(tv);
		alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}

	@Override
	/*
	 * MA: will load and show all ingredients in the ListView under Pantry tab.
	 */
	protected void onStart() {
		super.onStart();
		ingredientsLV = (ListView) findViewById(R.id.lvPantry);
		ArrayList<String> ingredients = user.getPantry().getAllIngredients();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, ingredients);
		ingredientsLV.setAdapter(adapter);
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
		final ArrayList<Recipe> recipes = populateMyRecipes();
		
		// GC: Click listener for items in the recipe listview
		ListView recipeListView = (ListView) findViewById(R.id.lvMyRecipes);
		recipeListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//GC: get the id of the recipe the user selects
				UUID recipeId = recipes.get(position).getRecipeId();
				
				// GC: Start the ViewRecipeActivity
				Intent viewRecipeIntent = new Intent(getApplicationContext(),
						ViewRecipeActivity.class);
				
				// GC: add code 1 to intent to indicate coming from MyRecipes
				viewRecipeIntent.putExtra("code", 1);
				
				// GC: add recipeId as a string to the intent
				viewRecipeIntent.putExtra("recipeId", recipeId.toString());
				startActivity(viewRecipeIntent);
				
			}

		});
		/*
		 * Clicklistener for the add button. When the add button is clicked the
		 * NewRecipeActivity is launched.
		 */
		Button addButton = (Button) findViewById(R.id.buttonAddRecipe);

		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//GC: The add button starts the NewRecipeActivity
				Intent newRecipeIntent = new Intent(getApplicationContext(),
						NewRecipeActivity.class);
				startActivity(newRecipeIntent);
			}
		});

	}

	/* GC: For MyRecipes Tab. Retrieves all recipes from the cache and lists
	 *  their names and returns the list of retrieved recipes*/
	public ArrayList<Recipe> populateMyRecipes() {
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		ArrayList<String> recipeNames = new ArrayList<String>();
		ListView recipeListView = (ListView) findViewById(R.id.lvMyRecipes);

		// GC: Obtain all the recipes in the cache as an ArrayList
		SqlClient client = new SqlClient(this);
		recipes = client.getAllRecipes();

		// GC: Do not add recipes to the ListView if the cache is empty
		if (recipes != null) {

			for (Recipe recipe : recipes) {
				recipeNames.add(recipe.getName());
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.list_item, recipeNames);
			recipeListView.setAdapter(adapter);
		}
		
		return recipes;
	}
}
