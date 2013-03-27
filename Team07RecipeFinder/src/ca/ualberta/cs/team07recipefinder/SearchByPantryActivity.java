package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchByPantryActivity extends Activity {
	ListView ingredientsLV;
	User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_by_pantry);
		
		user = User.getInstance();
		ingredientsLV = (ListView) findViewById(R.id.lvPantry);
		registerForContextMenu(ingredientsLV);
	
	}

		
	/**
	 * @author xiaohuim
	 * 
	 * This will load and show all ingredients in the ListView under Pantry tab.
	 */
	protected void onStart() {
		super.onStart();
		ingredientsLV = (ListView) findViewById(R.id.lvPantry);
		ArrayList<String> ingredients = user.getPantry().getAllIngredients();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, ingredients);
		ingredientsLV.setAdapter(adapter);
	}

	
}