package poring.world.market.filter;

import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static poring.world.Constants.Constants.*;

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
    if (key.equalsIgnoreCase(BROKEN)) {
      if (value.contains("&&")) {
        return "not a multiple values field, remove _&&_";
      }
      if (!value.equalsIgnoreCase(YES) && !value.equalsIgnoreCase(NO)) {
        return "use only _yes_ or _no_";
      }
      return null;
    } else if (
        key.equalsIgnoreCase(MAX_PRICE)
        || key.equalsIgnoreCase(REFINE_GT)
        || key.equalsIgnoreCase(REFINE_LT)
    ) {
      try {
        Integer.parseInt(value);
        return null;
      } catch (Exception e) {
        return "use only numbers";
      }
    } else if (key.equalsIgnoreCase(NUM_SLOTS)) {
      try {
        int numSlots = Integer.parseInt(value);
        if (numSlots < 0 || numSlots > 2) {
          return "only between 0 and 2 slots";
        }
        return null;
      } catch (Exception e) {
        return "use only numbers";
      }
    } else if (key.equalsIgnoreCase(CATEGORY)) {
      if (CATEGORY_MAP.containsKey(value.toLowerCase())) {
        return null;
      } else {
        return "Invalid value \"" + value + "\". Try one: " + String.join(", ", CATEGORY_MAP.keySet());
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

  public static boolean filter(JSONObject minObj, Map<String, String> filters) {
    if (filters == null || filters.isEmpty()) {
      return false;
    }

    boolean outerFilter = false;
    for (String key : filters.keySet()) {
      boolean innerFilter = false;
      for (String value : filters.get(key).split("&&")) {
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

  private static Integer getRefineValue(String name) {
    String strPattern = "\\+?([0-9]*) .*";
    Pattern p = Pattern.compile(strPattern);
    Matcher matcher = p.matcher(name);
    if (matcher.matches()) {
      String refineStr = matcher.group(1);
      return Integer.parseInt(refineStr);
    }
    return null;
  }

  private static Integer getNumSlots(String name) {
    String strPattern = ".*\\[([0-9]*)].*";
    Pattern p = Pattern.compile(strPattern);
    Matcher matcher = p.matcher(name);
    if (matcher.matches()) {
      String refineStr = matcher.group(1);
      return Integer.parseInt(refineStr);
    }
    return null;
  }

}
