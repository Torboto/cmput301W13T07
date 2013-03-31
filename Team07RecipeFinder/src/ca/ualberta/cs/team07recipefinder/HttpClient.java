package ca.ualberta.cs.team07recipefinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.os.NetworkOnMainThreadException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author Torboto Methods for saving recipes, deleting recipes, and searching
 *         for recipes from webservice:
 *         http://cmput301.softwareprocess.es:8080/CMPUT301W13T07/
 */
public class HttpClient {

	private DefaultHttpClient httpClient = new DefaultHttpClient();
	private Gson gson = new Gson();
	private String recipeUrl = "http://cmput301.softwareprocess.es:8080/cmput301w13t07/recipes/";
	private String imageUrl = "http://cmput301.softwareprocess.es:8080/cmput301w13t07/images/";

	/**
	 * Writes recipe object to elastic search on the internet.
	 * 
	 * @param recipe
	 */
	public void writeRecipe(Recipe recipe) {
		recipe.location = Recipe.Location.SERVER;
		HttpPost httpPost = new HttpPost(recipeUrl + recipe.getRecipeId());
		StringEntity stringEntity = null;

		try {
			stringEntity = new StringEntity(gson.toJson(recipe));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		httpPost.setHeader("Accept", "application/json");
		httpPost.setEntity(stringEntity);
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NetworkOnMainThreadException e) {
			// e.printStackTrace();
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
			e.printStackTrace();
		}
		try {
			// ET: Think this is right, EntityUtils is from more recent version
			// of library that isn't in default Android package.
			entity.consumeContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads in the identified recipe by it's given id.
	 * 
	 * @param uuid
	 *            identifying id for recipe to be found
	 * @return recipe object
	 */
	public Recipe readRecipe(UUID uuid) {
		HttpGet httpGet = new HttpGet(recipeUrl + uuid + "?pretty=1");
		// HttpGet httpPost = new
		// HttpGet("http://cmput301.softwareprocess.es:8080/testing/lab02/999?pretty=1");//S4bRPFsuSwKUDSJImbCE2g?pretty=1

		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String status = response.getStatusLine().toString();
		System.out.println(status);

		String json = "";
		try {
			json = getEntityContent(response);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// We have to tell GSON what type we expect
		Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<Recipe>>() {
		}.getType();
		// Now we expect to get a Recipe response
		ElasticSearchResponse<Recipe> esResponse = gson.fromJson(json,
				elasticSearchResponseType);
		// We get the recipe from it!
		if (esResponse != null) {
			Recipe recipe = esResponse.getSource();
			System.out.println(recipe.toString());
			// TODO consume?

			return recipe;
		}
		return null;
	}

	public void deleteRecipe(UUID uuid) {
		HttpDelete httpDelete = new HttpDelete(recipeUrl + uuid);
		httpDelete.addHeader("Accept", "application/json");

		HttpResponse response = null;
		try {
			response = httpClient.execute(httpDelete);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String status = response.getStatusLine().toString();
		System.out.println(status);

		HttpEntity entity = response.getEntity();

		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(entity.getContent()));
			String output;
			System.err.println("Output from Server -> ");
			while ((output = br.readLine()) != null) {
				System.err.println(output);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// TODO: Consume?

	}

	public void updateRecipe(Recipe recipe) {
		DeleteRecipeTask deleteTask = new DeleteRecipeTask();
		deleteTask.execute(recipe.recipeId);
		WriteRecipeTask writeTask = new WriteRecipeTask();
		writeTask.execute(recipe);
	}

	/**
	 * Searches for recipes containing any of the ingredients given.
	 * 
	 * @param ingredients
	 *            list of ingredients
	 * @return list of matching recipes
	 */
	public ArrayList<Recipe> searchRecipes(List<String> ingredients) {
		HttpPost httpPost = new HttpPost(recipeUrl + "_search?pretty=1");
		StringEntity stringEntity;
		ArrayList<Recipe> recipeResults = new ArrayList<Recipe>();
		HttpResponse response = null;
		HttpEntity entity = null;
		BufferedReader buff = null;
		String output = null;

		String queryString = "";
		if (ingredients.size() > 1) {
			for (String s : ingredients) {
				queryString += s;
				if (s != ingredients.get(ingredients.size() - 1).toString()) {
					queryString += " OR ";
				}
			}
		} else {
			queryString = ingredients.get(0);
		}

		String query = "{\"query\" : " + "{\"query_string\" : " + "{"
				+ "\"default_field\" : \"ingredients\"," + "\"query\" : \""
				+ queryString + "\"}}}";

		try {
			stringEntity = new StringEntity(query);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setEntity(stringEntity);
			response = httpClient.execute(httpPost);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String status = response.getStatusLine().toString();
		System.out.println(status);

		String json = null;
		try {
			json = getEntityContent(response);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<Recipe>>() {
		}.getType();
		ElasticSearchSearchResponse<Recipe> esResponse = gson.fromJson(json,
				elasticSearchSearchResponseType);
		System.err.println(esResponse);

		for (ElasticSearchResponse<Recipe> esrt : esResponse.getHits()) {
			Recipe recipe = esrt.getSource();
			recipeResults.add(recipe);
		}

		// TODO Release connection? Consume?
		return recipeResults;
	}

	/**
	 * Searches for recipes based on titles.
	 * 
	 * @param name 
	 * title of recipe to search
	 * @return Returns all recipes that match the keyword given
	 */
	public ArrayList<Recipe> searchRecipes(String name) {
		ArrayList<Recipe> recipeResults = new ArrayList<Recipe>();
		HttpGet searchRequest = null;
		HttpResponse response = null;

		try {
			searchRequest = new HttpGet(recipeUrl + "_search?pretty=1&q="
					+ java.net.URLEncoder.encode(name, "UTF-8"));
			searchRequest.setHeader("Accept", "application/json");
			response = httpClient.execute(searchRequest);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String status = response.getStatusLine().toString();
		System.out.print(status);

		String json = null;
		try {
			json = getEntityContent(response);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<Recipe>>() {
		}.getType();
		ElasticSearchSearchResponse<Recipe> esResponse = gson.fromJson(json,
				elasticSearchSearchResponseType);
		System.err.println(esResponse);

		for (ElasticSearchResponse<Recipe> esrt : esResponse.getHits()) {
			Recipe recipe = esrt.getSource();
			recipeResults.add(recipe);
		}

		return recipeResults;
	}

	public void writeImage(UUID uuid, Bitmap bitmap) {
		HttpPost httpPost = new HttpPost(imageUrl + uuid);
		StringEntity stringEntity = null;

		httpPost.setHeader("Accept", "application/json");
		httpPost.setEntity(stringEntity);
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NetworkOnMainThreadException e) {
			// e.printStackTrace();
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
			e.printStackTrace();
		}
		try {
			// ET: Think this is right, EntityUtils is from more recent version
			// of library that isn't in default Android package.
			entity.consumeContent();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Bitmap readImage(UUID uuid) {
		return null;
	}

	public Bitmap[] readAllImages(UUID uuid) {
		return null;
	}

	/**
	 * Get the response from elastic search, and return json string.
	 * 
	 * @param response
	 * @return json response string
	 * @throws IOException
	 */
	String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(response.getEntity().getContent())));
		String output;
		System.err.println("Output from Server -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.err.println("JSON:" + json);
		return json;
	}

}
