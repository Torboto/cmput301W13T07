package ca.ualberta.cs.team07recipefinder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;

@SuppressWarnings("serial")
public class User implements Serializable{
	private static User instance = null;
	private String name;
	private String email;
	private Pantry pantry;
	private String filename = "userdata";

	protected User() {
	}
	
	public static User getInstance() {
		if (instance == null) {
			instance = new User();
		}
		return instance;
	}

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

	public void emailRecipe(int recipeId) {

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
