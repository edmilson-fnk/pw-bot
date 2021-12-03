package poring.world.market.filter;

import org.json.simple.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static poring.world.constants.Constants.*;

public class FilterUtils {

  public static String translate(Map<String, String> filters) {
    if (filters == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (String filterKey : filters.keySet()) {
      sb.append(translateItem(filterKey, filters.get(filterKey)));
    }
    return sb.toString();
  }

  public static String translateItem(String key, String value) {
    BaseFilter keyFilter = FILTER_CLASSES.get(key.toLowerCase());

    return String.format("_%s_: %s; ", keyFilter.getName(), keyFilter.translate(value));
  }

  // Returns NULL if it's a valid key-value pair
  public static String validate(String key, String value) {
    if (value.isEmpty()) {
      return "empty value";
    }

    return FILTER_CLASSES.get(key.toLowerCase()).validate(value);
  }

  // Returns true it this item needs to be removed and returns false otherwise
  public static boolean filter(JSONObject minObj, Map<String, String> filters) {
    if (filters == null || filters.isEmpty()) {
      return false;
    }

    for (String key : filters.keySet()) {
      for (String value : filters.get(key).split(QUERY_SPLIT_TOKEN)) {
        BaseFilter keyFilter = FILTER_CLASSES.get(key.toLowerCase());
        if (keyFilter.filter(minObj, value.trim())) {
          return true;
        }
      }
    }
    return false;
  }

  public static Integer getRefineValue(String name) {
    String strPattern = "\\+?([0-9]*) .*";
    Pattern p = Pattern.compile(strPattern, Pattern.CASE_INSENSITIVE);
    Matcher matcher = p.matcher(name);
    if (matcher.matches()) {
      String refineStr = matcher.group(1);
      return Integer.parseInt(refineStr);
    }
    return 0;
  }

  public static Integer getNumSlots(String name) {
    String strPattern = ".*\\[([0-9]*)].*";
    Pattern p = Pattern.compile(strPattern, Pattern.CASE_INSENSITIVE);
    Matcher matcher = p.matcher(name);
    if (matcher.matches()) {
      String refineStr = matcher.group(1);
      return Integer.parseInt(refineStr);
    }
    return null;
  }

  public static String validateNumeric(String value) {
    try {
      Integer.parseInt(value);
      return null;
    } catch (Exception e) {
      return "use only numbers";
    }
  }

}
