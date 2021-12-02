package poring.world.market.filter;

import org.json.simple.JSONObject;

import java.text.DecimalFormat;
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
      sb.append(translate(filterKey, filters.get(filterKey)));
    }
    return sb.toString();
  }

  public static String translate(String key, String value) {
    BaseFilter keyFilter = FILTER_CLASSES.get(key.toLowerCase());

    return String.format("_%s_: %s; ", keyFilter.getName(), keyFilter.translate(value));
  }

  // Returns NULL if it's a valid key-value pair
  public static String validate(String key, String value) {
    if (value.isEmpty()) {
      return "empty value";
    }

    BaseFilter keyFilter = FILTER_CLASSES.get(key.toLowerCase());
    return keyFilter.validate(value);
  }


  public static boolean filter(JSONObject minObj, Map<String, String> filters) {
    if (filters == null || filters.isEmpty()) {
      return false;
    }

    boolean outerFilter = false;
    for (String key : filters.keySet()) {
      boolean innerFilter = false;
      for (String value : filters.get(key).split(QUERY_SPLIT_TOKEN)) {
        value = value.trim();
        if (key.equalsIgnoreCase(MAX_PRICE)) {
          if (((long) ((JSONObject) minObj.get("lastRecord")).get("price")) > Long.parseLong(value)) {
            innerFilter = true;
          }
        } else if (key.equalsIgnoreCase(BROKEN)) {
          if (value.equalsIgnoreCase(YES) && !minObj.get("name").toString().contains("(broken)")
              || value.equalsIgnoreCase(NO) && minObj.get("name").toString().contains("(broken)")) {
            innerFilter = true;
          }
        } else if (key.equalsIgnoreCase(ENCHANT)) {
          String name = minObj.get("name").toString().toLowerCase();
          innerFilter = !name.matches(String.format(".*<.*%s.*>.*", value.toLowerCase()));
          if (!innerFilter) {
            break;
          }
        } else if (key.equalsIgnoreCase(EXCEPT)) {
          String name = minObj.get("name").toString().toLowerCase();
          innerFilter = name.contains(value.toLowerCase());
          if (innerFilter) {
            break;
          }
        } else if (key.equalsIgnoreCase(REFINE_GT)) {
          Integer refineItem = getRefineValue(minObj.get("name").toString().toLowerCase());
          if (refineItem != null) {
            int refineFilter = Integer.parseInt(value);
            if (refineItem < refineFilter) {
              innerFilter = true;
            }
          } else {
            innerFilter = true;
          }

          if (innerFilter) {
            break;
          }
        } else if (key.equalsIgnoreCase(REFINE_LT)) {
          Integer refineItem = getRefineValue(minObj.get("name").toString().toLowerCase());
          if (refineItem != null) {
            int refineFilter = Integer.parseInt(value);
            if (refineItem > refineFilter) {
              innerFilter = true;
            }
          } else {
            innerFilter = false;
          }

          if (innerFilter) {
            break;
          }
        } else if (key.equalsIgnoreCase(NUM_SLOTS)) {
          Integer numSlots = getNumSlots(minObj.get("name").toString().toLowerCase());
          int expectedSlots = Integer.parseInt(value);
          if (expectedSlots == 0) {
            innerFilter = numSlots != null;
          } else {
            innerFilter = numSlots == null || numSlots != expectedSlots;
          }

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

  public static Integer getRefineValue(String name) {
    String strPattern = "\\+?([0-9]*) .*";
    Pattern p = Pattern.compile(strPattern);
    Matcher matcher = p.matcher(name);
    if (matcher.matches()) {
      String refineStr = matcher.group(1);
      return Integer.parseInt(refineStr);
    }
    return null;
  }

  public static Integer getNumSlots(String name) {
    String strPattern = ".*\\[([0-9]*)].*";
    Pattern p = Pattern.compile(strPattern);
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
