package ca.ualberta.cs.team07recipefinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

/**
 * @author Torboto
 * 
 *         Activity which displays account creation the first time app is used.
 */
public class UserActivity extends Activity {

	User user = User.getInstance();

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPersonName;

	// UI references.
	private EditText mEmailView;
	private EditText mPersonNameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_user);

		// Set up the login form.
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPersonNameView = (EditText) findViewById(R.id.name);

		findViewById(R.id.buttonCreateAccount).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						verifyUser();
						// setError should return null if no flags have be set
						// during verifyUser.
						if (mEmailView.getError() == null
								&& mPersonNameView.getError() == null) {
							user.setEmail(mEmail);
							user.setName(mPersonName);

							// MA: added this to test for solving the Pantry
							// crashing issue
							user.setPantry(new Pantry());

							user.Write(getApplicationContext());
							launchMainActivity();
						}
					}
				});

		/**
		 * Checks to see if userdata exists in internal app data folder. If so
		 * it is not required for user to login, so skip this activity.
		 */
		if (user.userExists(getApplicationContext())) {
			launchMainActivity();
		}
	}

	/**
	 * Starts main activity.
	 */
	public void launchMainActivity() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		// TODO: Get rid of this flag, should be a better way to launch new
		// activity.
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getApplicationContext().startActivity(intent);
	}

	/**
	 * Attempts to register the account specified by the login form. If there
	 * are form errors (invalid email, missing fields, etc.), the errors are
	 * presented and no actual registration attempt is made.
	 */
	public void verifyUser() {
		// Reset errors.
		mEmailView.setError(null);
		mPersonNameView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPersonName = mPersonNameView.getText().toString();
		View focusView = null;

		// Check for a valid email address and name.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
		}
		// Check for a valid email address.
		if (TextUtils.isEmpty(mPersonName)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mPersonNameView;
		}
	}
}
