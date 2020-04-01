package poring.world.watcher;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import poring.world.Fetcher;
import poring.world.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Watcher extends Thread {

  private static final int WAITING_MINUTES = 60;
  private Map<Long, List<WatchObject>> watchMap;
  private DiscordApi api;

  public Watcher(DiscordApi api) {
    this.api = api;
  }

  public void add(String query, MessageAuthor messageAuthor, TextChannel channel) {
    WatchObject listenObj = new WatchObject(query, messageAuthor, channel);

    long authorId = messageAuthor.getId();
    if (!watchMap.containsKey(authorId)) {
      watchMap.put(authorId, new LinkedList<>());
    }
    watchMap.get(authorId).add(listenObj);
    this.saveMap();
  }

  public Map<Long, List<WatchObject>> getMap() {
    return this.watchMap;
  }

  @Override
  public synchronized void start() {
    super.start();
    System.out.println("Running poring.world bot watcher...");
  }

  @Override
  public void run() {
    this.loadMap();

    while (true) {
      waitSomeTime();

      System.out.println("Verifying queue on poring.world API...");
      if (watchMap == null || watchMap.isEmpty()) {
        continue;
      }
      for (Long authorId : watchMap.keySet()) {
        User author;
        try {
          author = api.getUserById(authorId).get();
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
          continue;
        }
        StringBuilder objMessage = new StringBuilder();
        objMessage.append(String.format("Hey <@%s>, we found something for you\n", author.getId()));

        boolean theresSomethingFlag = false;
        for (WatchObject obj : watchMap.get(author.getId())) {
          JSONArray marketItems = Fetcher.query(obj.getQuery());
          if (marketItems.size() > 0) {
            theresSomethingFlag = true;
            objMessage.append(String.format("_%s_\n", obj.getQuery()));
            for (Object marketItem : marketItems) {
              objMessage.append(String.format("    %s\n", Utils.getItemMessage((JSONObject) marketItem)));
            }
          }
        }
        if (theresSomethingFlag) {
          author.sendMessage(objMessage.toString());
        }
      }
    }
  }

  private void waitSomeTime() {
    try {
      System.out.println("Waiting " + WAITING_MINUTES + " minutes...");
      Thread.sleep(1000 * 60 * WAITING_MINUTES);
    } catch (InterruptedException e) {
      System.out.println("Error on watcher thread!");
      throw new RuntimeException(e);
    }
  }

  public void saveMap() {
    try {
      FileOutputStream fos = new FileOutputStream("watcherMap.dat");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(this.watchMap);
      oos.close();
      fos.close();
    } catch (FileNotFoundException e) {
      System.out.println("File watcherMap.dat not found on saving");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Error on saving watcherMap.dat");
      e.printStackTrace();
    }
  }

  public void loadMap() {
    this.watchMap = new HashMap<>();
    try {
      FileInputStream fis = new FileInputStream("watcherMap.dat");
      ObjectInputStream ois = new ObjectInputStream(fis);
      Map<Long, List<WatchObject>> map = new HashMap<>((Map) ois.readObject());
      ois.close();
      this.watchMap = map;
    } catch (FileNotFoundException e) {
      System.out.println("File watcherMap.dat not found on reading");
    } catch (IOException | ClassNotFoundException e) {
      System.out.println("Error on loading map file");
      e.printStackTrace();
    }
  }

}
