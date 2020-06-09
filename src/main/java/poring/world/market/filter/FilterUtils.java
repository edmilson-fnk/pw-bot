package poring.world.market.filter;

import static poring.world.Constants.Constants.BROKEN;
import static poring.world.Constants.Constants.ENCHANT;
import static poring.world.Constants.Constants.EXCEPT;
import static poring.world.Constants.Constants.MAX_PRICE;
import static poring.world.Constants.Constants.FILTERS_NAME;
import static poring.world.Constants.Constants.NO;
import static poring.world.Constants.Constants.YES;

import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.util.Map;

public class FilterUtils {

  public static String translate(Map<String, String> filters) {
    if (filters == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (String filterKey : filters.keySet()) {
      sb.append(translate(filterKey, filters.get(filterKey)));
    }
    return sb.toString();
  }

  public static String translate(String key, String value) {
    String rKey = FILTERS_NAME.get(key.toLowerCase());
    String rValue = value;
    if (key.equalsIgnoreCase(MAX_PRICE)) {
      rValue = new DecimalFormat("###,###,###,###").format(Double.parseDouble(value));
    } else if (key.equalsIgnoreCase(BROKEN)) {
      rValue = value.toLowerCase();
    } else if (key.equalsIgnoreCase(ENCHANT)) {
      rValue = value.toLowerCase();
    } else if (key.equalsIgnoreCase(EXCEPT)) {
      rValue = value.toLowerCase();
    }
    return String.format("_%s_: %s; ", rKey, rValue);
  }

  public static String validate(String key, String value) {
    if (key.equalsIgnoreCase(MAX_PRICE)) {
      try {
        Integer.parseInt(value);
      } catch (Exception e) {
        return e.getMessage();
      }
    } else if (key.equalsIgnoreCase(BROKEN)) {
      return value.equalsIgnoreCase(YES) || value.equalsIgnoreCase(NO) ? null : value;
    } else if (key.equalsIgnoreCase(ENCHANT)) {
      return !value.isEmpty() ? null : "empty value";
    } else if (key.equalsIgnoreCase(EXCEPT)) {
      return !value.isEmpty() ? null : "empty value";
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
        if (((long) ((JSONObject) minimalJsonObject.get("lastRecord")).get("price")) > Long.parseLong(value)) {
          return true;
        }
      } else if (key.equalsIgnoreCase(BROKEN)) {
        if (value.equalsIgnoreCase(YES) && !minimalJsonObject.get("name").toString().contains("(broken)")
        || value.equalsIgnoreCase(NO) && minimalJsonObject.get("name").toString().contains("(broken)")) {
          return true;
        }
      } else if (key.equalsIgnoreCase(ENCHANT)) {
        String name = minimalJsonObject.get("name").toString().toLowerCase();
        return !name.matches(String.format(".*<.*%s.*>.*", value.toLowerCase()));
      } else if (key.equalsIgnoreCase(EXCEPT)) {
        String name = minimalJsonObject.get("name").toString().toLowerCase();
        return name.contains(value.toLowerCase());
      }
    }
    return false;
  }

}
