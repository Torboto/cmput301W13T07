package ca.ualberta.cs.team07recipefinder;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class Image {
	Bitmap image;
	String filename;
	
	Image(String filename, Bitmap image){
		this.filename = filename;
		this.image = image;
	}
	
	/**
	 * Retrieve the path names of the images saved on the sd card associated
	 * with the recipe id associated with uuid.
	 * 
	 * @param uuid
	 * @return
	 */
	static public ArrayList<String> getAllLocalRecipeImages(UUID uuid) {

		ArrayList<String> images = new ArrayList<String>();

		try {
			// Specify the expected file path for the recipe images.
			File path = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/tmp/" + String.valueOf(uuid));
			File[] files = path.listFiles();

			for (File file : files) {
				images.add(String.valueOf(file));
			}
		} catch (Exception e) {
			// The folder may not exist or there are no images
			Log.w("Recipe Image Error: ", "No such folder or file.");
		}
		return images;
	}

	static public ArrayList<String> getAllServerRecipeImages(UUID uuid){
		return null;
	}
	
	/**
	 * Retrieves the bitmap saved at the path specified by path
	 * 
	 * @param path
	 * @return
	 */
	static public Bitmap getLocalThumbnailImage(String path) {
		Bitmap bmp = null;
		File f = new File(path);
		try {
			bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
		} catch (Exception e) {
			// No file exists matching the path
		}
		return bmp;
	}
	
	static public void saveImageToServer(Bitmap bitmap){
		
	}
	
	static public Bitmap getServerThumbnailImage(UUID uuid) {
		return null;
	}

	/**
	 * Deletes the recipe image file located at the file path.
	 * 
	 * @param path
	 */
	static public void deleteLocalImage(String path) {
		File file = new File(path);
		try {
			file.delete();
		} catch (Exception e) {
			// No file exists with that path
			Log.w("Delete image", "Recipe image cannot be deleted");
		}
	}
}
