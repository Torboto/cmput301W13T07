package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
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
 * @author ajstarna
 * 
 */
public class ViewRecipeActivity extends Activity {
	int sourceCode;
	KeyListener titleListener;
	KeyListener descriptionListener;
	KeyListener directionsListener;

	Recipe currentRecipe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipe);

		Bundle extras = getIntent().getExtras();
		sourceCode = extras.getInt("code");
		final String recipeString = extras.getString("recipeId");

		// AS: call fillCurrentRecipe() to get the recipe from either the local
		// database
		// or server.
		fillCurrentRecipe(recipeString);

		// AS: hide the add ingredient button
		Button addButton = (Button) findViewById(R.id.bNewIngredient);
		addButton.setVisibility(4);

		// ET: Save current recipe to cache
		Button saveButton = (Button) findViewById(R.id.b_recipeSave);
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SqlClient sqlClient = new SqlClient(getApplicationContext());
				sqlClient.writeRecipe(currentRecipe);
				showRecipeSaved();
			}
		});

		// MA: start GalleryViewActivity when click on the thumbnail.
		ImageButton pictureButton = (ImageButton) findViewById(R.id.ibRecipe);
		pictureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Only editable in editmode
					Intent galleryIntent = new Intent(getApplicationContext(),
							ImageGalleryActivity.class);
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
				User user = User.getInstance();
				user.emailRecipe(currentRecipe, getApplicationContext());
			}

		});

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
					// the edit mode is activated
					if (isEditableRecipe()) {
						editMode();
						Button saveButton = (Button) findViewById(R.id.b_recipeSave);
						saveButton
								.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										// AS: The save button calls editRecipe
										// then finishes
										editRecipe(recipeString);
										savedDialog();
									}
								});

						Button newIngredientButton = (Button) findViewById(R.id.bNewIngredient);

						newIngredientButton
								.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										ingredientDialog();
									}
								});

					} else {
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

		// PopulateImages cannot be called before the async task has returned
		// with recipe object, otherwise currentRecipe will be null.
		if (currentRecipe != null
				&& currentRecipe.location == Recipe.Location.LOCAL) {
			populateImages();
		}
	}

	/**
	 * Populates thumbnail of recipe.
	 */
	public void populateImages() {
		// Check if the recipe has any images saved on the sd card and get
		// the bitmap for the imagebutton

		ArrayList<Image> images = ImageController.getAllRecipeImages(
				currentRecipe.getRecipeId(), currentRecipe.location);

		Log.w("*****", "outside");
		Log.w("*****", String.valueOf(currentRecipe.getRecipeId()));
		Log.w("*****", String.valueOf(currentRecipe.location));
		ImageButton pictureButton = (ImageButton) findViewById(R.id.ibRecipe);

		// Set the image of the imagebutton to the first image in the folder
		if (images.size() > 0) {
			pictureButton.setImageBitmap(images.get(0).getBitmap());
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
			populateRecipeInfo();
			populateImages();
		}
		if (sourceCode == 2) {
			SearchRecipeTask search = new SearchRecipeTask(recipeID);

			search.setDataDownloadListener(new DataDownloadListener() {
				public void dataDownloadedSuccessfully(ArrayList<Recipe> data) {
					currentRecipe = data.get(0);
					populateRecipeInfo();
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
	private void populateRecipeInfo() {
		String title = currentRecipe.getTitle();
		String directions = currentRecipe.getDirections();
		String description = currentRecipe.getDescription();

		fillTextViews(title, description, directions);
		populateIngredientView();
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
			String directions) {
		// AS: first create the edit text objects
		EditText etTitle = (EditText) findViewById(R.id.etRecipeTitle);
		EditText etDescription = (EditText) findViewById(R.id.etRecipeDescription);
		EditText etDirections = (EditText) findViewById(R.id.etDirectionsList);

		// AS: then set the text views
		etTitle.setText(title);
		etDescription.setText(description);
		etDirections.setText(directions);

		// AS: save the original key listeners
		titleListener = etTitle.getKeyListener();
		descriptionListener = etDescription.getKeyListener();
		directionsListener = etDirections.getKeyListener();

		// AS: then set them to be uneditable
		etTitle.setKeyListener(null);
		etDescription.setKeyListener(null);
		etDirections.setKeyListener(null);

	}

	/**
	 * This method hides the save button. It is called the user is viewing one
	 * of their own recipes, so the save button is not needed.
	 */
	private void hideSave() {
		Button saveButton = (Button) findViewById(R.id.b_recipeSave);
		saveButton.setVisibility(4);
	}

	/**
	 * This method makes the save button visible again, for when the user enters
	 * edit mode.
	 */
	private void showSaveAndAdd() {
		Button saveButton = (Button) findViewById(R.id.b_recipeSave);
		Button addButton = (Button) findViewById(R.id.bNewIngredient);
		saveButton.setVisibility(1);
		addButton.setVisibility(1);
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
	}

	/**
	 * This method hides the email button. It is called if the user enters edit
	 * mode.
	 */
	private void hideEmail() {
		Button emailButton = (Button) findViewById(R.id.b_recipeEmail);
		emailButton.setVisibility(4);
	}

	/**
	 * This method changes the edit texts and ingredients to now be editable for
	 * the user.
	 */
	private void editableEditTexts() {
		EditText etTitle = (EditText) findViewById(R.id.etRecipeTitle);
		EditText etDescription = (EditText) findViewById(R.id.etRecipeDescription);
		EditText etDirections = (EditText) findViewById(R.id.etDirectionsList);

		etTitle.setKeyListener(titleListener);
		etDescription.setKeyListener(descriptionListener);
		etDirections.setKeyListener(directionsListener);

		setListViewOnClickListener();

	}

	/**
	 * This method sets up the recipe to be edited.
	 * 
	 * @param recipeString
	 *            the string representation of a recipe UUID
	 */
	private void editMode() {
		// Set the boolean to true to indicate in edit mode
		editableEditTexts();
		hideEditDelete();
		hideEmail();
		showSaveAndAdd();
		showThatEditable();
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
		RecipeController.deleteLocalRecipe(recipeID, getApplicationContext());
	}

	/**
	 * This method edits the recipe in the database,
	 * 
	 * @param recipeString
	 *            the string representation of a recipe UUID
	 */
	private void editRecipe(String recipeString) {
		UUID recipeID = UUID.fromString(recipeString);
		Recipe newRecipe = grabRecipeInfo();
		RecipeController.updateRecipe(recipeID, newRecipe,
				getApplicationContext());
	}

	/**
	 * This method forms and returns a new Recipe object by getting the
	 * necessary information from the Activity's EditTexts.
	 * 
	 * @return the newly created Recipe.
	 */
	private Recipe grabRecipeInfo() {
		// Get the number of images the recipe has
		ImageController.updateImageNumber(currentRecipe);

		EditText etTitle = (EditText) findViewById(R.id.etRecipeTitle);
		EditText etDescription = (EditText) findViewById(R.id.etRecipeDescription);
		EditText etDirections = (EditText) findViewById(R.id.etDirectionsList);

		String title = etTitle.getText().toString();
		String description = etDescription.getText().toString();
		String directions = etDirections.getText().toString();
		String email = currentRecipe.getCreatorEmail();
		Recipe newRecipe = new Recipe();
		newRecipe.setName(title);
		newRecipe.setDescription(description);
		newRecipe.setDirections(directions);
		newRecipe.setIngredients(currentRecipe.getIngredients());
		newRecipe.setQuantities(currentRecipe.getQuantities());
		newRecipe.setUnits(currentRecipe.getUnits());
		newRecipe.setCreatorEmail(email);
		newRecipe.setRecipeId(currentRecipe.getRecipeId());
		return newRecipe;

	}

	/**
	 * This method determines if the current recipe is editable. If the user's
	 * email matches the creator of the recipe's email, then it is.
	 * 
	 * @return true if editable or false otherwise
	 */
	private boolean isEditableRecipe() {
		User user = User.getInstance();
		String userEmail = user.getEmail();
		String creatorEmail = currentRecipe.getCreatorEmail();
		if (userEmail.equalsIgnoreCase(creatorEmail)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method sets up the lvIngredients list view to show the contents of
	 * the array list 'combined' attribute of recipe.
	 */
	private void populateIngredientView() {
		ListView ingredientsLV = (ListView) findViewById(R.id.lv_Ingredients);
		registerForContextMenu(ingredientsLV);

		ArrayList<String> combined = RecipeView
				.formCombinedArray(currentRecipe);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, combined);
		ingredientsLV.setAdapter(adapter);
		setListViewHeightBasedOnChildren(ingredientsLV);
	}

	/**
	 * This method sets up the ingredients to launch the edit ingredient dialog
	 * when clicked on.
	 */
	protected void setListViewOnClickListener() {

		ListView ingredientsLV = (ListView) findViewById(R.id.lv_Ingredients);
		registerForContextMenu(ingredientsLV);

		ingredientsLV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// AS: launch editIngredientDialog with the position
				editIngredientDialog(position);
			}
		});

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
					RecipeView.addIngredient(ingredientET, unitET, quantityET,
							currentRecipe);
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
		ingredientET.setText(currentRecipe.getIngredients().get(index));

		final EditText unitET = new EditText(this);
		unitET.setHint("Unit of measurement");
		unitET.setText(currentRecipe.getUnits().get(index));

		final EditText quantityET = new EditText(this);
		quantityET.setHint("Quantity");
		quantityET.setText(currentRecipe.getQuantities().get(index));

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
							index, currentRecipe);
					populateIngredientView();
				}
			}
		});
		alert.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				RecipeView.deleteIngredient(index, currentRecipe);
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
	 * This method creates a dialog which informs the user that the changes to
	 * the current recipe have been saved and finishes the activity on click of
	 * OK.
	 */
	private void savedDialog() {
		TextView tv = new TextView(this);
		tv.setText("Changes to the current recipe have been saved.");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Success");
		alert.setView(tv);
		alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});
		alert.show();
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

	/**
	 * This method creates a dialog which informs the user that there are no
	 * search results to return
	 */
	private void showRecipeSaved() {
		TextView tv = new TextView(this);
		tv.setText("Recipe has been saved in MyRecipes!");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Complete!");
		alert.setView(tv);
		alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}

	/**
	 * This method takes an EditText and returns true if it is empty and false
	 * otherwise.
	 * 
	 * <
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
