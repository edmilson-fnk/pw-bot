package poring.world;

import org.json.simple.JSONObject;
import poring.world.watcher.WatchObject;

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
import java.util.List;
import java.util.Map;
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
      // TODO fix timestamp on snap time
//      returnMessage.append(" in snap until ");
//      returnMessage.append(formatTimestamp(snapEnd*1000));
    }

    return returnMessage.toString();
  }

  public static void saveMap(Map<Long, List<WatchObject>> map) {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream("watcherMap.dat");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(map);
      oos.close();
      fos.close();
    } catch (FileNotFoundException e) {
      System.out.println("File watcherMap.dat not found");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Error on saving watcherMap.dat");
      e.printStackTrace();
    }
  }

  public static Map<Long, List<WatchObject>> loadMap() {
    try {
      FileInputStream fis = new FileInputStream("watcherMap.dat");
      ObjectInputStream ois = new ObjectInputStream(fis);
      Map<Long, List<WatchObject>> map = new HashMap<>((Map) ois.readObject());
      ois.close();
      return map;
    } catch (FileNotFoundException e) {
      System.out.println("File watcherMap.dat not found");
    } catch (IOException | ClassNotFoundException e) {
      System.out.println("Error on loading map file");
      e.printStackTrace();
    }
    return new HashMap<>();
  }

  public static String formatTimestamp(long timestamp) {
    return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(timestamp));
  }

  public static String priceWithoutDecimal(Double price) {
    return new DecimalFormat("###,###,###,###.##").format(price);
  }

}
