package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Controller for images. Decides how to access the image depending on where the
 * recipe is saved (internal/external).
 * 
 * @author Torboto
 */
public class ImageController {
	/**
	 * Gets images for recipe from local and external, depending on where it is
	 * located.
	 * 
	 * @param uuid
	 *            UUID to identify recipe by.
	 * @param location
	 *            Recipe location enum.
	 * @return All images associated with this recipe.
	 */
	static public ArrayList<Image> getAllRecipeImages(UUID uuid,
			Recipe.Location location) {
		ArrayList<Image> images = new ArrayList<Image>();

		if (location == Recipe.Location.LOCAL) {
			images = Image.getAllLocalRecipeImages(uuid);
		} else if (location == Recipe.Location.SERVER) {

		} else {
			Log.e("ImageController",
					"ERROR: No recipe location set in getAllRecipeImages");
		}
		return images;
	}

	/**
	 * Retrieves the bitmap saved at the path specified by path
	 * 
	 * @param path
	 *            Path to SD card in local device.
	 * @param location
	 *            Recipe location enum.
	 * @return A single representative image for recipe.
	 */
	static public Bitmap getThumbnailImage(String path, Recipe.Location location) {
		Bitmap bmp = null;

		if (location == Recipe.Location.LOCAL) {
			bmp = Image.getLocalThumbnailImage(path);
		} else if (location == Recipe.Location.SERVER) {

		} else {
			Log.e("ImageController",
					"ERROR: No recipe location set in setThumbnailImage");
		}
		return bmp;
	}

	static public Bitmap getExternalImage(UUID uuid) {
		return null;
	}

	/**
	 * Deletes the recipe image file located at the file path.
	 * 
	 * @param image
	 *            Image object as it contains filename
	 * @param location
	 *            Where images are stored on current device
	 */
	static public void deleteImage(Image image, Recipe.Location location) {
		if (location == Recipe.Location.LOCAL) {
			image.deleteLocalImage();
		} else if (location == Recipe.Location.SERVER) {

		} else {
			Log.e("ImageController",
					"ERROR: No recipe location set in deleteImage");
		}
	}

	/**
	 * Gets the number of images that a recipe has saved on the sdcard
	 * 
	 * @param uuid
	 *            Recipe to be examined.
	 * @param location
	 *            Where recipe is stored (local/external).
	 * @return Number of images said recipe has.
	 */
	static public int getNumberImages(UUID uuid, Recipe.Location location) {
		int maxImageNumber = -1;

		if (location == Recipe.Location.LOCAL) {
			// get the paths of all images saved locally
			ArrayList<Image> images = getAllRecipeImages(uuid, location);
			maxImageNumber = images.size();
		} else if (location == Recipe.Location.SERVER) {
			// TODO: SERVERSTUFF
		}

		return maxImageNumber;
	}

	/**
	 * Update the integer value that represents the number of saved images for a
	 * locally saved recipe.
	 * 
	 * @param recipe
	 *            Recipe to be updated.
	 */
	static public void updateImageNumber(Recipe recipe) {
		int imageNumber = ImageController.getNumberImages(recipe.getRecipeId(),
				Recipe.Location.LOCAL);
		recipe.setImageNumber(imageNumber);
	}

}