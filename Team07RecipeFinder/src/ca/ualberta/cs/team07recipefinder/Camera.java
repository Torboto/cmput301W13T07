package ca.ualberta.cs.team07recipefinder;

import java.io.File;

import android.os.Environment;

/**
 * @author gcoomber
 * 
 * A class that contains the folder name where images are saved, the
 * number of images in the file, and a method for setting the file for the
 * bitmap image.
 * 
 */
public class Camera {
	private String folderName;
	private int imageNumber;

	/**
	 * @param folderName
	 * @param imageNumber
	 */
	public Camera(String folderName, int imageNumber) {
		this.folderName = folderName;
		this.imageNumber = imageNumber;
	};

	/**
	 * Creates and returns the file with the file path where the image 
	 * will be saved.
	 * 
	 * @return
	 */
	public File getFile() {
		String folder = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/tmp/" + this.folderName;
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();
		}

		String imageFilePath = folder + "/" + folderName + "_"
				+ String.valueOf(imageNumber) + ".jpg";
		File imageFile = new File(imageFilePath);

		return imageFile;
	}

}
