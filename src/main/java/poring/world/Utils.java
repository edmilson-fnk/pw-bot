package poring.world;

import com.google.common.collect.ImmutableList;
import com.sun.xml.bind.v2.util.EditDistance;
import org.json.simple.JSONObject;
import poring.world.s3.S3Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import static poring.world.constants.Constants.QUERY_SPLIT_TOKEN;

public class Utils {

  public static String getQuery(String[] command) {
    StringJoiner joiner = new StringJoiner(" ");
    for (int i = 2; i < command.length; i++) {
      joiner.add(command[i]);
    }
    return joiner.toString().trim().toLowerCase();
  }

  public static List<String> getNames(String query) {
    List<String> names = new LinkedList<>();
    for (String name : query.trim().split(QUERY_SPLIT_TOKEN)) {
      if (!name.trim().isEmpty()) {
        names.add(name.trim());
      }
    }
    return names;
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
      returnMessage.append(String.format(" snapped by %s, until %s", lastRecord.get("snapBuyers"),
          formatTimestamp(snapEnd*1000)));
    }

    return returnMessage.toString();
  }

  public static void waitSomeTime() {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      System.out.println("Error while waiting a second!");
      throw new RuntimeException(e);
    }
  }

  public static String formatTimestamp(long timestamp) {
    return String.format("<t:%d:f>", timestamp/1000);
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

  public static String getUserAgent() {
    List<String> userAgents = ImmutableList.of(
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36",
        "Mozilla/5.0 (Linux; Android 6.0.1; SM-G920V Build/MMB29K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.36",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.246"
    );

    return userAgents.stream()
                     .skip((int) (userAgents.size() * Math.random()))
                     .findFirst().get();
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
      System.out.printf("File %s not found on saving%n", fileName);
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.printf("Error on saving %s%n", fileName);
    }
    return null;
  }

  public static synchronized Map loadMapFile(String fileName) {
    try {
      File file = S3Files.getFile(fileName);
      FileInputStream fis = new FileInputStream(file);
      ObjectInputStream ois = new ObjectInputStream(fis);
      Map map = new HashMap<>((Map) ois.readObject());
      ois.close();
      return map;
    } catch (FileNotFoundException e) {
      System.out.printf("File %s not found on reading%n", fileName);
      e.printStackTrace();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      System.out.printf("Error on loading %s%n", fileName);
    }
    return new HashMap();
  }

}
