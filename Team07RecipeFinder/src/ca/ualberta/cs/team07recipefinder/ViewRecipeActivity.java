package ca.ualberta.cs.team07recipefinder;

import java.util.UUID;

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
		
		Bundle extras = getIntent().getExtras();
		int code = extras.getInt("code");
		String recipeString = extras.getString("recipeId");
		
		// AS: call fillRecipe() to get the information to be displayed about
		// the current recipe
		fillRecipe(recipeString);
		
		// AS: depending on whether the user came from My Recipes or from a search
		// we set up different buttons 
		if (code == 1){
			// AS: if came from My Recipes
			fromMyRecipes();
			Button deleteButton = (Button) findViewById(R.id.b_recipeDelete);
			deleteButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// AS: The delete button calls deleteRecipe and finishes activity
					Bundle extras = getIntent().getExtras();
					String recipeString = extras.getString("recipeID");
					deleteRecipe(recipeString);
					finish();
				}
			});
		} else if (code == 2) {
			// AS: if came from Search
			fromSearch();
		} else{
			// AS: the code should not get here
		}
	}

	private void fillRecipe(String recipeString) {
		// TOD
		
	}

	private void fromMyRecipes(){
		// AS: hides the save button since it is not available when we are viewing
		// one of our own recipes
		Button saveButton = (Button) findViewById(R.id.b_recipeSave);
		saveButton.setVisibility(4);
		return;
	}
	
	private void fromSearch(){
		// AS: hides the edit and delete buttons since they are not available
		// when we are viewing a recipe from search
		Button editButton = (Button) findViewById(R.id.b_recipeEdit);
		Button deleteButton = (Button) findViewById(R.id.b_recipeDelete);
		editButton.setVisibility(4);
		deleteButton.setVisibility(4);
		return;
	}
	
	private void deleteRecipe(String recipeString){
		//AS: delete the current recipe specified by a string representation of a UUID
		UUID recipeID = UUID.fromString(recipeString);
		RecipeController rc = new RecipeController();
		rc.deleteRecipe(recipeID);
		return;
	}
	
}
