package poring.world.watcher;

import static poring.world.s3.S3Files.WATCHER_FILTERS_DAT;
import static poring.world.s3.S3Files.WATCHER_MAP_DAT;

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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Watcher extends Thread {

  private Map<Long, List<WatchObject>> watchMap;
  private Map<Long, Map<String, Map<String, String>>> watchMapFilters;
  private DiscordApi api;

  public Watcher(DiscordApi api) {
    this.api = api;
  }

  public Map<Long, List<WatchObject>> getMap() {
    if (this.watchMap == null) {
      loadMaps();
    }
    return this.watchMap;
  }


  public Map<Long, Map<String, Map<String, String>>> getFilters() {
    if (this.watchMapFilters == null) {
      loadMaps();
    }
    return this.watchMapFilters;
  }

  public void add(String query, MessageAuthor messageAuthor, TextChannel channel) {
    add(query, messageAuthor, channel, null);
  }


  public void add(String query, MessageAuthor messageAuthor, TextChannel channel, Map<String, String> filters) {
    WatchObject listenObj = new WatchObject(query, messageAuthor, channel);

    long authorId = messageAuthor.getId();
    synchronized (this) {
      if (!watchMap.containsKey(authorId)) {
        watchMap.put(authorId, new LinkedList<>());
      }
      List<String> strObjects = watchMap.get(authorId).stream().map(WatchObject::toString).collect(Collectors.toList());
      String listenObjStr = listenObj.toString();
      if (!strObjects.contains(listenObjStr)) {
        watchMap.get(authorId).add(listenObj);

        // Save filters on this query
        if (filters != null && !filters.isEmpty()) {
          if (!watchMapFilters.containsKey(authorId)) {
            watchMapFilters.put(authorId, new HashMap<>());
          }
          Map<String, Map<String, String>> filterMap = watchMapFilters.get(authorId);
          if (filterMap.containsKey(listenObjStr)) {
            filterMap.put(listenObjStr, new HashMap<>());
          }
          filterMap.put(listenObjStr, filters);
        }

        this.saveMaps();
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
    this.loadMaps();

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

          List<String> messages = new LinkedList<>();
          StringBuilder sb = new StringBuilder();
          sb.append(String.format("Hey <@%s>, we found something for you\n", authorId));

          boolean theresSomethingFlag = false;
          for (WatchObject obj : watchMap.get(authorId)) {
            Map<String, String> filters = watchMapFilters.containsKey(authorId) ?
                watchMapFilters.get(authorId).getOrDefault(obj, null) :
                null;
            JSONArray marketItems = Fetcher.query(obj.getQuery(), filters);
            if (marketItems.size() > 0) {
              StringBuilder objMessage = new StringBuilder();
              theresSomethingFlag = true;
              objMessage.append(String.format("_%s_\n", obj.getQuery()));
              for (Object marketItem : marketItems) {
                objMessage.append(String.format("    %s\n", Utils.getItemMessage((JSONObject) marketItem)));
              }
              if (objMessage.length() + sb.length() >= 2000) {
                messages.add(sb.toString());
                sb = new StringBuilder();
              }
              sb.append(objMessage.toString());
            }
          }
          messages.add(sb.toString());
          if (theresSomethingFlag) {
            for (String msg : messages) {
              author.sendMessage(msg);
            }
          }
        }
      }
      System.out.println("watch list was notified! doing it again in an hour");
    }
  }

  private void waitAMinute() {
    try {
      Thread.sleep(1000 * 60);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public synchronized void saveMaps() {
    File mapFile = Utils.saveMapFile(this.watchMap, WATCHER_MAP_DAT);
    S3Files.uploadWatchList(mapFile);
    File filtersFile = Utils.saveMapFile(this.watchMapFilters, WATCHER_FILTERS_DAT);
    S3Files.uploadFiltersList(filtersFile);
  }

  public synchronized void loadMaps() {
    this.watchMap = Utils.loadMapFile(WATCHER_MAP_DAT);
    this.watchMapFilters = Utils.loadMapFile(WATCHER_FILTERS_DAT);
  }

}
