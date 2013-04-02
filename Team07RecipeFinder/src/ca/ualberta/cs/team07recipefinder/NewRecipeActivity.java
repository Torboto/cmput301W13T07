package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
				&& (!isEmpty(directionsEditText))
				&& (!newRecipe.getIngredients().isEmpty())) {
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
		;
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
	 * This method sets up the lvIngredients list view to show the contents of
	 * the array list 'combined.'
	 */
	private void populateIngredientView() {
		ListView ingredientsLV = (ListView) findViewById(R.id.lvIngredients);
		registerForContextMenu(ingredientsLV);

		ArrayList<String> combined = RecipeView.formCombinedArray(newRecipe);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, combined);
		ingredientsLV.setAdapter(adapter);

		setListViewOnClickListener(ingredientsLV);
		setListViewHeightBasedOnChildren(ingredientsLV);
	}

	/**
	 * This method sets up the ingredients to launch the edit ingredient dialog
	 * when clicked on.
	 */
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
	private void ingredientDialog() {
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
					RecipeView.addIngredient(ingredientET, unitET, quantityET,
							newRecipe);
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
		ingredientET.setText(newRecipe.getIngredients().get(index));

		final EditText unitET = new EditText(this);
		unitET.setHint("Unit of measurement");
		unitET.setText(newRecipe.getUnits().get(index));

		final EditText quantityET = new EditText(this);
		quantityET.setHint("Quantity");
		quantityET.setText(newRecipe.getQuantities().get(index));

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
					RecipeView.editIngredient(ingredientET, unitET, quantityET,
							index, newRecipe);
					populateIngredientView();
				}
			}
		});
		alert.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				RecipeView.deleteIngredient(index, newRecipe);
				populateIngredientView();
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
	 * This method takes an EditText and returns true if it is empty and false
	 * otherwise.
	 * 
	 * @param etText
	 *            the EditText to be tested
	 * @return True: if empty, false: otherwise.
	 */
	private boolean isEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0)
			return false;
		else
			return true;
	}
	
	/**
	 * Estimates size of listview to allow it to be inside the scrollview
	 * 
	 * @param listView
	 *            Listview object so that new height parameter can be set
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += 94;
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
}
