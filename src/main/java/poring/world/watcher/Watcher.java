package poring.world.watcher;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import poring.world.Fetcher;
import poring.world.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Watcher extends Thread {

  private static final int WAITING_MINUTES = 50;
  private Map<MessageAuthor, List<WatchObject>> watchMap;

  public void add(String query, MessageAuthor messageAuthor, TextChannel channel) {
    WatchObject listenObj = new WatchObject(query, messageAuthor, channel);

    if (!watchMap.containsKey(messageAuthor)) {
      watchMap.put(messageAuthor, new LinkedList<>());
    }
    watchMap.get(messageAuthor).add(listenObj);
  }

  public Map<MessageAuthor, List<WatchObject>> getMap() {
    return this.watchMap;
  }

  @Override
  public synchronized void start() {
    super.start();
    System.out.println("Running poring.world bot watcher...");
    this.watchMap = new HashMap<>();
  }

  @Override
  public void run() {
    while (true) {
      System.out.println("Verifying queue on poring.world API...");
      for (MessageAuthor author : watchMap.keySet()) {
        StringBuilder objMessage = new StringBuilder();
        objMessage.append(String.format("Hey <@%s>, we found something for you\n", author.getId()));

        boolean theresSomethingFlag = false;
        for (WatchObject obj : watchMap.get(author)) {
          JSONArray marketItems = Fetcher.query(obj.getQuery());
          if (marketItems.size() > 0) {
            theresSomethingFlag = true;
            objMessage.append(String.format("_%s_\n", obj.getQuery()));
            for (Object marketItem : marketItems) {
              objMessage.append(String.format("    %s\n", Utils.getItemMessage((JSONObject) marketItem)));
            }
          }
          if (theresSomethingFlag) {
            obj.getChannel().sendMessage(objMessage.toString());
          }
        }
      }
      try {
        System.out.println("Waiting " + WAITING_MINUTES + " minutes...");
        Thread.sleep(1000 * 60 * WAITING_MINUTES);
      } catch (InterruptedException e) {
        System.out.println("Error on watcher thread!");
        throw new RuntimeException(e);
      }
    }
  }

}
