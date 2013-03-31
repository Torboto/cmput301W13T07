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

	Image(String filename, Bitmap image) {
		this.filename = filename;
		this.image = image;
	}
	
	public Bitmap getBitmap() {
		return this.image;
	}
	
	public String getFilename() {
		return this.filename;
	}

	static public ArrayList<String> getAllServerRecipeImages(UUID uuid) {
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

	static public void saveImageToServer(Bitmap bitmap) {

	}

	static public Bitmap getServerThumbnailImage(UUID uuid) {
		return null;
	}

	/**
	 * Deletes the recipe image file located at the file path.
	 * 
	 * @param path
	 */
	public void deleteLocalImage() {
		try {
			String[] nameComponents = this.filename.split("_");
			Log.w("DELETING", "DELETING");
			if(nameComponents.length <= 1)
			{
				Log.e("ERROR", "ERROR: image name incorrect for deleteLocalImage");
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
	 * returns the path of the folder where image folders are saved
	 * @return
	 */
	public String getStoragePath() {
		String storagePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/tmp/";
		return storagePath;
	}
}
