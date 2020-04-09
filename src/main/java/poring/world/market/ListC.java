package poring.world.market;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.general.Command;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ListC extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    Map<Long, java.util.List<WatchObject>> watcherMap = watcher.getMap();
    MessageAuthor messageAuthor = event.getMessageAuthor();
    List<String> messages = new LinkedList<>();
    if (watcherMap.containsKey(messageAuthor.getId()) && !watcherMap.get(messageAuthor.getId()).isEmpty()) {
      StringBuilder sb = new StringBuilder();
      sb.append("Items for **");
      sb.append(messageAuthor.getDisplayName());
      sb.append("**\n");
      List<WatchObject> objList = watcherMap.get(messageAuthor.getId());
      for (int i = 0; i < objList.size(); i++) {
        StringBuilder subSb = new StringBuilder();
        WatchObject obj = objList.get(i);
        subSb.append("(");
        subSb.append(i + 1);
        subSb.append(") _");

        subSb.append(obj.getQuery());
        subSb.append("_\n");

        if (subSb.length() + sb.length() >= 2000) {
          messages.add(sb.toString());
          sb = new StringBuilder();
        }
        sb.append(subSb.toString());
      }
      messages.add(sb.toString());
      for (String msg : messages) {
        event.getChannel().sendMessage(msg);
      }
    } else {
      event.getChannel().sendMessage("No items found for _" + messageAuthor.getDisplayName() + "_");
    }
  }

  @Override
  public String getHelp() {
    return "shows your watch list";
  }

  @Override
  public java.util.List<String> getQueries() {
    return ImmutableList.of("");
  }

}
