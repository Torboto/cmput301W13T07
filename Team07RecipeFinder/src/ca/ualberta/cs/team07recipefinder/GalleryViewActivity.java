package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class GalleryViewActivity extends Activity {
  int sourceCode;
	Recipe currentRecipe;
	RecipeController controller = new RecipeController();
	ArrayList<String> imagePaths;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_view);

		Bundle extras = getIntent().getExtras();
		sourceCode = extras.getInt("code");
		final String recipeString = extras.getString("recipeId");

		ImageView imageview = (ImageView) findViewById(R.id.imageView);
		
		fillCurrentRecipe(recipeString);

		ArrayList<String> imagePaths = ImageController.getAllRecipeImages(
				currentRecipe.getRecipeId(), currentRecipe.location);

		if (imagePaths.size() > 0) {
			imageview.setImageBitmap(ImageController.getThumbnailImage(
					imagePaths.get(0), currentRecipe.location));
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
