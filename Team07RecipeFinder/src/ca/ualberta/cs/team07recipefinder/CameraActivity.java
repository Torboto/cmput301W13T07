package ca.ualberta.cs.team07recipefinder;

import java.io.File;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author edited by gcoomber
 * 
 *         Activity that allows the user to take a picture and save it to the sd
 *         storage. The code is an altered version of the lab example code for
 *         the camera test.
 */
public class CameraActivity extends Activity {

	Camera camera;
	Uri imageFileUri;
	Context context;
	int duration = Toast.LENGTH_SHORT;
	Toast toast;
	ImageView iv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		TextView tv = (TextView) findViewById(R.id.status);
		tv.setText("Click to take a photo\n");
		iv = (ImageView) findViewById(R.id.ivPreview);
		iv.setImageResource(R.drawable.recipe_image_outline);
		
		ImageButton button = (ImageButton) findViewById(R.id.ibTakeAPhoto);
		OnClickListener listener = new OnClickListener() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			public void onClick(View v) {
				takeAPhoto();
			}
		};
		button.setOnClickListener(listener);

		// MA: Save button will finish this activity. Cancel button will delete
		// the temporary saved image.
		Button cancelButton = (Button) findViewById(R.id.buttonCancelImage);
		Button saveButton = (Button) findViewById(R.id.buttonSaveImage);

		context = getApplicationContext();
		duration = Toast.LENGTH_SHORT;

		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// delete photo
				// deleteTempPhoto();
				finish();
			}
		});

		// Get the name of the folder the image will be saved in
		Bundle extras = getIntent().getExtras();
		String folderName = extras.getString("recipeId");
		int imageNumber = extras.getInt("imageNumber") + 1;

		this.camera = new Camera(folderName, imageNumber);
	}

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	/**
	 * Take a photo by saving a photo in the temp folder in the external storage
	 */
	public void takeAPhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File imageFile = camera.getFile();
		imageFileUri = Uri.fromFile(imageFile);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			TextView tv = (TextView) findViewById(R.id.status);
			if (resultCode == RESULT_OK) {
				tv.setText("Photo OK!");
				ImageView iv = (ImageView) findViewById(R.id.ivPreview);
				iv.setRotation(90);
				iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
				iv.setImageDrawable(Drawable.createFromPath(imageFileUri
						.getPath()));
			} else if (resultCode == RESULT_CANCELED) {
				tv.setText("Photo canceled");
			} else {
				tv.setText("Not sure what happened!" + resultCode);
			}
		}
	}

}
