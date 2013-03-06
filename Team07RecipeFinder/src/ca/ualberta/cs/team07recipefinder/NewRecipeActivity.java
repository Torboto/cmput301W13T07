package ca.ualberta.cs.team07recipefinder;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_new_recipe2, menu);
		return true;
	}
	
	public void back(View v) {
		// AS: Go back to my recipes activity
		finish();
	}
	
	public void createRecipe(View v){
		
		EditText DescriptionEditText = getDescription();
		EditText IngredientsEditText = getIngredients();
		EditText DirectionsEditText = getDirections();
		if ((!isEmpty(DescriptionEditText)) && (!isEmpty(IngredientsEditText)) &&
				(!isEmpty(DirectionsEditText))){
			/*
			 *  AS: Now we know the required fields are filled in
			 *   before we proceed
			 */
			String Description = DescriptionEditText.toString();
			String Ingredients = IngredientsEditText.toString();
			String Directions = DirectionsEditText.toString();
			RecipeBookController RBController = new RecipeBookController();
			//RBController.creatRecipe();
		}
		
		// AS: If one or more fields empty could potentially have a 
		// dialog saying so?
	}
	private boolean isEmpty(EditText etText) {
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
		EditText DescriptionEditText = (EditText) findViewById(R.id.etDescription);
		return DescriptionEditText;
	}

	private EditText getIngredients(){
		// AS: returns the ingredients as an Edit Text
		EditText IngredientsEditText = (EditText) findViewById(R.id.etNewIngredients);
		return IngredientsEditText;
	}
	
	private EditText getDirections(){
		// AS: returns the new directions as an Edit Text
		EditText IngredientsEditText = (EditText) findViewById(R.id.etNewIngredients);
		return IngredientsEditText;
	}
	
	
}
