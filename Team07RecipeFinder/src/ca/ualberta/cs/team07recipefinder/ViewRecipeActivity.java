package ca.ualberta.cs.team07recipefinder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewRecipeActivity extends Activity {
	/* */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipe);
		
		//Bundle extras = getIntent().getExtras();
		//int code = extras.getInt("code");
		int code = 0;
		if (code == 0){
			// AS: if came from My Recipes
			fromMyRecipes();
			Button deleteButton = (Button) findViewById(R.id.bDone);
			deleteButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// AS: The delete button calls deleteRecipe and finishes activity
					deleteRecipe();
					finish();
				}
			});
		} else if (code == 1) {
			// AS: if came from Search
			fromSearch();
		} else{
			// AS: the code should not get here
		}
	}

	private void fromMyRecipes(){
		Button saveButton = (Button) findViewById(R.id.b_recipeSave);
		saveButton.setVisibility(4);
		return;
	}
	
	private void fromSearch(){
		Button editButton = (Button) findViewById(R.id.b_recipeEdit);
		Button deleteButton = (Button) findViewById(R.id.b_recipeDelete);
		editButton.setVisibility(4);
		deleteButton.setVisibility(4);
		return;
	}
	
	private void deleteRecipe(){
		//AS: will need to delete the current recipe
		return;
	}
	
}
