package poring.world.listen;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import poring.world.Fetcher;
import poring.world.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Listener extends Thread {

  public static final int WAITING_MINUTES = 50;
  private List<ListenObject> listenQueue;
  private Map<MessageAuthor, List<ListenObject>> listenMap;

  public void add(String query, MessageAuthor messageAuthor, TextChannel channel) {
    ListenObject listenObj = new ListenObject(query, messageAuthor, channel);
    listenQueue.add(listenObj);

    if (!listenMap.containsKey(messageAuthor)) {
      listenMap.put(messageAuthor, new LinkedList<>());
    }
    listenMap.get(messageAuthor).add(listenObj);

    System.out.println("Added \"" + query + "\" on listener queue");
  }

  public void remove(ListenObject obj) {
    listenQueue.remove(obj);
  }

  public List<ListenObject> getQueue() {
    return this.listenQueue;
  }

  public Map<MessageAuthor, List<ListenObject>> getMap() {
    return this.listenMap;
  }

  public void remove(MessageAuthor author, ListenObject obj) {
    if (listenMap.containsKey(author)) {
      listenMap.get(author).remove(obj);
    }
  }

  @Override
  public synchronized void start() {
    super.start();
    System.out.println("Running poring.world bot listener...");
    this.listenQueue = new LinkedList<>();
  }

  @Override
  public void run() {
    while (true) {
      System.out.println("Verifying queue on poring.world API...");
      for (ListenObject obj : listenQueue) {
        JSONArray marketItems = Fetcher.query(obj.getQuery());
        StringBuilder objMessage = new StringBuilder();
        if (marketItems.size() > 0) {
          objMessage.append("Hey <@");
          objMessage.append(obj.getMessageAuthor().getId());
          objMessage.append(">, we found something for \"_");
          objMessage.append(obj.getQuery());
          objMessage.append("_\" right now:");
          objMessage.append("\n");
          for (Object marketObj : marketItems) {
            objMessage.append(Utils.getItemMessage((JSONObject) marketObj));
            objMessage.append("\n");
          }
          obj.getChannel().sendMessage(objMessage.toString());
        }
      }
      try {
        System.out.println("Waiting " + WAITING_MINUTES + " minutes...");
        Thread.sleep(1000 * 60 * WAITING_MINUTES);
      } catch (InterruptedException e) {
        System.out.println("Error on listener thread!");
        throw new RuntimeException(e);
      }
    }
  }
}
