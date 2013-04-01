package ca.ualberta.cs.team07recipefinder;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * @author Torboto, gcoomber
 * 
 *         Data class to hold filename and bitmap together for saving purposes.
 */
public class Image {
	Bitmap image;
	String filename;

	Image(String filename, Bitmap image) {
		this.filename = filename;
		this.image = image;
	}

	/**
	 * Retrieves the bitmap saved at the path specified by path
	 * 
	 * @param path
	 *            Path to SD card on local device.
	 * @return Bitmap object to be used as the thumbnail.
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

	/**
	 * Gets images saved on the sd card associated with the recipe id associated
	 * with uuid.
	 * 
	 * @param uuid
	 *            UUID to indentify recipe by.
	 * @return List of images that belong to recipe.
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
				Bitmap bitmap = Image.getLocalThumbnailImage(String
						.valueOf(file));
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

	/**
	 * Retrieves all images associated with recipe from online.
	 * 
	 * @param uuid
	 *            Recipe id.
	 * @return All images for recipe.
	 */
	static public ArrayList<String> getAllServerRecipeImages(UUID uuid) {
		return null;
	}

	/**
	 * Save image online using uuid as filename.
	 * 
	 * @param bitmap
	 *            Image to be saved.
	 * @param uuid
	 *            UUID of associated recipe to be used in filename.
	 */
	static public void saveImageToServer(Bitmap bitmap, UUID uuid) {

	}

	/**
	 * Returns first image to be used as thumbnail.
	 * 
	 * @param uuid
	 *            Recipe Id.
	 * @return Single image to be used as thumbnail image.
	 */
	static public Bitmap getServerThumbnailImage(UUID uuid) {
		return null;
	}

	/**
	 * Deletes the recipe image file located at the file path.
	 */
	public void deleteLocalImage() {
		try {
			String[] nameComponents = this.filename.split("_");
			Log.w("DELETING", "DELETING");
			if (nameComponents.length <= 1) {
				Log.e("ERROR",
						"ERROR: image name incorrect for deleteLocalImage");
				return;
			}
			Log.w("DELETING", "Test");
			String filePath = getStoragePath() + nameComponents[0] + "/"
					+ filename;
			Log.w("DELETING", filePath);
			File file = new File(filePath);
			file.delete();
		} catch (Exception e) {
			// No file exists with that path
			Log.e("Delete image", "ERROR: Recipe image cannot be deleted");
		}
	}

	/**
	 * Returns the path of the folder where image folders are saved
	 * 
	 * @return Storage path for current device.
	 */
	public String getStoragePath() {
		String storagePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/tmp/";
		return storagePath;
	}

	@SuppressWarnings("javadoc")
	public Bitmap getBitmap() {
		return this.image;
	}

	@SuppressWarnings("javadoc")
	public String getFilename() {
		return this.filename;
	}
}
