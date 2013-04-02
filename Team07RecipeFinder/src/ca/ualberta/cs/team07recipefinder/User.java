package ca.ualberta.cs.team07recipefinder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @author Torboto
 * 
 *         Contains user credentials and any information that requires
 *         persistence.
 */
@SuppressWarnings("serial")
public class User implements Serializable {
	private static User instance = null;
	private String name;
	private String email;
	private Pantry pantry;
	private String filename = "userdata";

	/**
	 * Singleton constructor so that only one user exists on each device, and
	 * pantry data is persistent.
	 * 
	 * @return The current instance of user.
	 */
	public static User getInstance() {
		if (instance == null) {
			instance = new User();
		}
		return instance;
	}

	/**
	 * Sets name/email member variables for object, and then writes serilized
	 * form to file userdata in the application data folder.
	 * 
	 * @param context
	 *            Required to be able to write userdata file to local memory.
	 */
	public void Write(Context context) {
		try {
			context.deleteFile(filename);
			FileOutputStream fout = context.openFileOutput(filename,
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(this);
			oos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Reads in User object from userdata file in application data folder
	 * 
	 * @param context
	 *            Required to be able to read userdata file from local memory.
	 */
	public void Read(Context context) {
		User user = null;
		FileInputStream fin = null;
		ObjectInputStream ois = null;

		try {
			fin = context.openFileInput(this.getFilename());
			ois = new ObjectInputStream(fin);
			user = (User) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		email = user.email;
		name = user.name;
		pantry = user.pantry;

		try {
			fin.close();
			ois.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Checks to see if userdata exists in internal app data folder.
	 * 
	 * @param context
	 *            Required to be able to read userdata file from local memory.
	 * @return If userdata exists, returns true.
	 */
	public Boolean userExists(Context context) {
		File file = context.getFileStreamPath(this.getFilename());

		if (file.exists()) {
			this.Read(context);
			return true;
		} else {
			return false;
		}
	}

	public void emailRecipe(Recipe recipe, Context context) {
		String emailBody = convertToEmail(recipe);
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL, email);
		i.putExtra(Intent.EXTRA_SUBJECT, "Recipe");
		i.putExtra(Intent.EXTRA_TEXT, emailBody);
		context.startActivity(Intent.createChooser(i, "Send mail..."));
	}

	
	private String convertToEmail(Recipe recipe) {
		String title = recipe.getTitle();
		String directions = recipe.getDirections();
		String description = recipe.getDescription();
		String combined = formCombinedString(recipe); 
		return "Recipe Title:\n" + title + "\n\nRecipe Description:\n"
				+ description + "\n\nIngredients:\n" + combined
				+ "\n\nDirections:\n" + directions;
	}

	
	private String formCombinedString(Recipe recipe) {
		ArrayList<String> ingredients = recipe.getIngredients();
		ArrayList<String> quantities = recipe.getQuantities();
		ArrayList<String> units = recipe.getUnits();
		String combined = new String();

		for (int index = 0; index < ingredients.size(); index++) {
			combined = combined + (quantities.get(index) + " " + units.get(index) + "  "
					+ ingredients.get(index));
		}
		return combined;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Pantry getPantry() {
		return pantry;
	}

	public void setPantry(Pantry pantry) {
		this.pantry = pantry;
	}

	public String getFilename() {
		return filename;
	}

}
