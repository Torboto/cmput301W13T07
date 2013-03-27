package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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

		Button prevButton = (Button) findViewById(R.id.buttonPrev);
		Button nextButton = (Button) findViewById(R.id.buttonNext);
		Button deleteButton = (Button) findViewById(R.id.buttonDelete);

		prevButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentIndex == 0)
					currentIndex = total - 1;
				else
					currentIndex--;
				showImage(currentIndex);
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
			}
		});

		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

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
