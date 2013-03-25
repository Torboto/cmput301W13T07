package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * The NewRecipeActvity displays EditTexts for the user to enter a recipe title,
 * description, ingredients list, and directions. If the user fills in every EditText
 * then hits the 'done' button, a new Recipe is written to the user's database with the
 * entered information.
 * 
 * @author Adam St. Arnaud
 *
 */
public class NewRecipeActivity extends Activity {

	EditText titleEditText;
	EditText descriptionEditText;
	EditText ingredientsEditText;
	EditText directionsEditText;
	
	// New recipe that will be populated with the info entered by the user
	Recipe newRecipe = new Recipe();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_recipe);

		titleEditText = (EditText) findViewById(R.id.etRecipeTitle);
		descriptionEditText = (EditText) findViewById(R.id.etRecipeDescription);
		ingredientsEditText = (EditText) findViewById(R.id.etIngredientsList);
		directionsEditText = (EditText) findViewById(R.id.etDirectionsList);

		Button doneButton = (Button) findViewById(R.id.bDone);
		doneButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// AS: The done button calls createRecipe
				createRecipe();
			}
		});
		
		ImageButton pictureButton = (ImageButton) findViewById(R.id.ibRecipe);
		pictureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(
						getApplicationContext(),
						CameraActivity.class);
				cameraIntent.putExtra("recipeId",
						String.valueOf(newRecipe.getRecipeId()));
				startActivity(cameraIntent);
			}
		});
	}


	/**
	 * This method checks that the Edit Texts are all non empty, and if so,
	 * calls grabRecipeInfo and writeRecipe to form and write a new recipe
	 * to the user's database.
	 */
	private void createRecipe() {

		if ((!isEmpty(titleEditText)) && (!isEmpty(descriptionEditText))
				&& (!isEmpty(ingredientsEditText))
				&& (!isEmpty(directionsEditText))) {
			/*
			 * AS: Now we know the required fields are filled in before we
			 * proceed to create a new Recipe
			 */
			Recipe newRecipe = grabRecipeInfo();
			writeRecipe(newRecipe);
			finish();
		}
		else
			missingFields();
	}

	/**
	 * This method forms and returns a new Recipe object by getting the
	 * necessary information from the Activity's EditTexts.
	 * 
	 * @return the newly created Recipe.
	 */
	private Recipe grabRecipeInfo() {
		String title = titleEditText.getText().toString();
		String description = descriptionEditText.getText().toString();
		ArrayList<String> ingredients = 
				parseIngredients(ingredientsEditText);
		String directions = directionsEditText.getText().toString();
		String email = grabEmail();
		newRecipe.setDescription(description);
		newRecipe.setIngredients(ingredients);
		newRecipe.setName(title);
		newRecipe.setDirections(directions);
		newRecipe.setCreatorEmail(email);
		return newRecipe;
		
	}


	/**
	 * This method takes a recipe and writes it to the user's database.
	 * 
	 * @param newRecipe the recipe to be written
	 */
	private void writeRecipe(Recipe newRecipe) {
		RecipeController rc = new RecipeController();
		rc.writeRecipe(newRecipe, getApplicationContext());
		return;
	}


	/**
	 * This method takes an EditText and returns true if it is empty and false otherwise.
	 * 
	 * @param etText the EditText to be tested
	 * @return       True: if empty, false: otherwise.
	 */
	private boolean isEmpty(EditText etText) {
		// AS: returns if an Edit Text is empty or not
		if (etText.getText().toString().trim().length() > 0)
			return false;
		else
			return true;
	}

	/**
	 * This method gets and instance of the User singleton and then extracts
	 * the user's email.
	 * 
	 * @return the user's email as a string
	 */
	private String grabEmail() {
		User theUser = User.getInstance();
		String email = theUser.getEmail();
		return email;
	}

	/**
	 * This method takes the ingredients as an EditText and returns them as an
	 * ArrayList of strings. It assumes that they are separated by newline characters.
	 * 
	 * @param ingredientsEditText the ingredients as an EditText
	 * @return                    the ingredients as an ArrayList of strings
	 */
	private ArrayList<String> parseIngredients(EditText ingredientsEditText) {
		String ingredientsString = ingredientsEditText.getText().toString();
		ArrayList<String> ingredients = new ArrayList<String>(
				Arrays.asList(ingredientsString.split("\n")));
		return ingredients;
	}
	
	
	/**
	 * This method creates a dialog which informs the user that they are missing one
	 * or more fields in the recipe they tried to create.
	 */
	private void missingFields() {
		TextView tv = new TextView(this);
		tv.setText("You must fill in all fields to create a recipe!");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Sorry");
		alert.setView(tv);
		alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}

}
