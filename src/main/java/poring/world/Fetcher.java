package poring.world;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import poring.world.market.filter.FilterUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static poring.world.Constants.Constants.*;

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
  public static JSONObject getCheapestPremiums() {
    Map<String, String> parameters = new HashMap<>(DEFAULT_PARAMETERS);
    parameters.put("order", "price");
    parameters.put("inStock", "1");
    parameters.put("category", "1052");

    try {
      Map<String, String> colorParameters = new HashMap<>(parameters);
      JSONArray jsonData = getJsonData(colorParameters, null);

      if (jsonData.size() > 1) {
        return (JSONObject) jsonData.get(0);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static JSONObject getCheapestCards(Set<String> colors) {
    Map<String, String> parameters = new HashMap<>(DEFAULT_PARAMETERS);
    parameters.put("order", "price");
    parameters.put("inStock", "1");
    parameters.put("category", "1010");
    parameters.put("endCategory", "1016");

    try {
      JSONObject returnJson = new JSONObject();
      Set<String> queryColors = new HashSet<>(colors);
      if (queryColors.isEmpty()) {
        queryColors.addAll(CARD_COLOR.values());
      }
      for (String color : queryColors) {
        Map<String, String> colorParameters = new HashMap<>(parameters);
        colorParameters.put("rarity", color);
        JSONArray jsonData = getJsonData(colorParameters, null);

        for (Object card : jsonData) {
          String snapKey = color + "snap";
          String noSnapKey = color + "nosnap";
          if (returnJson.containsKey(snapKey) && returnJson.containsKey(noSnapKey)) {
            break;
          }
          JSONObject jsonCard = (JSONObject) card;
          String snapEnd = ((JSONObject) jsonCard.get("lastRecord")).get("snapEnd").toString();
          if (snapEnd.equals("0")) {
            if (!returnJson.containsKey(noSnapKey)) {
              returnJson.put(noSnapKey, jsonCard);
            }
          } else {
            if (!returnJson.containsKey(snapKey)) {
              returnJson.put(snapKey, jsonCard);
            }
          }
        }
      }
      return returnJson;
    } catch (IOException e) {
      e.printStackTrace();
      return new JSONObject();
    }
  }

  private static JSONArray getJsonData(Map<String, String> param, Map<String, String> filters) throws IOException {
    BasicHeader h1 = new BasicHeader(HttpHeaders.USER_AGENT, Utils.getUserAgent());

    List<Header> headers = Lists.newArrayList(h1);
    try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultHeaders(headers).build()) {

      String parametersUrl = getParametersUrl(param);
      String fullUrl = BASE_URL + parametersUrl;
      HttpGet request = new HttpGet(fullUrl);
      HttpResponse response = client.execute(request);

      if (response.getStatusLine().getStatusCode() != 200) {
        System.out.println(String.format("Error %s\nURL %s", response.getStatusLine().getStatusCode(), fullUrl));
        return new JSONArray();
      }

      BufferedReader bufReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      StringBuilder builder = new StringBuilder();
      String line;

      while ((line = bufReader.readLine()) != null) {
        builder.append(line);
        builder.append(System.lineSeparator());
      }

      JSONParser parser = new JSONParser();
      JSONArray returnJson = new JSONArray();
      try {
        for (Object object : (JSONArray) parser.parse(builder.toString())) {
          JSONObject minimalJsonObject = retainDefaultKeys((JSONObject) object);
          if (isValid(minimalJsonObject) && !FilterUtils.filter(minimalJsonObject, filters)) {
            returnJson.add(minimalJsonObject);
          }
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
      Utils.waitSomeTime();
      return returnJson;
    }
  }

  public static JSONArray query(String search) {
    return query(search, null);
  }

  public static JSONArray query(String search, Map<String, String> filters) {
    String encodedSearch = null;
    try {
      encodedSearch = URLEncoder.encode(search, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return new JSONArray();
    }

    Map<String, String> parameters = new HashMap<>(DEFAULT_PARAMETERS);
    parameters.put("q", encodedSearch);
    if (filters.containsKey(CATEGORY)) {
      parameters.put(CATEGORY, CATEGORY_MAP.get(filters.get(CATEGORY)));
    }

    try {
      return getJsonData(parameters, filters);
    } catch (IOException e) {
      System.out.println("Error on parameters: " + parameters);
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

  private static boolean isValid(JSONObject jsonItem) {
    return isStillThere(jsonItem) && isPricePositive(jsonItem);
  }

  private static boolean isPricePositive(JSONObject jsonItem) {
    JSONObject lastRecord = (JSONObject) jsonItem.get("lastRecord");
    long price = Long.parseLong(lastRecord.get("price").toString());
    return price > 0;
  }

  private static boolean isStillThere(JSONObject jsonItem) {
    JSONObject lastRecord = (JSONObject) jsonItem.get("lastRecord");
    long snapEnd = Long.parseLong(lastRecord.get("snapEnd").toString()) * 1000;
    long buyers = Long.parseLong(lastRecord.get("snapBuyers").toString());
    return buyers == 0 || snapEnd == 0 || new Date().before(new Date(snapEnd));
  }

}
