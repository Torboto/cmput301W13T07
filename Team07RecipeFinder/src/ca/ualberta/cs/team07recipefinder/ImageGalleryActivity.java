package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author xiaohui
 * 
 *         This is the gallery view activity for viewing images of a recipe.
 *         User can delete or add a image in this.
 */
public class ImageGalleryActivity extends Activity {
	int sourceCode;
	Recipe currentRecipe;
	RecipeController controller = new RecipeController();
	// ArrayList<String> imagePaths;
	ArrayList<Image> images;
	ImageView imageview;
	int currentIndex, total;
	Context context;
	int duration = Toast.LENGTH_SHORT;
	Toast toast;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_view);

		Bundle extras = getIntent().getExtras();
		sourceCode = extras.getInt("code");
		final String recipeString = extras.getString("recipeId");

		imageview = (ImageView) findViewById(R.id.imageView);

		fillCurrentRecipe(recipeString);

		/*
		 * images = ImageController.getAllRecipeImages(
		 * currentRecipe.getRecipeId(), currentRecipe.location);
		 * 
		 * currentIndex = 0; total = images.size(); if (images.size() > 0) {
		 * imageview.setImageBitmap(images.get(currentIndex).getBitmap()); }
		 */

		currentIndex = 0;
		loadImages();

		ImageButton prevButton = (ImageButton) findViewById(R.id.buttonPrev);
		ImageButton nextButton = (ImageButton) findViewById(R.id.buttonNext);
		ImageButton addButton = (ImageButton) findViewById(R.id.buttonAdd);
		ImageButton deleteButton = (ImageButton) findViewById(R.id.buttonDelete);

		context = getApplicationContext();
		duration = Toast.LENGTH_SHORT;

		prevButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentIndex <= 0)
					currentIndex = total - 1;
				else
					currentIndex--;
				showImage(currentIndex);
				if (toast != null) {
					toast.cancel();
				}
				toast = Toast.makeText(context, currentIndex + 1 + " of "
						+ total, duration);
				toast.show();
			}
		});

		nextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentIndex >= total - 1)
					currentIndex = 0;
				else
					currentIndex++;
				showImage(currentIndex);
				if (toast != null) {
					toast.cancel();
				}
				toast = Toast.makeText(context, currentIndex + 1 + " of "
						+ total, duration);
				toast.show();
			}
		});

		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get the number of images the recipe has
				ImageController.updateImageNumber(currentRecipe);

				Intent cameraIntent = new Intent(getApplicationContext(),
						CameraActivity.class);
				cameraIntent.putExtra("recipeId",
						String.valueOf(currentRecipe.getRecipeId()));
				Log.w("&&&", String.valueOf(currentRecipe.getImageNumber()));
				cameraIntent.putExtra("imageNumber",
						currentRecipe.getImageNumber() + 1);
				startActivity(cameraIntent);
			}
		});

		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Image tempImage;
				if (total > 1) {
					tempImage = images.get(currentIndex);
					tempImage.deleteLocalImage();
					images = ImageController.getAllRecipeImages(
							currentRecipe.getRecipeId(), currentRecipe.location);
					total = images.size();
					if (currentIndex == total)
						currentIndex--;
					showImage(currentIndex);
					if (toast != null) {
						toast.cancel();
					}
					toast = Toast.makeText(context, currentIndex + 1 + " of "
							+ total, duration);
					toast.show();
				} else {
					if (toast != null) {
						toast.cancel();
					}
					toast = Toast.makeText(context,
							"Sorry, you need to keep at least one photo!",
							duration);
					toast.show();
				}
			}
		});
	}

	/**
	 *         This method will reload the images variable in case the images
	 *         are changed.
	 */
	protected void loadImages() {
		images = ImageController.getAllRecipeImages(
				currentRecipe.getRecipeId(), currentRecipe.location);
		total = images.size();
		if (images.size() > 0) {
			imageview.setImageBitmap(images.get(currentIndex).getBitmap());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		loadImages();
	}

	/**
	 *         This method will reset the currently showing image in the
	 *         ImageView given the current index.
	 * 
	 * @param index
	 */
	protected void showImage(int index) {
		if (images.size() > 0) {
			imageview.setImageBitmap(images.get(currentIndex).getBitmap());
		}
	}

	/**
	 * @param recipeString
	 */
	private void fillCurrentRecipe(String recipeString) {
		// AS: first get the recipe from the database using a recipeController
		UUID recipeID = UUID.fromString(recipeString);

		if (sourceCode == 1) {
			currentRecipe = RecipeController.getLocalRecipe(recipeID,
					getApplicationContext());
		}
		if (sourceCode == 2) {
			SearchRecipeTask search = new SearchRecipeTask(recipeID);

			search.setDataDownloadListener(new DataDownloadListener() {
				public void dataDownloadedSuccessfully(ArrayList<Recipe> data) {
					currentRecipe = data.get(0);
				}
			});
			search.execute("");
		}
	}
}
