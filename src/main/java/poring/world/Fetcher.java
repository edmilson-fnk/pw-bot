package poring.world;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Fetcher {

  public static String BASE_URL = "https://poring.world/api/search?";
  public static Map<String, String> DEFAULT_PARAMETERS = ImmutableMap.of(
      "order", "popularity",
      "category", "",
      "inStock", "1",
      "rarity", "",
      "modified", ""
  );
  public static Map<String, Set<String>> DEFAULT_RETURN_MAP = ImmutableMap.of(
      "name", Collections.emptySet(),
      "lastRecord", ImmutableSet.of("price", "snapBuyers", "snapEnd", "stock", "timestamp")
  );

  public static JSONArray query(String search) {
    String encodedSearch = null;
    try {
      encodedSearch = URLEncoder.encode(search, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return new JSONArray();
    }

    Map<String, String> parameters = new HashMap<>(DEFAULT_PARAMETERS);
    parameters.put("q", encodedSearch);

    String parametersUrl = getParametersUrl(parameters);

    try {
      try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
        HttpGet request = new HttpGet(BASE_URL + parametersUrl);
        HttpResponse response = client.execute(request);

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder builder = new StringBuilder();
        String line;

        while ((line = bufReader.readLine()) != null) {
          builder.append(line);
          builder.append(System.lineSeparator());
        }

        try {
          JSONParser parser = new JSONParser();
          JSONArray returnJson = new JSONArray();
          for (Object object : (JSONArray) parser.parse(builder.toString())) {
            JSONObject minimalJsonObject = retainDefaultKeys((JSONObject) object);
            returnJson.add(minimalJsonObject);
          }

          return returnJson;
        } catch (ParseException e) {
          e.printStackTrace();
          return new JSONArray();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      return new JSONArray();
    }
  }

  private static JSONObject retainDefaultKeys(JSONObject jsonObject) {
    JSONObject newJsonObject = new JSONObject();
    for (String key : DEFAULT_RETURN_MAP.keySet()) {
      if (DEFAULT_RETURN_MAP.get(key).isEmpty()) {
        newJsonObject.put(key, jsonObject.get(key));
      } else {
        if (!newJsonObject.containsKey(key)) {
          newJsonObject.put(key, new JSONObject());
        }
        for (String subKey : DEFAULT_RETURN_MAP.get(key)) {
          ((JSONObject) newJsonObject.get(key)).put(subKey, ((JSONObject) jsonObject.get(key)).get(subKey));
        }
      }
    }
    return newJsonObject;
  }

  private static String getParametersUrl(Map<String, String> parameters) {
    StringBuilder parametersUrl = new StringBuilder();
    for (Map.Entry<String, String> entry : parameters.entrySet()) {
      parametersUrl.append(entry.getKey());
      parametersUrl.append("=");
      parametersUrl.append(entry.getValue());
      parametersUrl.append("&");
    }
    return parametersUrl.toString();
  }

}
