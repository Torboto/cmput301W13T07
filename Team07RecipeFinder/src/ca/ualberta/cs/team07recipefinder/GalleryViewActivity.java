package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class GalleryViewActivity extends Activity {
	int sourceCode;
	Recipe currentRecipe;
	RecipeController controller = new RecipeController();
	ArrayList<String> imagePaths;
	ImageView imageview;
	int currentIndex, total;

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
		ImageButton deleteButton = (ImageButton) findViewById(R.id.buttonDelete);

		prevButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentIndex == 0)
					currentIndex = total - 1;
				else
					currentIndex--;
				showImage(currentIndex);
				Toast.makeText(getApplicationContext(),
						currentIndex + 1 + " / " + total,
						Toast.LENGTH_SHORT).show();
			}
		});

		nextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentIndex == total - 1)
					currentIndex = 0;
				else
					currentIndex++;
				showImage(currentIndex);
				Toast.makeText(getApplicationContext(),
						currentIndex + 1 + " / " + total,
						Toast.LENGTH_SHORT).show();
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
					if (currentIndex == 0)
						currentIndex = total - 1;
					else
						currentIndex--;
					showImage(currentIndex);
				} else {
					Toast.makeText(getApplicationContext(),
							"Sorry, you cannot delete the last photo!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
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
