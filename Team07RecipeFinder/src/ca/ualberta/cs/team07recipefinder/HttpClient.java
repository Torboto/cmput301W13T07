package ca.ualberta.cs.team07recipefinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.NetworkOnMainThreadException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/*
 * GC:
 * methods for saving recipes, deleting recipes, and searching for recipes from
 * webservice: http://cmput301.softwareprocess.es:8080/CMPUT301W13T07/
 */
public class HttpClient {

	private DefaultHttpClient httpClient = new DefaultHttpClient();
	private Gson gson = new Gson();
	private String url = "http://cmput301.softwareprocess.es:8080/cmput301w13t07/recipes/";

	public void writeRecipe(Recipe recipe) {
		HttpPost httpPost = new HttpPost(url + recipe.getRecipeId());
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
		} catch (NetworkOnMainThreadException e) {
			// TODO
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

	public ArrayList<Recipe> searchRecipes(ArrayList<String> ingredients) {
		HttpPost httpPost = new HttpPost(url + "_search?pretty=1");
		StringEntity stringEntity;

		String queryString = "";
		for (String s : ingredients) {
			queryString += s + " OR ";
		}
		String query = "{\"query\" : " + "{\"query_string\" : " + "{"
				+ "\"default_field\" : \"ingredients\"," + "\"query\" : \""
				+ queryString + "\"}}}";
		try {
			stringEntity = new StringEntity(query);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setEntity(stringEntity);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpResponse response;
		HttpEntity entity;
		try {
			response = httpClient.execute(httpPost);
			String status = response.getStatusLine().toString();
			System.out.println(status);

			entity = response.getEntity();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// InputStream json = response.getEntity().getContent();
		//
		// String json = httpClient.execute(reponse,)
		//
		// java.lang.reflect.Type elasticSearchResponseType = new
		// TypeToken<ElasticSearchResponse<Recipe>>() {
		// }.getType();
		// ElasticSearchResponse<Recipe> esResponse = gson.fromJson(json,
		// elasticSearchResponseType);
		// System.err.println(esResponse);
		// for (ElasticSearchResponse<Recipe> r : esResponse.getHits()) {
		// Recipe recipe = r.getSource();
		// System.err.println(recipe);
		// }
		// TODO Release connection? Consume?
		return null;
	}

	public ArrayList<Recipe> searchRecipes(String name) {
		ArrayList<Recipe> recipeResults;
		HttpGet searchRequest;
		try {
			searchRequest = new HttpGet(url + "_search?pretty=1&q="
					+ java.net.URLEncoder.encode(name, "UTF-8"));
			searchRequest.setHeader("Accept", "application/json");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		HttpResponse response;
		try {
			response = httpClient.execute(searchRequest);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String status = response.getStatusLine().toString();
		System.out.print(status);

		HttpEntity entity = response.getEntity();
		BufferedReader buff;

		buff = new BufferedReader(new InputStreamReader(entity.getContent()));
		String output;
		System.err.println("Output from Server -> ");

		while ((output = buff.readLine()) != null) {
			System.err.println(output);
		}
		
		//If there are errors here about the type, that is an Eclipse bug. They are not actually errors.
		//To get code to compile, go to the Problems tab, and delete the errors involved ElasticSearchResponse not being a valid type.
		java.lang.reflect.Type elasticSearchResponseType = new TypeToken<ElasticSearchRepsonse<Recipe>>(){}.getType();
		ElasticSearchResponse<Recipe> esResponse = gson.fromJson(output, elasticSearchResponseType);
		System.err.println(esResponse);
		for (ElasticSearchResponse<Recipe> r : esResponse.getHits()) {
			Recipe recipe = r.getSource();
			recipeResults.add(recipe);
			System.err.println(recipe);
		}

		return recipeResults;
	}

}
