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
import poring.world.cache.Cache;
import poring.world.market.filter.FilterUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static poring.world.constants.Constants.*;

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

  Cache cache = new Cache();

  public JSONObject getCheapestPremiums() {
    Map<String, String> parameters = new HashMap<>(DEFAULT_PARAMETERS);
    parameters.put("order", "price");
    parameters.put("inStock", "1");
    parameters.put("category", "1052");

    try {
      Map<String, String> colorParameters = new TreeMap<>(parameters);
      JSONArray jsonData = getJsonData(colorParameters, null);

      if (jsonData.size() > 1) {
        return (JSONObject) jsonData.get(0);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public JSONObject getCheapestCards(Set<String> colors) {
    int maxCards = 3;
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
        JSONArray snapCards = new JSONArray();
        JSONArray noSnapCards = new JSONArray();


        Map<String, String> colorParameters = new TreeMap<>(parameters);
        colorParameters.put("rarity", color);
        JSONArray jsonData = getJsonData(colorParameters, null);

        for (Object card : jsonData) {
          JSONObject jsonCard = (JSONObject) card;
          String snapEnd = ((JSONObject) jsonCard.get("lastRecord")).get("snapEnd").toString();
          if (snapEnd.equals("0")) {
            if (noSnapCards.size() < maxCards) {
              noSnapCards.add(jsonCard);
            }
          } else {
            if (snapCards.size() < maxCards) {
              snapCards.add(jsonCard);
            }
          }
        }

        String snapKey = color + "snap";
        returnJson.put(snapKey, snapCards);
        String noSnapKey = color + "nosnap";
        returnJson.put(noSnapKey, noSnapCards);
      }

      return returnJson;
    } catch (IOException e) {
      e.printStackTrace();
      return new JSONObject();
    }
  }

  private JSONArray getJsonData(Map<String, String> param, Map<String, String> filters) throws IOException {
    BasicHeader h1 = new BasicHeader(HttpHeaders.USER_AGENT, Utils.getUserAgent());

    List<Header> headers = Lists.newArrayList(h1);
    try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultHeaders(headers).build()) {

      String parametersUrl = getParametersUrl(param);
      String fullUrl = BASE_URL + parametersUrl;
      String jsonStr;
      if (this.cache.containsKey(fullUrl)) {
        jsonStr = this.cache.get(fullUrl);
      } else {
        HttpGet request = new HttpGet(fullUrl);
        HttpResponse response = client.execute(request);

        if (response.getStatusLine().getStatusCode() != 200) {
          System.out.printf("Error %s\nURL %s%n", response.getStatusLine().getStatusCode(), fullUrl);
          return new JSONArray();
        }

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder builder = new StringBuilder();
        String line;

        while ((line = bufReader.readLine()) != null) {
          builder.append(line);
          builder.append(System.lineSeparator());
        }
        jsonStr = builder.toString();
        this.cache.put(fullUrl, jsonStr);
        Utils.waitSomeTime();
      }

      JSONParser parser = new JSONParser();
      JSONArray returnJson = new JSONArray();
      try {
        for (Object object : (JSONArray) parser.parse(jsonStr)) {
          JSONObject minimalJsonObject = retainDefaultKeys((JSONObject) object);
          if (isValid(minimalJsonObject) && !FilterUtils.filter(minimalJsonObject, filters)) {
            returnJson.add(minimalJsonObject);
          }
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return returnJson;
    }
  }

  public JSONArray query(String search) {
    return query(search, null);
  }

  public JSONArray query(String search, Map<String, String> filters) {
    String encodedSearch = null;
    try {
      encodedSearch = URLEncoder.encode(search, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return new JSONArray();
    }

    Map<String, String> parameters = new TreeMap<>(DEFAULT_PARAMETERS);
    parameters.put("q", encodedSearch);
    Map<String, String> filtersCase = filters == null ? new HashMap<>() :
            filters.keySet().stream().collect(Collectors.toMap(String::toLowerCase, filters::get));
    if (filtersCase.containsKey(CATEGORY)) {
      String categoryName = filtersCase.get(CATEGORY).toLowerCase();
      parameters.put(CATEGORY, CATEGORY_MAP.get(categoryName));

      // Add end category if it's a range
      if (END_CATEGORY_MAP.containsKey(categoryName)) {
        parameters.put(END_CATEGORY, END_CATEGORY_MAP.get(categoryName));
      }
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
      parametersUrl.append(entry.getValue().toLowerCase());
      parametersUrl.append("&");
    }
    return parametersUrl.toString();
  }

  public static int getResponseCode() {
    BasicHeader h1 = new BasicHeader(HttpHeaders.USER_AGENT, Utils.getUserAgent());

    List<Header> headers = Lists.newArrayList(h1);

    try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultHeaders(headers).build()) {
      String parametersUrl = getParametersUrl(new HashMap<>(DEFAULT_PARAMETERS));
      String fullUrl = BASE_URL + parametersUrl;
      HttpGet request = new HttpGet(fullUrl);
      HttpResponse response = client.execute(request);

      return response.getStatusLine().getStatusCode();
    } catch (IOException e) {
      return 1;
    }
  }

  private static boolean isValid(JSONObject jsonItem) {
    return isStillThere(jsonItem) && isPricePositive(jsonItem) && isNotBCCStuff(jsonItem);
  }

  private static boolean isNotBCCStuff(JSONObject jsonItem) {
    String name = jsonItem.get("name").toString().toLowerCase();
    return !(name.contains("{") && name.contains("}"));
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

  public void resetCache() {
    this.cache.clear();
  }
}
