package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GalleryViewActivity extends Activity {
	int sourceCode;
	Recipe currentRecipe;
	RecipeController controller = new RecipeController();
	ArrayList<String> imagePaths;
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

		imagePaths = ImageController.getAllRecipeImages(
				currentRecipe.getRecipeId(), currentRecipe.location);

		currentIndex = 0;
		total = imagePaths.size();
		if (imagePaths.size() > 0) {
			imageview.setImageBitmap(ImageController.getThumbnailImage(
					imagePaths.get(currentIndex), currentRecipe.location));
		}

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
				RecipeController.updateImageNumber(currentRecipe);

				Intent cameraIntent = new Intent(getApplicationContext(),
						CameraActivity.class);
				cameraIntent.putExtra("recipeId",
						String.valueOf(currentRecipe.getRecipeId()));
				cameraIntent.putExtra("imageNumber",
						currentRecipe.getImageNumber() + total + 1);
				startActivityForResult(cameraIntent, RESULT_OK);
			}
		});

		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (total > 1) {
					Image.deleteLocalImage(imagePaths.get(currentIndex));
					imagePaths = ImageController.getAllRecipeImages(
							currentRecipe.getRecipeId(), currentRecipe.location);
					total = imagePaths.size();
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		imagePaths = ImageController.getAllRecipeImages(
				currentRecipe.getRecipeId(), currentRecipe.location);
		total = imagePaths.size();
		currentIndex = total - 1;
		if (imagePaths.size() > 0) {
			imageview.setImageBitmap(ImageController.getThumbnailImage(
					imagePaths.get(currentIndex), currentRecipe.location));
		}
	}

	protected void showImage(int index) {
		if (imagePaths.size() > 0) {
			imageview.setImageBitmap(ImageController.getThumbnailImage(
					imagePaths.get(index), currentRecipe.location));
		}
	}

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
