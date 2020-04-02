package poring.world.market;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.general.Command;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Map;

public class ListC extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    Map<Long, java.util.List<WatchObject>> watcherMap = watcher.getMap();
    MessageAuthor messageAuthor = event.getMessageAuthor();
    if (watcherMap.containsKey(messageAuthor.getId()) && !watcherMap.get(messageAuthor.getId()).isEmpty()) {
      StringBuilder sb = new StringBuilder();
      sb.append("Items for **");
      sb.append(messageAuthor.getDisplayName());
      sb.append("**\n");
      java.util.List<WatchObject> objList = watcherMap.get(messageAuthor.getId());
      for (int i = 0; i < objList.size(); i++) {
        WatchObject obj = objList.get(i);
        sb.append("(");
        sb.append(i + 1);
        sb.append(") _");

        sb.append(obj.getQuery());
        sb.append("_\n");
      }
      event.getChannel().sendMessage(sb.toString());
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
