package ca.ualberta.cs.team07recipefinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

	public ArrayList<Recipe> searchRecipes(List<String> ingredients) {
		HttpPost httpPost = new HttpPost(url + "_search?pretty=1");
		StringEntity stringEntity;
		ArrayList<Recipe> recipeResults = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		BufferedReader buff = null;
		String output = null;

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
			response = httpClient.execute(httpPost);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String status = response.getStatusLine().toString();
		System.out.println(status);

		String json = null;
		try {
			json = getEntityContent(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<Recipe>>() {
		}.getType();
		ElasticSearchSearchResponse<Recipe> esResponse = gson.fromJson(
				json, elasticSearchSearchResponseType);
		System.err.println(esResponse);
		for (ElasticSearchResponse<Recipe> esrt : esResponse.getHits()) {
			Recipe recipe = esrt.getSource();
			recipeResults.add(recipe);
		}
		
		// TODO Release connection? Consume?
		return recipeResults;
	}

	public ArrayList<Recipe> searchRecipes(String name) {
		ArrayList<Recipe> recipeResults = new ArrayList<Recipe>();
		HttpGet searchRequest = null;
		BufferedReader buff = null;
		String output = null;
		HttpResponse response = null;

		try {
			searchRequest = new HttpGet(url + "_search?pretty=1&q="
					+ java.net.URLEncoder.encode(name, "UTF-8"));
			searchRequest.setHeader("Accept", "application/json");
			response = httpClient.execute(searchRequest);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String status = response.getStatusLine().toString();
		System.out.print(status);

		String json = null;
		try {
			json = getEntityContent(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<Recipe>>() {
		}.getType();
		ElasticSearchSearchResponse<Recipe> esResponse = gson.fromJson(
				json, elasticSearchSearchResponseType);
		System.err.println(esResponse);

		for (ElasticSearchResponse<Recipe> esrt : esResponse.getHits()) {
			Recipe recipe = esrt.getSource();
			recipeResults.add(recipe);
		}

		return recipeResults;
	}
	
	/**
	 * get the http response and return json string
	 */
	String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent())));
		String output;
		System.err.println("Output from Server -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.err.println("JSON:"+json);
		return json;
	}

}
