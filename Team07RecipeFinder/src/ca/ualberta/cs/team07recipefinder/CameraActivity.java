package ca.ualberta.cs.team07recipefinder;

import java.io.File;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Activity that allows the user to take a picture and save it to the sd
 * storage. The code is an altered version of the lab example code for the
 * camera test.
 * 
 * @author edited by gcoomber
 * 
 */
public class CameraActivity extends Activity {

	Uri imageFileUri;
	String folderName = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		ImageButton button = (ImageButton) findViewById(R.id.TakeAPhoto);
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				takeAPhoto();
			}
		};
		button.setOnClickListener(listener);
		
		// Get the name of the folder the image will be saved in
		Bundle extras = getIntent().getExtras();
		folderName = extras.getString("recipeId");
	}

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	/**
	 * Take a photo by saving a photo in the temp folder in the external storage
	 * with the current time as the jpg name.
	 */
	public void takeAPhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		String folder = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/tmp/" + folderName;
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();
		}

		String imageFilePath = folder + "/"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg";
		File imageFile = new File(imageFilePath);
		imageFileUri = Uri.fromFile(imageFile);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			TextView tv = (TextView) findViewById(R.id.status);
			if (resultCode == RESULT_OK) {
				tv.setText("Photo OK!");
				ImageButton button = (ImageButton) findViewById(R.id.TakeAPhoto);
				button.setImageDrawable(Drawable.createFromPath(imageFileUri
						.getPath()));
			} else if (resultCode == RESULT_CANCELED) {
				tv.setText("Photo canceled");
			} else {
				tv.setText("Not sure what happened!" + resultCode);
			}
		}
	}
}
