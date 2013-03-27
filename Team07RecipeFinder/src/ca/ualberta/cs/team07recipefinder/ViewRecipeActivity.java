package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The ViewRecipeActivity displays the information about a particular Recipe.
 * The title, description, ingredients, and directions are shown to the user.
 * Different buttons are visible depending on whether the activity was launched
 * from the user's recipes or from a search of the online database. An integer
 * named "code" is passed through the intent which signifies which buttons are
 * seen. If the user is looking at their own recipe then they have the options
 * to delete or edit. If the user is viewing a searched recipe, they can save it
 * to their own database.
 * 
 * @author Adam St. Arnaud
 * 
 */
public class ViewRecipeActivity extends Activity {
	int sourceCode;
	KeyListener titleListener;
	KeyListener descriptionListener;
	KeyListener ingredientsListener;
	KeyListener directionsListener;
	// TODO: ET- if currentRecipe is a member variable, it should need to be
	// passed into any functions like ParseRecipe
	Recipe currentRecipe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipe);

		Bundle extras = getIntent().getExtras();
		sourceCode = extras.getInt("code");
		final String recipeString = extras.getString("recipeId");

		// AS: call fillCurrentRecipe() to get the recipe from either the local
		// databse
		// or server.
		fillCurrentRecipe(recipeString);

		ImageButton pictureButton = (ImageButton) findViewById(R.id.ibRecipe);
		pictureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent galleryIntent = new Intent(getApplicationContext(),
						GalleryViewActivity.class);
				galleryIntent.putExtra("code", sourceCode);
				galleryIntent.putExtra("recipeId",
						String.valueOf(currentRecipe.getRecipeId()));
				startActivity(galleryIntent);
			}
		});
		
		Button emailButton = (Button) findViewById(R.id.b_recipeEmail);
		emailButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// AS: The email button calls emailToSelf() and emailDialog()
				emailToSelf();
				emailDialog();
			}

		});
		// AS: depending on whether the user came from My Recipes or from a
		// search we set up different buttons
		if (sourceCode == 1) {
			// AS: if came from My Recipes
			hideSave();
			// hideEmail(); commented out for testing
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
					if (isEditableRecipe()) {
						editTextMode();
						hideEditDelete();
						showSave();
						showThatEditable();
						Button saveButton = (Button) findViewById(R.id.b_recipeSave);
						saveButton
								.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										// AS: The save button calls editRecipe
										// then finishes
										editRecipe(recipeString);
										finish();
									}
								});
					}

					// AS: if not editable then nothing happens (inform user
					// here)
					else {
						showThatNotEditable();
					}
				}
			});

		} else if (sourceCode == 2) {
			// AS: if came from Search
			hideEditDelete();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void populateImages() {
		// Check if the recipe has any images saved on the sd card and get
		// the bitmap for the imagebutton
		ArrayList<String> imagePaths = ImageController.getAllRecipeImages(
				currentRecipe.getRecipeId(), currentRecipe.location);

		ImageButton pictureButton = (ImageButton) findViewById(R.id.ibRecipe);

		// Set the image of the imagebutton to the first image in the folder
		if (imagePaths.size() > 0) {
			pictureButton.setImageBitmap(ImageController.getThumbnailImage(
					imagePaths.get(0), currentRecipe.location));
		}

	}

	/**
	 * Takes a recipe ID as a string and extracts the recipe from either the
	 * user's database or the server to set currentRecipe.
	 * 
	 * @param recipeString
	 *            a string representation of a recipe UUID
	 */
	private void fillCurrentRecipe(String recipeString) {
		// AS: first get the recipe from the database using a recipeController
		UUID recipeID = UUID.fromString(recipeString);

		if (sourceCode == 1) {
			currentRecipe = RecipeController.getLocalRecipe(recipeID,
					getApplicationContext());
			parseRecipe(currentRecipe);
			populateImages();
		}
		if (sourceCode == 2) {
			SearchRecipeTask search = new SearchRecipeTask(recipeID);

			search.setDataDownloadListener(new DataDownloadListener() {
				public void dataDownloadedSuccessfully(ArrayList<Recipe> data) {
					currentRecipe = data.get(0);
					parseRecipe(currentRecipe);
					populateImages();
				}
			});
			search.execute("");
		}
	}

	/**
	 * This method takes a recipe, puts the title, directions, description, and
	 * ingredients, then calls fillTextViews with this information.
	 * 
	 * @param recipe
	 *            the recipe with information to gather
	 */
	private void parseRecipe(Recipe recipe) {
		String title = recipe.getName();
		String directions = recipe.getDirections();
		String description = recipe.getDescription();
		String ingredients = convertList(recipe.getIngredients());

		fillTextViews(title, description, directions, ingredients);
	}

	/**
	 * This method takes Strings of a recipe's needed information then sets the
	 * corresponding edit texts with this information. It also makes the edit
	 * texts non editable and saves the key listeners of the edit texts.
	 * 
	 * @param title
	 *            the title of the recipe
	 * @param description
	 *            the description of the recipe
	 * @param directions
	 *            the directions of the recipe
	 * @param ingredients
	 *            the ingredients of the recipe
	 */
	private void fillTextViews(String title, String description,
			String directions, String ingredients) {
		// AS: first create the edit text objects
		EditText etTitle = (EditText) findViewById(R.id.etRecipeTitle);
		EditText etDescription = (EditText) findViewById(R.id.etRecipeDescription);
		EditText etDirections = (EditText) findViewById(R.id.etDirectionsList);
		EditText etIngredients = (EditText) findViewById(R.id.etIngredientsList);

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
	 * @param ingredientsList
	 *            an array list containing the ingredients for a recipe
	 * @return the string containing all ingredients
	 */
	private String convertList(ArrayList<String> ingredientsList) {
		String ingredients = "";
		for (String s : ingredientsList) {
			ingredients += (s + "\n");
		}
		return ingredients;
	}

	/**
	 * This method hides the save button. It is called the user is viewing one
	 * of their own recipes, so the save button is not needed.
	 */
	private void hideSave() {
		Button saveButton = (Button) findViewById(R.id.b_recipeSave);
		saveButton.setVisibility(4);
		return;
	}

	/**
	 * This method hides the email button. It is called the user is viewing one
	 * of their own recipes, so the email button is not needed.
	 */
	private void hideEmail() {
		Button emailButton = (Button) findViewById(R.id.b_recipeEmail);
		emailButton.setVisibility(4);
		return;
	}

	/**
	 * This method makes the save button visible again, for when the user enters
	 * edit mode.
	 */
	private void showSave() {
		Button saveButton = (Button) findViewById(R.id.b_recipeSave);
		saveButton.setVisibility(1);
		return;
	}

	/**
	 * This method hides the edit and delete buttons. It is called if the user
	 * is viewing a searched recipe from the internet, so there is no editing or
	 * deleting.
	 */
	private void hideEditDelete() {
		Button editButton = (Button) findViewById(R.id.b_recipeEdit);
		Button deleteButton = (Button) findViewById(R.id.b_recipeDelete);
		editButton.setVisibility(4);
		deleteButton.setVisibility(4);
		return;
	}

	/**
	 * This method changes the edit texts to now be editable for the user.
	 */
	private void editTextMode() {
		EditText etTitle = (EditText) findViewById(R.id.etRecipeTitle);
		EditText etDescription = (EditText) findViewById(R.id.etRecipeDescription);
		EditText etDirections = (EditText) findViewById(R.id.etDirectionsList);
		EditText etIngredients = (EditText) findViewById(R.id.etIngredientsList);

		etTitle.setKeyListener(titleListener);
		etDescription.setKeyListener(descriptionListener);
		etDirections.setKeyListener(directionsListener);
		etIngredients.setKeyListener(ingredientsListener);

		return;
	}

	/**
	 * This method takes a string representation of a recipe UUID and deletes
	 * the corresponding recipe from the database. The string is first converted
	 * to a UUID object, and is used by a RecipeController object to delete the
	 * recipe.
	 * 
	 * @param recipeString
	 *            the string representation of a recipe UUID
	 */
	private void deleteRecipe(String recipeString) {
		UUID recipeID = UUID.fromString(recipeString);
		RecipeController.deleteRecipe(recipeID, getApplicationContext());
		return;
	}

	private void editRecipe(String recipeString) {
		UUID recipeID = UUID.fromString(recipeString);
		Recipe newRecipe = grabRecipeInfo();
		RecipeController.updateRecipe(recipeID, newRecipe,
				getApplicationContext());
		return;
	}

	/**
	 * This method forms and returns a new Recipe object by getting the
	 * necessary information from the Activity's EditTexts.
	 * 
	 * @return the newly created Recipe.
	 */
	private Recipe grabRecipeInfo() {
		EditText etTitle = (EditText) findViewById(R.id.etRecipeTitle);
		EditText etDescription = (EditText) findViewById(R.id.etRecipeDescription);
		EditText etDirections = (EditText) findViewById(R.id.etDirectionsList);
		EditText etIngredients = (EditText) findViewById(R.id.etIngredientsList);

		String title = etTitle.getText().toString();
		String description = etDescription.getText().toString();
		ArrayList<String> ingredients = parseIngredients(etIngredients);
		String directions = etDirections.getText().toString();
		String email = grabEmail();
		Recipe newRecipe = new Recipe();
		newRecipe.setName(title);
		newRecipe.setDescription(description);
		newRecipe.setDirections(directions);
		newRecipe.setIngredients(ingredients);
		newRecipe.setCreatorEmail(email);
		return newRecipe;

	}

	/**
	 * This method gets and instance of the User singleton and then extracts the
	 * user's email.
	 * 
	 * @return the user's email as a string
	 */
	private String grabEmail() {
		User theUser = User.getInstance();
		String email = theUser.getEmail();
		return email;
	}

	/**
	 * This method determines if the current recipe is editable. If the user's
	 * email matches the creator of the recipe's email, then it is.
	 * 
	 * @return true if editable or false otherwise
	 */
	private boolean isEditableRecipe() {
		String userEmail = grabEmail();
		String creatorEmail = currentRecipe.getCreatorEmail();
		if (userEmail.equalsIgnoreCase(creatorEmail))
			return true;
		else
			return false;
	}

	/**
	 * This method takes the ingredients as an EditText and returns them as an
	 * ArrayList of strings. It assumes that they are separated by newline
	 * characters.
	 * 
	 * @param ingredientsEditText
	 *            the ingredients as an EditText
	 * @return the ingredients as an ArrayList of strings
	 */
	private ArrayList<String> parseIngredients(EditText ingredientsEditText) {
		String ingredientsString = ingredientsEditText.getText().toString();
		ArrayList<String> ingredients = new ArrayList<String>(
				Arrays.asList(ingredientsString.split("\n")));
		return ingredients;
	}

	/**
	 * This method creates a dialog which informs that user that they can now
	 * edit the current recipe.
	 */
	private void showThatEditable() {
		TextView tv = new TextView(this);
		tv.setText("You may now edit this recipe!");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Edit Mode");
		alert.setView(tv);
		alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}

	/**
	 * This method creates a dialog which informs the user that only the
	 * original creator can edit their recipe.
	 */
	private void showThatNotEditable() {
		TextView tv = new TextView(this);
		tv.setText("Only the original creator may edit their recipe!");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Sorry");
		alert.setView(tv);
		alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}

	private void emailDialog() {
		TextView tv = new TextView(this);
		tv.setText("Email sent to your account!");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Email");
		alert.setView(tv);
		alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}

	private void emailToSelf() {
		String userEmail = grabEmail();
		String emailBody = convertToEmail();

		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL, userEmail);
		i.putExtra(Intent.EXTRA_SUBJECT, "Recipe");
		i.putExtra(Intent.EXTRA_TEXT, emailBody);
		startActivity(Intent.createChooser(i, "Send mail..."));
	}

	// TODO: ET - factor this out into another class
	private String convertToEmail() {
		String title = currentRecipe.getName();
		String directions = currentRecipe.getDirections();
		String description = currentRecipe.getDescription();
		String ingredients = convertList(currentRecipe.getIngredients());
		return "Recipe Title:\n" + title + "\n\nRecipe Description:\n"
				+ description + "\n\nIngredients:\n" + ingredients
				+ "\n\nDirections:\n" + directions;
	}

}
