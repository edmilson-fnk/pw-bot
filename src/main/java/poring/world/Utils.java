package poring.world;

import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;

public class Utils {

  public static String getQuery(String[] command) {
    return getQuery(command, 2);
  }

  public static String getQuery(String[] command, int index) {
    StringJoiner joiner = new StringJoiner(" ");
    for (int i = index; i < command.length; i++) {
      joiner.add(command[i]);
    }
    return joiner.toString();
  }

  public static String getItemMessage(JSONObject jsonItem) {
    return getItemMessage(jsonItem, "**");
  }

  public static String getItemMessage(JSONObject jsonItem, String highlighter) {
    StringBuilder returnMessage = new StringBuilder();
    returnMessage.append(highlighter);
    returnMessage.append(jsonItem.get("name"));
    returnMessage.append(highlighter);
    returnMessage.append(" Price: ");
    String priceStr = ((JSONObject) jsonItem.get("lastRecord")).get("price").toString();
    returnMessage.append(priceWithoutDecimal(Double.parseDouble(priceStr)));

    long snapEnd = Long.parseLong(((JSONObject) jsonItem.get("lastRecord")).get("snapEnd").toString());
    if (snapEnd > 0) {
      returnMessage.append(" in snap!");
      // TODO fix timestamp on snap time
//      returnMessage.append(" in snap until ");
//      returnMessage.append(formatTimestamp(snapEnd*1000));
    }

    return returnMessage.toString();
  }

  public static String formatTimestamp(long timestamp) {
    return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(timestamp));
  }

  public static String priceWithoutDecimal(Double price) {
    return new DecimalFormat("###,###,###,###.##").format(price);
  }

  public static String capitalize(String str) {
    if(str == null || str.isEmpty()) {
      return str;
    }

    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

}
