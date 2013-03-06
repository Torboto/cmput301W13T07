package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

public class RecipeController {
	
	private RecipeController(){
	}

	public static void writeRecipe(Recipe recipe){
	}

    public void deleteRecipe(UUID uuid){
    }

    /*
     * Write over old recipe with the same UUID
     */
    public void updateRecipe(Recipe recipe){
    }
	
    public RecipeBook searchRecipeIngredients(ArrayList<String> ingredients){
        return null;
	}
    
    public RecipeBook searchRecipeTitles(String title){
        return null;
	}
}
