package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchByPantryActivity extends Activity {
	ListView ingredientsLV;
	User user;
	Set <Integer> indicesToSearch = new HashSet<Integer>();
	ArrayList <String> ingredientsToSearch = new ArrayList<String>();
	ArrayList <String> ingredients = new ArrayList<String>();
	
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
				finish();
			}
		});
		
		Button searchButton = (Button) findViewById(R.id.button_search1);
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// AS: The search button grabs the needed ingredients using indicesToSearch
				// then calls searchIngredients
				
				// clear the ingredients list every time so we don't "double count" certain indices
				ingredientsToSearch.clear();
				for(Integer i : indicesToSearch){
					ingredientsToSearch.add(ingredients.get(i));
				}
				searchIngredients();
				testDialog();
			}
		});
	
	}
	
	/**
	 * This method sets up the list view by getting an instance of the user and extracting
	 * the ingredients from the user's pantry.
	 */
	private void setUpListView(){
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
	 * This method sets up an on click listener for the list view. The index of the ingredient
	 * is either added or removed from indicesToBeSearched depending if the check box is
	 * already clicked.
	 */
	protected void setListViewOnClickListener() {

		ingredientsLV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// AS: if position is in indexesToSearch already, then we remove it from set;
				// otherwise, we add it to the set.
				
				if (indicesToSearch.contains(position))
					indicesToSearch.remove(position);
				else
					indicesToSearch.add(position);
				
			}
		});
	}
	
	
	private void searchIngredients() {
		
	}

	protected void testDialog() {
		TextView tv = new TextView(this);
		String thingy = new String();
		thingy = "";
		for (String ingredient : ingredientsToSearch)
			thingy += (ingredient + " ,");
			
		tv.setText(thingy);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Sorry!");
		alert.setView(tv);
		alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}
}