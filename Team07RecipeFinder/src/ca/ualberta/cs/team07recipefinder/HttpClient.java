package ca.ualberta.cs.team07recipefinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.NetworkOnMainThreadException;

import com.google.gson.Gson;

/*
 * GC:
 * methods for saving recipes, deleting recipes, and searching for recipes from
 * webservice: http://cmput301.softwareprocess.es:8080/CMPUT301W13T07/
 */
public class HttpClient {

	private DefaultHttpClient httpClient = new DefaultHttpClient();
	private Gson gson = new Gson();

	public void writeRecipe(Recipe recipe) {
		HttpPost httpPost = new HttpPost(
				"http://cmput301.softwareprocess.es:8080/cmput301w13t07/recipes/"
						+recipe.getRecipeId());
		StringEntity stringEntity = null;

		try {
			stringEntity = new StringEntity(gson.toJson(recipe));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		httpPost.setHeader("Accept", "application/json");
		httpPost.setEntity(stringEntity);
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetworkOnMainThreadException e){
			// TODO
			//e.printStackTrace();
		}

		String status = response.getStatusLine().toString();
		System.out.println(status);

		HttpEntity entity = response.getEntity();
		BufferedReader buff;
		
		try {
			buff = new BufferedReader(
					new InputStreamReader(entity.getContent()));
			String output;
			System.err.println("Output from Server -> ");
			
			while ((output = buff.readLine()) != null) {
				System.err.println(output);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			// ET: Think this is right, EntityUtils is from more recent version
			// of library that isn't in default Android package.
			entity.consumeContent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Recipe readRecipe(UUID uuid) {
		return null;
	}

	public void updateRecipe() {

	}

	public RecipeBook searchRecipes(ArrayList<String> ingredients) {
		return null;
	}

	public RecipeBook searchRecipes(String title) {
		return null;
	}

}
