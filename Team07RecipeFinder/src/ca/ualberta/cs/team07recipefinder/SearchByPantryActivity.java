package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author ajstarna
 * 
 *         This activity is launched when the user selects
 *         "Search Using My Pantry" from the Search tab. The user selects
 *         ingredients from their pantry, which are returned to the main
 *         activity as an ArrayList and used in an ingredients search.
 */
public class SearchByPantryActivity extends Activity {
	ListView ingredientsLV;
	User user;
	Set<Integer> indicesToSearch = new HashSet<Integer>();
	ArrayList<String> ingredientsToSearch = new ArrayList<String>();
	ArrayList<String> ingredients = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_by_pantry);

		setUpListView();
		setListViewOnClickListener();

		Button cancelButton = (Button) findViewById(R.id.button_cancel1);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// AS: The cancel button finnishes the activity
				// without returning anything
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);
				finish();
			}
		});

		Button searchButton = (Button) findViewById(R.id.button_search1);
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// AS: The search button grabs the needed ingredients using
				// indicesToSearch then returns them in ingredientsToSearch

				// clear the ingredients list every time so we don't
				// "double count" certain indices
				ingredientsToSearch.clear();

				// AS: only do a search if at least one ingredient selected
				if (indicesToSearch.isEmpty()) {
					emptySearchDialog();
				} else {
					for (Integer i : indicesToSearch) {
						ingredientsToSearch.add(ingredients.get(i));
					}

					// AS: return the ingredients to main activity
					Intent returnIntent = new Intent();
					returnIntent.putStringArrayListExtra("ingredients_list",
							ingredientsToSearch);
					setResult(RESULT_OK, returnIntent);
					finish();
				}
			}
		});

	}

	/**
	 * This method sets up the list view by getting an instance of the user and
	 * extracting the ingredients from the user's pantry.
	 */
	private void setUpListView() {
		user = User.getInstance();
		ingredientsLV = (ListView) findViewById(R.id.lvPantry1);
		registerForContextMenu(ingredientsLV);
		ingredientsLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		ingredients = user.getPantry().getAllIngredients();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.simple_list_item_multiple_choice, ingredients);
		ingredientsLV.setAdapter(adapter);
	}

	/**
	 * This method sets up an on click listener for the list view. The index of
	 * the ingredient is either added or removed from indicesToBeSearched
	 * depending if the check box is already clicked.
	 */
	protected void setListViewOnClickListener() {

		ingredientsLV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// AS: if position is in indexesToSearch already, then we remove
				// it from set;
				// otherwise, we add it to the set.

				if (indicesToSearch.contains(position)) {
					indicesToSearch.remove(position);
				} else {
					indicesToSearch.add(position);
				}

			}
		});
	}

	/**
	 * This method creates a dialog which informs the user that at least one
	 * ingredient must be selected to search by.
	 */
	protected void emptySearchDialog() {
		TextView tv = new TextView(this);
		tv.setText("Please select at least one ingredient to search!");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Nothing Selected");
		alert.setView(tv);
		alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}
}