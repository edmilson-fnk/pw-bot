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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
      rValue = value.replaceAll("\\s*&&\\s*", " or ").toLowerCase();
    } else if (key.equalsIgnoreCase(EXCEPT)) {
      rValue = value.replaceAll("\\s*&&\\s*", " and ").toLowerCase();
    }
    return String.format("_%s_: %s; ", rKey, rValue);
  }

  public static String validate(String key, String value) {
    if (value.isEmpty()) {
      return "empty value";
    }

    // Unique values keys
    if (key.equalsIgnoreCase(MAX_PRICE)) {
      try {
        if (value.contains("&&")) {
          return "not a multiple values field, remove _&&_";
        }
        Integer.parseInt(value);
        return null;
      } catch (Exception e) {
        return e.getMessage();
      }
    } else if (key.equalsIgnoreCase(BROKEN)) {
      if (value.contains("&&")) {
        return "not a multiple values field, remove _&&_";
      }
      if (!value.equalsIgnoreCase(YES) && !value.equalsIgnoreCase(NO)) {
        return "use only _yes_ or _no_";
      }
    }

      // Multiple values keys
    List<String> values = Arrays.stream(value.split("&&")).map(String::trim).collect(Collectors.toList());
    for (String v : values) {
      if (key.equalsIgnoreCase(ENCHANT)) {
        // No validation yet
        return null;
      } else if (key.equalsIgnoreCase(EXCEPT)) {
        // No validation yet
        return null;
      }
    }
    return null;
  }

  public static boolean filter(JSONObject minimalJsonObject, Map<String, String> filters) {
    if (filters == null || filters.isEmpty()) {
      return false;
    }

    boolean outerFilter = false;
    for (String key : filters.keySet()) {
      boolean innerFilter = false;
      for (String value : filters.get(key).split("&&")) {
        value = value.trim();
        if (key.equalsIgnoreCase(MAX_PRICE)) {
          if (((long) ((JSONObject) minimalJsonObject.get("lastRecord")).get("price")) > Long.parseLong(value)) {
            innerFilter = true;
          }
        } else if (key.equalsIgnoreCase(BROKEN)) {
          if (value.equalsIgnoreCase(YES) && !minimalJsonObject.get("name").toString().contains("(broken)")
              || value.equalsIgnoreCase(NO) && minimalJsonObject.get("name").toString().contains("(broken)")) {
            innerFilter = true;
          }
        } else if (key.equalsIgnoreCase(ENCHANT)) {
          String name = minimalJsonObject.get("name").toString().toLowerCase();
          innerFilter = !name.matches(String.format(".*<.*%s.*>.*", value.toLowerCase()));
          if (!innerFilter) {
            break;
          }
        } else if (key.equalsIgnoreCase(EXCEPT)) {
          String name = minimalJsonObject.get("name").toString().toLowerCase();
          innerFilter = name.contains(value.toLowerCase());
          if (innerFilter) {
            break;
          }
        }
      }
      if (innerFilter) {
        outerFilter = true;
        break;
      }
    }
    return outerFilter;
  }

}
