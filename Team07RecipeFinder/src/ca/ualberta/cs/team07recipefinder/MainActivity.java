package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * Main activity that is launched when user has an account. It handles the
 * pantry, searching, and viewing local recipes.
 * 
 * @author Torboto
 * @author gcoomber
 * @author xiaohuim
 */
public class MainActivity extends Activity {

	private User user;
	private ListView ingredientsLV;
	private Pantry myPantry;

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
		myPantry = user.getPantry();
		ingredientsLV = (ListView) findViewById(R.id.lvPantry);
		registerForContextMenu(ingredientsLV);

		myPantry = user.getPantry();
	}

	@Override
	public void onResume() {
		super.onResume();

		// GC: Populate and set click listener for items in the myRecipes
		// listview
		populateMyRecipes();
		populatePantry();

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

		findViewById(R.id.buttonSynch).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						RecipeController.synchronize(getApplicationContext());
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
						if (RecipeController
								.checkInternetConnection(getApplicationContext()) == false) {
							return;
						}
						EditText etSearchNameView = (EditText) findViewById(R.id.etSearchName);
						// AS: if edittext not empty then do a search
						if (!isEmpty(etSearchNameView))
							populateSearch(etSearchNameView.getText()
									.toString(), null);
					}
				});

		// AS: listener for Pantry Search button
		findViewById(R.id.buttonPantrySearch).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent pantrySearchIntent = new Intent(
								getApplicationContext(),
								SearchByPantryActivity.class);
						startActivityForResult(pantrySearchIntent, 1);
					}
				});
	}

	/**
	 * 
	 * Takes in a listview, and an array of recipes to bind to an
	 * onItemClickListener These variables must be passed in as final so that
	 * the listener declared inside may access them.
	 * 
	 * @param listView
	 *            listview to bind to recipes param
	 * @param recipes
	 *            list of recipes to bind to given listview
	 */
	protected void setListViewOnClickListener(final ListView listView,
			final ArrayList<Recipe> recipes) {

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// GC: get the id of the recipe the user selects
				UUID recipeId = recipes.get(position).getRecipeId();

				// GC: Start the ViewRecipeActivity
				Intent viewRecipeIntent = new Intent(getApplicationContext(),
						ViewRecipeActivity.class);

				if (listView.getId() == R.id.lvMyRecipes) {
					// GC: add code 1 to intent to indicate coming from
					// MyRecipes
					viewRecipeIntent.putExtra("code", 1);
				}
				if (listView.getId() == R.id.lvSearchResults) {
					// ET: add code 2 to intent to indicate coming from Search
					// tab
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
					getBaseContext(), android.R.layout.simple_list_item_1,
					recipes);
			listView.setAdapter(adapter);
		}
	}

	/**
	 * This will show a dialog for user to input a ingredient and save it to the
	 * pantry. If the input is null, it will call the showInvalidInputWaring()
	 * to show warn the user.
	 * 
	 * @param v
	 * 
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
					myPantry.addIngredient(ingredientName);
					user.setPantry(myPantry);
					user.Write(getApplicationContext());
					populatePantry();
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

	/**
	 * Will show a dialog says the input is invalid
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
	 * This will show a dialog with a EditText and update the ingredient of the
	 * given index
	 * 
	 * @param index
	 * 
	 */
	private void editIngredient(final int index) {
		final EditText et = new EditText(this);
		et.setText(myPantry.getIngredient(index));
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Edit Ingredient");
		alert.setView(et);
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				myPantry.updateIngredient(index, et.getText().toString());
				populatePantry();
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
	 * If user long-press on item in the pantry listview and this menu will show.
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
	 * If click on an item in the ConetextMenu and the corresponding method will
	 * be called.
	 */
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int index = info.position;
		int ID = item.getItemId();
		switch (item.getItemId()) {
		case R.id.menu_delete_ingredient:
			myPantry.removeIngredient(index);
			user.setPantry(myPantry);
			user.Write(getApplicationContext());
			break;
		case R.id.menu_edit_ingredient:
			editIngredient(index);
			user.setPantry(myPantry);
			user.Write(getApplicationContext());
			break;
		// default:
		// return super.onContextItemSelected(item);
		}
		populatePantry();
		return true;
	}

	/**
	 * 
	 */
	public void populatePantry() {
		ingredientsLV = (ListView) findViewById(R.id.lvPantry);
		ArrayList<String> ingredients = myPantry.getAllIngredients();
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

	/**
	 * For MyRecipes Tab. Retrieves all recipes from the cache and lists their
	 * names and returns the list of retrieved recipes
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
	 * This method is called when a search is done, it creates an async task, as
	 * well as defining a DataDownloadListener which is a type that will pass
	 * the data from the async task back to setSearch method.
	 */
	protected void populateSearch(String titleKeyword,
			ArrayList<String> ingredients) {
		SearchRecipeTask search = null;
		if (titleKeyword != null) {
			search = new SearchRecipeTask(titleKeyword, null);
		} else if (ingredients != null) {
			search = new SearchRecipeTask(null, ingredients);
		}

		search.setDataDownloadListener(new DataDownloadListener() {
			public void dataDownloadedSuccessfully(ArrayList<Recipe> data) {
				setSearch(data);
			}
		});
		search.execute("");
	}

	/**
	 * This method is called by the async task once it has completed it's call
	 * from the server.
	 * 
	 * @param recipes
	 *            receives list of recipes that matched the search
	 */
	public void setSearch(ArrayList<Recipe> recipes) {
		if (recipes.size() == 0) {
			noSearchResults();
		}
		ListView searchListView = (ListView) findViewById(R.id.lvSearchResults);
		ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(
				getBaseContext(), android.R.layout.simple_list_item_1, recipes);
		searchListView.setAdapter(adapter);

		setListViewOnClickListener(searchListView, recipes);
	}

	/**
	 * This method creates a dialog which informs the user that there are no
	 * search results to return
	 */
	private void noSearchResults() {
		TextView tv = new TextView(this);
		tv.setText("No recipes match this search. Try creating one!");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Sorry");
		alert.setView(tv);
		alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}

	/**
	 * This method takes an EditText and returns true if it is empty and false
	 * otherwise.
	 * 
	 * @param etText
	 *            the EditText to be tested
	 * @return True: if empty, false: otherwise.
	 */
	private boolean isEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0)
			return false;
		else
			return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			ArrayList<String> search_list = data
					.getStringArrayListExtra("ingredients_list");
			populateSearch(null, search_list);
		}
	}

}
