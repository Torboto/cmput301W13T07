package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;

public class NewRecipeActivity extends Activity {

	EditText titleEditText;
	EditText descriptionEditText;
	EditText ingredientsEditText;
	EditText directionsEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_recipe);
	}

	public void back(View v) {
		// AS: Go back to my recipes activity
		finish();
	}

	// AS: Stuff below here will be factored out into View

	public void createRecipe(View v) {

		descriptionEditText = grabDescription();
		ingredientsEditText = grabIngredients();
		directionsEditText = grabDirections();

		if ((!isEmpty(descriptionEditText)) && (!isEmpty(ingredientsEditText))
				&& (!isEmpty(directionsEditText))) {
			/*
			 * AS: Now we know the required fields are filled in before we
			 * proceed to create a new Recipe
			 */
			String title = "Need to Add This";
			String description = descriptionEditText.toString();
			ArrayList<String> ingredients = parseIngredients(ingredientsEditText);
			String directions = directionsEditText.toString();
			String email = grabEmail();

			Recipe newRecipe = new Recipe(title, description, ingredients,
			 directions, email );
			
			// Finally, we can use RecipeController to write this new Recipe
			RecipeController.writeRecipe(newRecipe);
		}

		// AS: If one or more fields empty could potentially have a
		// dialog saying so?
	}

	private boolean isEmpty(EditText etText) {
		// AS: returns if an Edit Text is empty or not
		if (etText.getText().toString().trim().length() > 0)
			return false;
		else
			return true;
	}

	// TODO: figure out title
	// private EditText grabTitle(){
	// AS: returns the title as an Edit Text
	// EditText titleEditText = (EditText) findViewById(R.id.etTitle);
	// return titleEditText;
	// }

	private EditText grabDescription() {
		// AS: returns the description as an Edit Text
		EditText descriptionEditText = (EditText) findViewById(R.id.etDescription);
		return descriptionEditText;
	}

	private EditText grabIngredients() {
		// AS: returns the ingredients as an Edit Text
		EditText ingredientsEditText = (EditText) findViewById(R.id.etNewIngredients);
		return ingredientsEditText;
	}

	private ArrayList<String> parseIngredients(EditText ingredientsEditText) {
		/*
		 *  AS: takes the ingredients as an Edit Text and returns them as
		 * and ArrayList of Strings. This assumes that they are separated by
		 * new lines.
		 */
		String ingredientsString = ingredientsEditText.toString();
		ArrayList<String> ingredients = (ArrayList<String>) Arrays
				.asList(ingredientsString.split("\n"));
		return ingredients;
	}

	private EditText grabDirections() {
		// AS: returns the new directions as an Edit Text
		EditText IngredientsEditText = (EditText) findViewById(R.id.etNewIngredients);
		return IngredientsEditText;
	}

	private String grabEmail() {
		// AS: gets the email address of the user
		User theUser = User.getInstance();
		String email = theUser.getEmail();
		return email;
	}

}
