package poring.world.watcher;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import poring.world.Fetcher;
import poring.world.Utils;
import poring.world.s3.S3Files;

import java.io.File;
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
import java.util.stream.Collectors;

public class Watcher extends Thread {

  private static final int WAITING_MINUTES = 60;
  private Map<Long, List<WatchObject>> watchMap;
  private DiscordApi api;

  public Watcher(DiscordApi api) {
    this.api = api;
  }

  public Map<Long, List<WatchObject>> getMap() {
    return this.watchMap;
  }

  public void add(String query, MessageAuthor messageAuthor, TextChannel channel) {
    WatchObject listenObj = new WatchObject(query, messageAuthor, channel);

    long authorId = messageAuthor.getId();
    synchronized (this) {
      if (!watchMap.containsKey(authorId)) {
        watchMap.put(authorId, new LinkedList<>());
      }
      List<String> strObjects = watchMap.get(authorId).stream().map(WatchObject::toString).collect(Collectors.toList());
      if (!strObjects.contains(listenObj.toString())) {
        watchMap.get(authorId).add(listenObj);
        this.saveMap();
      }
    }
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
      waitAMinute();

      if (new DateTime().getMinuteOfHour() != 0) {
        continue;
      }

      synchronized (this) {
        System.out.println("verifying watch list on poring.world API...");
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
      System.out.println("watch list was notified! doing it again in an hour");
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

  private void waitAMinute() {
    try {
      Thread.sleep(1000 * 60);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public synchronized void saveMap() {
    try {
      FileOutputStream fos = new FileOutputStream("watcherMap.dat");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(this.watchMap);
      oos.close();
      fos.close();
      S3Files.uploadWatchList(new File("watcherMap.dat"));
    } catch (FileNotFoundException e) {
      System.out.println("File watcherMap.dat not found on saving");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Error on saving watcherMap.dat");
      e.printStackTrace();
    }
  }

  public synchronized void loadMap() {
    this.watchMap = new HashMap<>();
    try {
      File file = S3Files.downloadWatchlist();
      FileInputStream fis = new FileInputStream(file);
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
