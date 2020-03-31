package poring.world;

import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;

public class Utils {

  public static String getQuery(String[] command) {
    StringJoiner joiner = new StringJoiner(" ");
    for (int i = 2; i < command.length; i++) {
      joiner.add(command[i]);
    }
    return joiner.toString();
  }

  public static String getItemMessage(JSONObject jsonItem) {
    StringBuilder returnMessage = new StringBuilder();
    returnMessage.append("**");
    returnMessage.append(jsonItem.get("name"));
    returnMessage.append("**");
    returnMessage.append(" Price: ");
    String priceStr = ((JSONObject) jsonItem.get("lastRecord")).get("price").toString();
    returnMessage.append(priceWithoutDecimal(Double.parseDouble(priceStr)));

    long snapEnd = Long.parseLong(((JSONObject) jsonItem.get("lastRecord")).get("snapEnd").toString());
    if (snapEnd > 0) {
      returnMessage.append(" in snap!");
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

}
