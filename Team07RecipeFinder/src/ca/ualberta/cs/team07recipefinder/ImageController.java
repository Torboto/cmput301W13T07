package ca.ualberta.cs.team07recipefinder;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
	static public ArrayList<String> getAllRecipeImages(UUID uuid, Recipe.Location location) {
		ArrayList<String> images = new ArrayList<String>();

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
	static public void deleteImage(String path, Recipe.Location location) {
		if (location == Recipe.Location.LOCAL) {
			Image.deleteLocalImage(path);
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
			ArrayList<String> imagePaths = getAllRecipeImages(uuid, location);
			maxImageNumber = imagePaths.size();
		} else if(location == Recipe.Location.SERVER) {
			// TODO: SERVERSTUFF
		}

		return maxImageNumber;
	}

}