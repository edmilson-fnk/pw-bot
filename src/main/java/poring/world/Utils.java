package poring.world;

import static poring.world.Constants.COMMAND_MAP;
import static poring.world.Constants.GENERAL_TIME_FORMAT;
import static poring.world.Constants.TIME_DIFF;

import com.sun.xml.bind.v2.util.EditDistance;
import org.json.simple.JSONObject;
import poring.world.s3.S3Files;
import poring.world.thanatos.ThanatosTeamObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class Utils {

  public static final int SOME_HOURS_AGO = (TIME_DIFF * 3600 * 1000);

  public static String getQuery(String[] command) {
    StringJoiner joiner = new StringJoiner(" ");
    for (int i = 2; i < command.length; i++) {
      joiner.add(command[i]);
    }
    return joiner.toString().trim();
  }

  public static String getItemStock(Object item) {
    JSONObject lastRecord = (JSONObject) ((JSONObject) item).get("lastRecord");
    return String.format("%sx", lastRecord.get("stock").toString());
  }

  public static String getItemName(Object item) {
    return ((JSONObject) item).get("name").toString();
  }

  public static String getItemSnap(Object item) {
    JSONObject lastRecord = (JSONObject) ((JSONObject) item).get("lastRecord");
    long snapEnd = Long.parseLong(lastRecord.get("snapEnd").toString());
    StringBuilder returnMessage = new StringBuilder();
    if (snapEnd > 0) {
      returnMessage.append(" until ");
      returnMessage.append(formatTimestamp(snapEnd*1000));
      returnMessage.append(" by ");
      returnMessage.append(lastRecord.get("snapBuyers").toString());
    }
    return returnMessage.toString();
  }

  public static String getItemPrice(Object item) {
    return priceWithoutDecimal(
        Double.parseDouble(((JSONObject) ((JSONObject) item).get("lastRecord")).get("price").toString())
    );
  }

  public static String getItemMessage(JSONObject jsonItem) {
    return getItemMessage(jsonItem, "**");
  }

  public static String getItemMessage(JSONObject jsonItem, String highlighter) {
    StringBuilder returnMessage = new StringBuilder();
    JSONObject lastRecord = (JSONObject) jsonItem.get("lastRecord");

    returnMessage.append(String.format("%-10s", lastRecord.get("stock").toString() + "x"));
    returnMessage.append(highlighter);
    returnMessage.append(jsonItem.get("name"));
    returnMessage.append(highlighter);
    returnMessage.append(" for ");
    String priceStr = lastRecord.get("price").toString();
    returnMessage.append(priceWithoutDecimal(Double.parseDouble(priceStr)));

    long snapEnd = Long.parseLong(lastRecord.get("snapEnd").toString());
    if (snapEnd > 0) {
      returnMessage.append(String.format(" in snap until %s by %s", formatTimestamp(snapEnd*1000),
          lastRecord.get("snapBuyers")));
    }

    return returnMessage.toString();
  }

  public static void waitSomeTime() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      System.out.println("Error on watcher thread!");
      throw new RuntimeException(e);
    }
  }

  public static String formatTimestamp(long timestamp) {
    Date date = new Date(timestamp - SOME_HOURS_AGO); // TODO use Timezone
    return new SimpleDateFormat(GENERAL_TIME_FORMAT).format(date);
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

  public static String pluralItem(int num) {
    return Utils.pluralize(num, "item", "items");
  }

  public static String pluralize(int num, String singular, String plural) {
    return num == 1 ? singular : plural;
  }

  public static String getNearestCommand(String key, Set<String> commands) {
    String nearest = EditDistance.findNearest(key, commands.toArray(new String[0]));
    if (EditDistance.editDistance(key, nearest) <= 4) {
      return nearest;
    } else {
      return null;
    }
  }

  public static String getNearestCommand(String key) {
    return getNearestCommand(key, COMMAND_MAP.keySet());
  }

  public static synchronized File saveMapFile(Map map, String fileName) {
    try {
      FileOutputStream fos = new FileOutputStream(fileName);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(map);
      oos.close();
      fos.close();
      return new File(fileName);
    } catch (FileNotFoundException e) {
      System.out.println(String.format("File %s not found on saving", fileName));
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(String.format("Error on saving %s", fileName));
    }
    return null;
  }

  public static synchronized Map loadMapFile(String fileName) {
    try {
      File file = S3Files.getFile(fileName);
      FileInputStream fis = new FileInputStream(file);
      ObjectInputStream ois = new ObjectInputStream(fis);
      Map<Long, ThanatosTeamObject> map = new HashMap<>((Map) ois.readObject());
      ois.close();
      return map;
    } catch (FileNotFoundException e) {
      System.out.println(String.format("File %s not found on reading", fileName));
      e.printStackTrace();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      System.out.println(String.format("Error on loading %s", fileName));
    }
    return new HashMap();
  }

}
