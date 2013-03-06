package ca.ualberta.cs.team07recipefinder;

import java.util.UUID;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;

public class NewRecipeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_recipe);
	}

	
	public void back(View v) {
		// AS: Go back to my recipes activity
		finish();
	}
	
	public void createRecipe(View v){
		
		EditText DescriptionEditText = getDescription();
		EditText IngredientsEditText = getIngredients();
		EditText DirectionsEditText = getDirections();
		if ((!IsEmpty(DescriptionEditText)) && (!IsEmpty(IngredientsEditText)) &&
				(!IsEmpty(DirectionsEditText))){
			/*
			 *  AS: Now we know the required fields are filled in
			 *   before we proceed
			 */
			String Title = "Need to Add This";
			String Description = DescriptionEditText.toString();
			String Ingredients = IngredientsEditText.toString();
			String Directions = DirectionsEditText.toString();
			UUID recipeID = getID();
			String email = getEmail();
			
		//	Recipe recipe = new Recipe();
		}
		
		// AS: If one or more fields empty could potentially have a 
		// dialog saying so?
	}
	private boolean IsEmpty(EditText etText) {
		// AS: returns if an Edit Text is empty or not
		if(etText.getText().toString().trim().length() > 0)
		    return false;
		 else
		   return true; 
	}
	
	//TODO: figure out title
	//private EditText getTitle(){
		// AS: returns the new directions as an Edit Text
	//	EditText TitleEditText = (EditText) findViewById(R.id.etTitle);
	//	return TitleEditText;
	//}
	
	private EditText getDescription(){
		// AS: returns the description as an Edit Text
		EditText descriptionEditText = (EditText) findViewById(R.id.etDescription);
		return descriptionEditText;
	}

	private EditText getIngredients(){
		// AS: returns the ingredients as an Edit Text
		EditText ingredientsEditText = (EditText) findViewById(R.id.etNewIngredients);
		return ingredientsEditText;
	}
	
	private EditText getDirections(){
		// AS: returns the new directions as an Edit Text
		EditText IngredientsEditText = (EditText) findViewById(R.id.etNewIngredients);
		return IngredientsEditText;
	}
	
	private UUID getID(){
		UUID id = UUID.randomUUID();
		return id;
	}
	private String getEmail(){
		User theUser = User.getInstance();
		String email = theUser.getEmail();
		return email;
	}
	
	
}
