package poring.world.market.filter;

import static poring.world.Constants.MAX_PRICE;
import static poring.world.Constants.FILTERS_NAME;

import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.util.Map;

public class FilterUtils {

  public static String translate(String key, String value) {
    String rKey = FILTERS_NAME.get(key);
    String rValue = value;
    if (key.equalsIgnoreCase(MAX_PRICE)) {
      rValue = new DecimalFormat("###,###,###,###").format(Double.parseDouble(value));
    }
    return String.format("%s: %s; ", rKey, rValue);
  }

  public static String validate(String key, String value) {
    if (key.equalsIgnoreCase(MAX_PRICE)) {
      try {
        Integer.parseInt(value);
      } catch (Exception e) {
        return e.getMessage();
      }
    }
    return null;
  }

  public static boolean filter(JSONObject minimalJsonObject, Map<String, String> filters) {
    if (filters == null || filters.isEmpty()) {
      return false;
    }

    for (String key : filters.keySet()) {
      String value = filters.get(key);
      if (key.equalsIgnoreCase(MAX_PRICE)) {
        return ((long) ((JSONObject) minimalJsonObject.get("lastRecord")).get("price")) > Long.parseLong(value);
      }
    }
    return false;
  }
}
