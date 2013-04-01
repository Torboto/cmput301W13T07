package ca.ualberta.cs.team07recipefinder;

import java.io.File;

import android.os.Environment;

/**
 * @author gcoomber
 * 
 *         A helper class that contains data and methods for the camera
 *         activity.
 * 	
 */
public class Camera {
	private String folderName;
	int imageNumber;

	/**
	 * @param folderName
	 * @param imageNumber
	 */
	public Camera(String folderName, int imageNumber) {
		this.folderName = folderName;
		this.imageNumber = imageNumber;
	};

	/**
	 * Gets the file with the file path that the image will be saved at.
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
