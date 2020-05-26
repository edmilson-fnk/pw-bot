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
    returnMessage.append(" Price: ");
    String priceStr = lastRecord.get("price").toString();
    returnMessage.append(priceWithoutDecimal(Double.parseDouble(priceStr)));

    long snapEnd = Long.parseLong(lastRecord.get("snapEnd").toString());
    if (snapEnd > 0) {
      returnMessage.append(" in snap until ");
      returnMessage.append(formatTimestamp(snapEnd*1000));
    }

    return returnMessage.toString();
  }

  public static void waitSomeTime() {
    try {
      Thread.sleep(1500);
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

  public static String getNearestCommand(String key) {
    String nearest = EditDistance.findNearest(key, COMMAND_MAP.keySet().toArray(new String[0]));
    if (EditDistance.editDistance(key, nearest) <= 4) {
      return nearest;
    } else {
      return null;
    }
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
