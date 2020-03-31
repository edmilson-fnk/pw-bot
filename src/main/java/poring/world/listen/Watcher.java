package poring.world.listen;

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

  public static final int WAITING_MINUTES = 50;
  private Map<MessageAuthor, List<ListenObject>> listenMap;

  public void add(String query, MessageAuthor messageAuthor, TextChannel channel) {
    ListenObject listenObj = new ListenObject(query, messageAuthor, channel);

    if (!listenMap.containsKey(messageAuthor)) {
      listenMap.put(messageAuthor, new LinkedList<>());
    }
    listenMap.get(messageAuthor).add(listenObj);
  }

  public Map<MessageAuthor, List<ListenObject>> getMap() {
    return this.listenMap;
  }

  @Override
  public synchronized void start() {
    super.start();
    System.out.println("Running poring.world bot watcher...");
    this.listenMap = new HashMap<>();
  }

  @Override
  public void run() {
    while (true) {
      System.out.println("Verifying queue on poring.world API...");
      for (MessageAuthor author : listenMap.keySet()) {
        StringBuilder objMessage = new StringBuilder();
        objMessage.append("Hey <@");
        objMessage.append(author.getId());
        objMessage.append(">, we found something for you\n");
        boolean theresSomethingFlag = false;
        for (ListenObject obj : listenMap.get(author)) {
          JSONArray marketItems = Fetcher.query(obj.getQuery());
          if (marketItems.size() > 0) {
            theresSomethingFlag = true;
            objMessage.append("_");
            objMessage.append(obj.getQuery());
            objMessage.append("_\n");
            for (Object marketItem : marketItems) {
              objMessage.append("    ");
              objMessage.append(Utils.getItemMessage((JSONObject) marketItem));
              objMessage.append("\n");
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
