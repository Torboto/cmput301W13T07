package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * @author Torboto
 * @author gcoomber
 * @author xiaohuim
 * 
 * Main activity that is launched when user has an account. It handles the pantry, searching, and viewing local recipes.
 */
public class MainActivity extends Activity {

	private User user;
	private ListView ingredientsLV;
	private Button addIngredientButton;

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

		user = User.getInstance();
		addIngredientButton = (Button) findViewById(R.id.buttonAddIngredient);
		ingredientsLV = (ListView) findViewById(R.id.lvPantry);
		registerForContextMenu(ingredientsLV);
	}

	@Override
	public void onResume() {
		super.onResume();

		// GC: Populate and set click listener for items in the myRecipes listview
		populateMyRecipes();

		// GC: Clicklistener for the add recipes button. When the add button is
		// clicked the NewRecipeActivity is launched.
		findViewById(R.id.buttonAddRecipe).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// GC: The add button starts the NewRecipeActivity
						Intent newRecipeIntent = new Intent(
								getApplicationContext(),
								NewRecipeActivity.class);
						startActivity(newRecipeIntent);
					}
				});

		// MA: Call addIngredient() when click on Add Ingredients button
		findViewById(R.id.buttonAddIngredient).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						addIngredient(v);
					}
				});

		// ET: listener for Search button
		findViewById(R.id.buttonSearch).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						populateSearch();
					}
				});

	}

	/**
	 * @author Torboto
	 * @author gcoomber
	 * 
	 * Takes in a listview, and an array of recipes to bind to an onItemClickListener
	 * These variables must be passed in as final so that the listener declared inside may access them.
	 * 
	 * @param listView listview to bind to recipes param
	 * @param recipes list of recipes to bind to given listview
	 */
	protected void setListViewOnClickListener(final ListView listView, final ArrayList<Recipe> recipes) {

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// GC: get the id of the recipe the user selects
				UUID recipeId = recipes.get(position).getRecipeId();

				// GC: Start the ViewRecipeActivity
				Intent viewRecipeIntent = new Intent(getApplicationContext(),
						ViewRecipeActivity.class);

				if(listView.getId() == R.id.lvMyRecipes){
				// GC: add code 1 to intent to indicate coming from MyRecipes
				viewRecipeIntent.putExtra("code", 1);
				}
				if(listView.getId() == R.id.lvSearchResults){
				// ET: add code 2 to intent to indicate coming from Search tab
				viewRecipeIntent.putExtra("code", 2);
				}
				
				// GC: add recipeId as a string to the intent
				viewRecipeIntent.putExtra("recipeId", recipeId.toString());
				startActivity(viewRecipeIntent);
			}
		});
		

		// GC: Do not add recipes to the ListView if the cache is empty
		if (recipes != null) {
		ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(
				getBaseContext(), android.R.layout.simple_list_item_1, recipes);
		listView.setAdapter(adapter);
		}
	}

	/*
	 * MA: will show a dialog for user to input a ingredient and save it to
	 * pantry
	 */
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

	/*
	 * MA: Will show a dialog says the input is invalid
	 */
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
	
	/**
	 * MA: show a dialog with a EditText and update the ingredient of the given
	 * index
	 */
	private void editIngredient(final int index) {
		final EditText et = new EditText(this);
		et.setText(user.getPantry().getIngredient(index));
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Edit Ingredient");
		alert.setView(et);
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				user.getPantry().updateIngredient(index, et.getText().toString());
				onStart();
			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
		alert.show();
	}

	@Override
	/**
	 * MA: long press on item in the pantry listview and this menu will show
	 */
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(getString(R.string.menu_pantry_title));
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_edit_pantry, menu);
	}

	@Override
	/**
	 * MA: click on an item in the ConetextMenu and the corresponding method will
	 * be called
	 */
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int index = info.position;
		switch (item.getItemId()) {
		case R.id.menu_delete_ingredient:
			user.getPantry().removeIngredient(index);
			onStart();
			return true;
		case R.id.menu_edit_ingredient:
			editIngredient(index);
			onStart();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	/*
	 * MA: Will load and show all ingredients in the ListView under Pantry tab.
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

	/*
	 * GC: For MyRecipes Tab. Retrieves all recipes from the cache and lists
	 * their names and returns the list of retrieved recipes
	 */
	public void populateMyRecipes() {
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();

		ListView recipeListView = (ListView) findViewById(R.id.lvMyRecipes);

		// GC: Obtain all the recipes in the cache as an ArrayList
		SqlClient client = new SqlClient(this);
		recipes = client.getAllRecipes();

		setListViewOnClickListener(recipeListView, recipes);
	}

	/**
	 * @author Torboto
	 * This method is called when a search is done, it creates an async
	 * task, as well as defining a DataDownloadListener which is a type that
	 * will pass the data from the async task back to setSearch method.
	 */
	protected void populateSearch() {
		EditText etSearchNameView = (EditText) findViewById(R.id.etSearchName);
		EditText etSearchIngredientsView = (EditText) findViewById(R.id.etIngredientsList);
		// TODO Convert everything to search ingredients with a string, instead
		// of an array
		SearchRecipeTask search = new SearchRecipeTask(etSearchNameView
				.getText().toString(), null);

		search.setDataDownloadListener(new DataDownloadListener() {
			@SuppressWarnings("unchecked")
			public void dataDownloadedSuccessfully(ArrayList<Recipe> data) {
				setSearch(data);
			}
		});
		search.execute("");
	}

	/**
	 * 	 * @author Torboto
	 * This method is called by the async task once it has completed it's
	 * call from the server.
	 * @param recipes receives list of recipes that matched the search
	 */
	public void setSearch(ArrayList<Recipe> recipes) {
		ListView searchListView = (ListView) findViewById(R.id.lvSearchResults);
		ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(
				getBaseContext(), android.R.layout.simple_list_item_1, recipes);
		searchListView.setAdapter(adapter);
		
		setListViewOnClickListener(searchListView, recipes);
	}
}
