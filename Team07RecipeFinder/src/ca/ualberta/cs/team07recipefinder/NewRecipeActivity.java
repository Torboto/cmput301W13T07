package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author ajstarna
 * 
 *         The NewRecipeActvity displays EditTexts for the user to enter a
 *         recipe title, description, and directions. There is also a list view
 *         for ingredients. If the user fills in every EditText and at least one
 *         ingredient then hits the 'done' button, a new Recipe is written to
 *         the user's database with the entered information.
 */
public class NewRecipeActivity extends Activity {

	EditText titleEditText;
	EditText descriptionEditText;
	EditText directionsEditText;

	// New recipe that will be populated with the info entered by the user
	Recipe newRecipe = new Recipe();

	ArrayList<String> ingredients = new ArrayList<String>();
	ArrayList<String> units = new ArrayList<String>();
	ArrayList<String> quantities = new ArrayList<String>();
	ArrayList<String> combined = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		newRecipe.createUUID();
		setContentView(R.layout.activity_new_recipe);

		titleEditText = (EditText) findViewById(R.id.etRecipeTitle);
		descriptionEditText = (EditText) findViewById(R.id.etRecipeDescription);
		directionsEditText = (EditText) findViewById(R.id.etDirectionsList);

		populateIngredientView();

		Button doneButton = (Button) findViewById(R.id.bDone);
		doneButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// AS: The done button calls createRecipe
				createRecipe();
			}
		});

		Button newIngredientButton = (Button) findViewById(R.id.bNewIngredient);
		newIngredientButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ingredientDialog();
			}
		});

		ImageButton pictureButton = (ImageButton) findViewById(R.id.ibRecipe);
		pictureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get the number of images the recipe has
				ImageController.updateImageNumber(newRecipe);

				Intent cameraIntent = new Intent(getApplicationContext(),
						CameraActivity.class);
				cameraIntent.putExtra("recipeId",
						String.valueOf(newRecipe.getRecipeId()));
				cameraIntent.putExtra("imageNumber", newRecipe.getImageNumber());
				startActivity(cameraIntent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

		// Check if the recipe has any images saved on the sd card and get
		// the bitmap for the imagebutton

		ArrayList<Image> images = ImageController.getAllRecipeImages(
				newRecipe.getRecipeId(), Recipe.Location.LOCAL);

		ImageButton pictureButton = (ImageButton) findViewById(R.id.ibRecipe);
		Log.w("1****", "outside");
		Log.w("2****", String.valueOf(images.size()));
		// Set the image of the imagebutton to the first image in the folder
		if (images.size() > 0) {
			pictureButton.setImageBitmap(images.get(0).getBitmap());
			Log.w("3****", "GAHHH");
		}
	}

	/**
	 * This method checks that the Edit Texts are all non empty, and if so,
	 * calls grabRecipeInfo and writeRecipe to form and write a new recipe to
	 * the user's database.
	 */
	private void createRecipe() {
		// Get the number of images the recipe has
		ImageController.updateImageNumber(newRecipe);

		if ((!isEmpty(titleEditText)) && (!isEmpty(descriptionEditText))
				&& (!isEmpty(directionsEditText)) && (!ingredients.isEmpty())) {
			/*
			 * AS: Now we know the required fields are filled in before we
			 * proceed to create a new Recipe
			 */
			Recipe newRecipe = grabRecipeInfo();
			writeRecipe(newRecipe);

			finish();
		} else
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
		String directions = directionsEditText.getText().toString();
		String email = grabEmail();
		newRecipe.setDescription(description);
		newRecipe.setIngredients(ingredients);
		newRecipe.setQuantities(quantities);
		newRecipe.setUnits(units);
		newRecipe.setName(title);
		newRecipe.setDirections(directions);
		newRecipe.setCreatorEmail(email);
		return newRecipe;

	}

	/**
	 * This method takes a recipe and writes it to the user's database.
	 * 
	 * @param newRecipe
	 *            the recipe to be written
	 */
	private void writeRecipe(Recipe newRecipe) {
		RecipeController.writeRecipe(newRecipe, getApplicationContext());
		return;
	}

	/**
	 * This method takes an EditText and returns true if it is empty and false
	 * otherwise.
	 * 
	 * @param etText
	 *            the EditText to be tested
	 * @return True: if empty, false: otherwise.
	 */
	private boolean isEmpty(EditText etText) {
		// AS: returns if an Edit Text is empty or not
		if (etText.getText().toString().trim().length() > 0)
			return false;
		else
			return true;
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
	 * This method creates a dialog which informs the user that they are missing
	 * one or more fields in the recipe they tried to create.
	 */
	private void missingFields() {
		TextView tv = new TextView(this);
		tv.setText("You must fill in all text fields!");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Sorry");
		alert.setView(tv);
		alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}

	/**
	 * This method creates a dialog with three edit texts, for ingredient,
	 * quantity, and unit of measurement. There is a 'Cancel' and 'Ok' button.
	 * 
	 */
	protected void ingredientDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Add New Ingredient");

		final EditText ingredientET = new EditText(this);
		ingredientET.setHint("Ingredient");

		final EditText unitET = new EditText(this);
		unitET.setHint("Unit of measurement");

		final EditText quantityET = new EditText(this);
		quantityET.setHint("Quantity");

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(1); // 1 is for vertical orientation
		layout.addView(ingredientET);
		layout.addView(unitET);
		layout.addView(quantityET);

		alert.setView(layout);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if ((!isEmpty(ingredientET)) && (!isEmpty(unitET))
						&& (!isEmpty(quantityET))) {
					addIngredient(ingredientET, unitET, quantityET);
					populateIngredientView();
				}
			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
		alert.show();
	}
	
	
	/**
	 * This method creates a dialog with three edit texts, for ingredient,
	 * quantity, and unit of measurement. There is a 'Cancel' and 'Ok' button.
	 * 
	 * @param index
	 *            The index in each array list of the current item.
	 */
	protected void editIngredientDialog(final int index) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Edit Ingredient");

		final EditText ingredientET = new EditText(this);
		ingredientET.setHint("Ingredient");
		ingredientET.setText(ingredients.get(index));

		final EditText unitET = new EditText(this);
		unitET.setHint("Unit of measurement");
		unitET.setText(units.get(index));

		final EditText quantityET = new EditText(this);
		quantityET.setHint("Quantity");
		quantityET.setText(quantities.get(index));

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(1); // 1 is for vertical orientation
		layout.addView(ingredientET);
		layout.addView(unitET);
		layout.addView(quantityET);

		alert.setView(layout);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if ((!isEmpty(ingredientET)) && (!isEmpty(unitET))
						&& (!isEmpty(quantityET))) {
					editIngredient(ingredientET, unitET, quantityET, index);
					populateIngredientView();
				}
			}
		});
		alert.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				deleteIngredient(index);
			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
		alert.show();
	}
	

	/**
	 * This method sets up the lvIngredients list view to show the contents of
	 * the array list 'combined.'
	 */
	private void populateIngredientView() {
		ListView ingredientsLV = (ListView) findViewById(R.id.lvIngredients);
		registerForContextMenu(ingredientsLV);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, combined);
		ingredientsLV.setAdapter(adapter);

		setListViewOnClickListener(ingredientsLV);

	}

	protected void setListViewOnClickListener(final ListView listView) {

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// AS: launch editIngredientDialog with the position
				editIngredientDialog(position);
			}
		});

	}

	
	/**
	 * This method takes in three edit texts for a new ingredient (the
	 * ingredient, the quantity, and the name) and adds the information to
	 * currentRecipe.
	 * 
	 * @param ingredientET
	 *            the edit text with ingredient
	 * @param unitET
	 *            the edit text with unit of measurement
	 * @param quantityET
	 *            the edit text with quantity
	 */
	private void addIngredient(EditText ingredientET, EditText unitET,
			EditText quantityET) {
		String ingredient = ingredientET.getText().toString();
		String unit = unitET.getText().toString();
		String quantity = quantityET.getText().toString();
		ingredients.add(ingredient);
		units.add(unit);
		quantities.add(quantity);
		combined.add(quantity + " " + unit + " " + ingredient);

	}

	/**
	 * This method takes in three edit texts for a new ingredient (the
	 * ingredient, the quantity, and the name) and an array index. It adds the
	 * information to currentRecipe at index.
	 * 
	 * @param ingredientET
	 *            the edit text with ingredient
	 * @param unitET
	 *            the edit text with unit of measurement
	 * @param quantityET
	 *            the edit text with quantity
	 */
	private void editIngredient(EditText ingredientET, EditText unitET,
			EditText quantityET, int index) {
		String ingredient = ingredientET.getText().toString();
		String unit = unitET.getText().toString();
		String quantity = quantityET.getText().toString();
		ingredients.set(index, ingredient);
		units.set(index, unit);
		quantities.set(index, quantity);
		combined.set(index, quantity + " " + unit + " " + ingredient);

	}

	/**
	 * This method deletes the ingredient, quantity, unit, and combined
	 * entry at index in each array list.
	 * @param index
	 */
	private void deleteIngredient(int index) {
		ingredients.remove(index);
		units.remove(index);
		quantities.remove(index);
		combined.remove(index);
		populateIngredientView();
	}

}
