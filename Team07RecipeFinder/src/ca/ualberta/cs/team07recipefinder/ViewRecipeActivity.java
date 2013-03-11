package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * The ViewRecipeActivity displays the information about a particular Recipe.
 * The title, description, ingredients, and directions are shown to the user.
 * Different buttons are visible depending on whether the activity was launched
 * from the user's recipes or from a search of the online database. An integer named
 * "code" is passed through the intent which signifies which buttons are seen.
 * If the user is looking at their own recipe then they have the options to delete
 * or edit. If the user is viewing a searched recipe, they can save it to their own
 * database.
 * 
 * @author Adam St. Arnaud
 *
 */
public class ViewRecipeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipe);
		
		Bundle extras = getIntent().getExtras();
		int code = extras.getInt("code");
		final String recipeString = extras.getString("recipeId");
		
		// AS: call fillRecipe() to get the information to be displayed about
		// the current recipe
		fillRecipeInfo(recipeString);
		
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

	/**
	 * Takes a recipe ID as a string and fills in the recipe's information
	 * to the appropriate text views by calling fillTextViews. It uses a
	 * RecipeController object to extract the recipe from the database,
	 * then extracts the needed information into Strings. fillTextViews is then called.
	 * 
	 * @param recipeString a string representation of a recipe UUID
	 */
	private void fillRecipeInfo(String recipeString) {
		// AS: first get the recipe from the database using a recipeController
		RecipeController rc = new RecipeController();
		UUID recipeID = UUID.fromString(recipeString);
		Recipe recipe = rc.getRecipe(recipeID, getApplicationContext());
		
		// AS: next, grab the information from the recipe
		String title = recipe.getName();
		String directions = recipe.getDirections();
		String description = recipe.getDescription();
		String ingredients = convertList(recipe.getIngredients());
		
		fillTextViews(title, description, directions, ingredients);
	}

	/**
	 * This method takes Strings of a recipe's needed information then sets
	 * the corresponding text views with this information.
	 * 
	 * @param title the title of the recipe
	 * @param description the description of the recipe
	 * @param directions the directions of the recipe
	 * @param ingredients the ingredients of the recipe
	 */
	private void fillTextViews(String title, String description,
			String directions, String ingredients) {
		// AS: first create the text view objects
		TextView tvTitle = (TextView)findViewById(R.id.tvRecipeTitle);
		TextView tvDescription = (TextView)findViewById(R.id.tvRecipeDescription);
		TextView tvDirections = (TextView)findViewById(R.id.tvDirectionsList);
		TextView tvIngredients = (TextView)findViewById(R.id.tvIngredientsList);
		
		// AS: then set the text views
		tvTitle.setText(title);
		tvDescription.setText(description);
		tvDirections.setText(directions);
		tvIngredients.setText(ingredients);
		return;	
	}

	/**
	 * This method takes and ArrayList of ingredients and returns them as a
	 * single string with newline characters between each.
	 * 
	 * @param ingredientsList an array list containing the ingredients for a recipe
	 * @return			      the string containing all ingredients
	 */
	private String convertList(ArrayList<String> ingredientsList){
		String ingredients = "";
		for (String s : ingredientsList) {
			ingredients += (s + "\n");
		}
		return ingredients;
	}
	
	/**
	 * This method hides the save button. It is called the user is viewing one of
	 * their own recipes, so the save button is not needed.
	 */
	private void fromMyRecipes(){
		Button saveButton = (Button) findViewById(R.id.b_recipeSave);
		saveButton.setVisibility(4);
		return;
	}
	
	/**
	 * This method hides the edit and delete buttons. It is called if the user is
	 * viewing a searched recipe from the internet, so there is no editing or deleting.
	 */
	private void fromSearch(){
		Button editButton = (Button) findViewById(R.id.b_recipeEdit);
		Button deleteButton = (Button) findViewById(R.id.b_recipeDelete);
		editButton.setVisibility(4);
		deleteButton.setVisibility(4);
		return;
	}
	
	/**
	 * This method takes a string representation of a recipe UUID and deletes the
	 * corresponding recipe from the database. The string is first converted to a
	 * UUID object, and is used by a RecipeController object to delete the recipe.
	 * @param recipeString
	 */
	private void deleteRecipe(String recipeString){
		UUID recipeID = UUID.fromString(recipeString);
		RecipeController rc = new RecipeController();
		rc.deleteRecipe(recipeID, getApplicationContext());
		return;
	}
	
}
