package poring.world;

import org.json.simple.JSONObject;
import poring.world.watcher.WatchObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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

  public static void saveMap(Map<Long, List<WatchObject>> map) throws IOException {
    FileOutputStream fos = new FileOutputStream("watcherMap.dat");
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(map);
    oos.close();
    fos.close();
    System.out.print("Serialized HashMap data is saved in watcherMap.dat");
  }

  public static void loadMap(Map<Long, List<WatchObject>> map) throws IOException {
    FileOutputStream fos = new FileOutputStream("watcherMap.dat");
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(map);
    oos.close();
    fos.close();
    System.out.print("Serialized HashMap data is saved in watcherMap.dat");
  }

  public static void main(String[] args) throws IOException {
    WatchObject w1 = new WatchObject("query", "Ved", 01L, 02L);
    List<WatchObject> list = new LinkedList<>();
    list.add(w1);

    Map<Long, List<WatchObject>> map = new HashMap<>();
    map.put(01L, list);
    saveMap(map);
  }

  public static String formatTimestamp(long timestamp) {
    return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(timestamp));
  }

  public static String priceWithoutDecimal(Double price) {
    return new DecimalFormat("###,###,###,###.##").format(price);
  }

}
