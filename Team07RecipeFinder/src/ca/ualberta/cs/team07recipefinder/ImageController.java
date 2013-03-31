package ca.ualberta.cs.team07recipefinder;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

/**
 * 
 */

/**
 * @author Torboto
 * 
 */
public class ImageController {
	/**
	 * Retrieve the path names of the images saved on the sd card associated
	 * with the recipe id associated with uuid.
	 * 
	 * @param uuid
	 * @return
	 */
	
	/**
	 * Retrieve the path names of the images saved on the sd card associated
	 * with the recipe id associated with uuid.
	 * 
	 * @param uuid
	 * @return
	 */
	static public ArrayList<Image> getAllLocalRecipeImages(UUID uuid) {

		ArrayList<Image> images = new ArrayList<Image>();

		try {
			// Specify the expected file path for the recipe images.
			File path = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/tmp/" + String.valueOf(uuid));
			
			File[] files = path.listFiles();

			int i = 1;
			// Create the image object
			for (File file : files) {
				String name = String.valueOf(uuid) + "_" + i + ".jpg";
				Bitmap bitmap = Image.getLocalThumbnailImage(String.valueOf(file));
				Image tempImage = new Image(name, bitmap);
				images.add(tempImage);
				i++;
			}
		} catch (Exception e) {
			// The folder may not exist or there are no images
			Log.e("getAllLocalRecipeImages: ", "ERROR: No such folder or file.");
		}
		return images;
	}
	
	static public ArrayList<Image> getAllRecipeImages(UUID uuid, Recipe.Location location) {
		ArrayList<Image> images = new ArrayList<Image>();

		if (location == Recipe.Location.LOCAL) {
			images = getAllLocalRecipeImages(uuid);
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
	 * @return
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
	 * @param path
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

	// Gets the number of images that a recipe has saved on the sdcard
	static public int getNumberImages(UUID uuid, Recipe.Location location) {
		int maxImageNumber = -1;

		if(location == Recipe.Location.LOCAL) {
			// get the paths of all images saved locally
			ArrayList<Image> images = getAllRecipeImages(uuid, location);
			maxImageNumber = images.size();
		} else if(location == Recipe.Location.SERVER) {
			// TODO: SERVERSTUFF
		}

		return maxImageNumber;
	}

}