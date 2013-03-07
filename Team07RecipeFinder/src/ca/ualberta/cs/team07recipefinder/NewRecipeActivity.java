package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewRecipeActivity extends Activity {

	EditText titleEditText = (EditText) findViewById(R.id.etRecipeTitle);
	EditText descriptionEditText = (EditText) findViewById(R.id.etRecipeDescription);
	EditText ingredientsEditText = (EditText) findViewById(R.id.etIngredientsList);
	EditText directionsEditText = (EditText) findViewById(R.id.etDirectionsList);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_recipe);
		
		Button doneButton = (Button) findViewById(R.id.bDone);
		doneButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// AS: The done button calls createRecipe
				createRecipe();
			}
		});
	}
	// AS: Stuff below here will be factored out into View

	private void createRecipe() {

		if ((!isEmpty(titleEditText)) && (!isEmpty(descriptionEditText))
				&& (!isEmpty(ingredientsEditText))
				&& (!isEmpty(directionsEditText))) {
			/*
			 * AS: Now we know the required fields are filled in before we
			 * proceed to create a new Recipe
			 */
			String title = titleEditText.toString();
			String description = descriptionEditText.toString();
			ArrayList<String> ingredients = parseIngredients(ingredientsEditText);
			String directions = directionsEditText.toString();
			String email = grabEmail();

			Recipe newRecipe = new Recipe(title, description, ingredients,
					directions, email);

			// Finally, we can use RecipeController to write this new Recipe
			RecipeController rc = new RecipeController();
			rc.writeRecipe(newRecipe);
		}

		// AS: If one or more fields empty could potentially have a
		// dialog saying so? Next Iteration of project
		finish();
	}

	private boolean isEmpty(EditText etText) {
		// AS: returns if an Edit Text is empty or not
		if (etText.getText().toString().trim().length() > 0)
			return false;
		else
			return true;
	}

	private String grabEmail() {
		// AS: gets the email address of the user
		User theUser = User.getInstance();
		String email = theUser.getEmail();
		return email;
	}

	private ArrayList<String> parseIngredients(EditText ingredientsEditText) {
		/*
		 * AS: takes the ingredients as an Edit Text and returns them as and
		 * ArrayList of Strings. This assumes that they are separated by new
		 * lines.
		 */
		String ingredientsString = ingredientsEditText.toString();
		ArrayList<String> ingredients = (ArrayList<String>) Arrays
				.asList(ingredientsString.split("\n"));
		return ingredients;
	}

}
