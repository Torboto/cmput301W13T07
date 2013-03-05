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

import android.content.Context;

/* 
 * ET: Wrote 
 */
@SuppressWarnings("serial")
public class User implements Serializable {
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

	/*
	 * ET: Sets name/email member variables for object, and then writes
	 * serilized form to file userdata in the application data folder.
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

	/*
	 * ET: Reads in User object from userdata file in application data folder
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

		this.email = user.email;
		this.name = user.name;
		this.pantry = user.pantry;

		try {
			fin.close();
			ois.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * ET: Checks to see if userdata exists in internal app data folder.
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

	/*
	 * Recipe will need a parser to compile it into a paintext format, this
	 * method should not parse anything, simply send an email. May want to
	 * change input to take in the paintext, and an email instead. Will require
	 * looking into JavaEmailAPI.
	 */
	public void emailRecipe(int recipeId, String email) {
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
