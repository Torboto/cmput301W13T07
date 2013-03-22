package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
	int sourceCode;
	String creatorEmail;
	KeyListener titleListener;
	KeyListener descriptionListener;
	KeyListener ingredientsListener;
	KeyListener directionsListener;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipe);

		Bundle extras = getIntent().getExtras();
		sourceCode = extras.getInt("code");
		final String recipeString = extras.getString("recipeId");

		// AS: call fillRecipe() to get the information to be displayed about
		// the current recipe
		fillRecipeInfo(recipeString);

		// AS: depending on whether the user came from My Recipes or from a
		// search we set up different buttons
		if (sourceCode == 1) {
			// AS: if came from My Recipes
			hideSave();
			Button deleteButton = (Button) findViewById(R.id.b_recipeDelete);
			Button editButton = (Button) findViewById(R.id.b_recipeEdit);
			
			deleteButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// AS: The delete button calls deleteRecipe and finishes
					// activity
					deleteRecipe(recipeString);
					finish();
				}
			});
			
			editButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// AS: if the recipe is editable to this user then
					// the edit button will change the editTexts and buttons
					if (true){
						editTextMode();
						hideEditDelete();
						showSave();
						
						Button saveButton = (Button) findViewById(R.id.b_recipeSave);
						saveButton.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								// AS: The save button calls editRecipe then finishes
								editRecipe(recipeString);
								finish();
							}

							
						});
										
						
					}
					// AS: if  not editable then nothing happens (inform user here)
					// for testing put finish() here
					else{
						finish();
					}
				}
			});
			
		} else if (sourceCode == 2) {
			// AS: if came from Search
			hideEditDelete();
		} else {
			// AS: the code should not get here
		}
	}

	/**
	 * Takes a recipe ID as a string and fills in the recipe's information
	 * to the appropriate text views by calling parseRecipe. It first extracts the recipe
	 * from either the user's database or the server, then calls parseRecipe.
	 * 
	 * @param recipeString a string representation of a recipe UUID
	 */
	private void fillRecipeInfo(String recipeString) {
		// AS: first get the recipe from the database using a recipeController
		RecipeController rc = new RecipeController();
		UUID recipeID = UUID.fromString(recipeString);
		Recipe recipe = null;

		if (sourceCode == 1) {
			recipe = rc.getRecipeSQL(recipeID, getApplicationContext());
			parseRecipe(recipe);	
		}
		if (sourceCode == 2){
			SearchRecipeTask search = new SearchRecipeTask(recipeID);

			search.setDataDownloadListener(new DataDownloadListener() {
				@SuppressWarnings("unchecked")
				public void dataDownloadedSuccessfully(ArrayList<Recipe> data) {
					parseRecipe(data.get(0));
				}
			});
			search.execute("");
		}
	}
	
	
	/**
	 * This method takes a recipe, puts the title, directions, description,
	 * and ingredients, then calls fillTextViews with this information.
	 * 
	 * @param recipe the recipe with information to gather
	 */
	private void parseRecipe(Recipe recipe){
				String title = recipe.getName();
				String directions = recipe.getDirections();
				String description = recipe.getDescription();
				String ingredients = convertList(recipe.getIngredients());
				
				// AS: get the creator email from the recipe
				creatorEmail = recipe.getCreatorEmail();
				
				fillTextViews(title, description, directions, ingredients);
	}

	/**
	 * This method takes Strings of a recipe's needed information then sets
	 * the corresponding edit texts with this information. It also makes the
	 * edit texts non editable and saves the key listeners of the edit texts.
	 * 
	 * @param title       the title of the recipe
	 * @param description the description of the recipe
	 * @param directions  the directions of the recipe
	 * @param ingredients the ingredients of the recipe
	 */
	private void fillTextViews(String title, String description,
			String directions, String ingredients) {
		// AS: first create the edit text objects
		EditText etTitle = (EditText)findViewById(R.id.etRecipeTitle);
		EditText etDescription = (EditText)findViewById(R.id.etRecipeDescription);
		EditText etDirections = (EditText)findViewById(R.id.etDirectionsList);
		EditText etIngredients = (EditText)findViewById(R.id.etIngredientsList);
		
		// AS: then set the text views
		etTitle.setText(title);
		etDescription.setText(description);
		etDirections.setText(directions);
		etIngredients.setText(ingredients);
		
		// AS: save the original key listeners
		titleListener = etTitle.getKeyListener();
		descriptionListener = etDescription.getKeyListener();
		directionsListener = etDirections.getKeyListener();
		ingredientsListener = etIngredients.getKeyListener();
		
		// AS: then set them to be uneditable
		etTitle.setKeyListener(null);
		etDescription.setKeyListener(null);
		etDirections.setKeyListener(null);
		etIngredients.setKeyListener(null);
		
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
	private void hideSave(){
		Button saveButton = (Button) findViewById(R.id.b_recipeSave);
		saveButton.setVisibility(4);
		return;
	}
	
	private void showSave(){
		Button saveButton = (Button) findViewById(R.id.b_recipeSave);
		saveButton.setVisibility(1);
		return;
	}
	
	/**
	 * This method hides the edit and delete buttons. It is called if the user is
	 * viewing a searched recipe from the internet, so there is no editing or deleting.
	 */
	private void hideEditDelete(){
		Button editButton = (Button) findViewById(R.id.b_recipeEdit);
		Button deleteButton = (Button) findViewById(R.id.b_recipeDelete);
		editButton.setVisibility(4);
		deleteButton.setVisibility(4);
		return;
	}
	
	/**
	 * This method changes the edit texts to now be editable for the user.
	 */
	private void editTextMode(){
		EditText etTitle = (EditText)findViewById(R.id.etRecipeTitle);
		EditText etDescription = (EditText)findViewById(R.id.etRecipeDescription);
		EditText etDirections = (EditText)findViewById(R.id.etDirectionsList);
		EditText etIngredients = (EditText)findViewById(R.id.etIngredientsList);
		
		etTitle.setKeyListener(titleListener);
		etDescription.setKeyListener(descriptionListener);
		etDirections.setKeyListener(directionsListener);
		etIngredients.setKeyListener(ingredientsListener);
		
		return;
	}
	
	
	
	/**
	 * This method takes a string representation of a recipe UUID and deletes the
	 * corresponding recipe from the database. The string is first converted to a
	 * UUID object, and is used by a RecipeController object to delete the recipe.
	 * 
	 * @param recipeString the string representation of a recipe UUID
	 */
	private void deleteRecipe(String recipeString){
		UUID recipeID = UUID.fromString(recipeString);
		RecipeController rc = new RecipeController();
		rc.deleteRecipe(recipeID, getApplicationContext());
		return;
	}
	
	private void editRecipe(String recipeString) {
		// TODO Auto-generated method stub
		
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
	 * This method determines if the current recipe is editable. If the user's email matches
	 * the creator of the recipe's email, then it is.
	 * 
	 * @return true if editable or false otherwise
	 */
	private boolean isEditableRecipe(){
		String userEmail = grabEmail();
		if (userEmail == creatorEmail)
			return true;
		else 
			return false;
		
	}
	
}
