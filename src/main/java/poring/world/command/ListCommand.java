package poring.world.command;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.listen.WatchObject;
import poring.world.listen.Watcher;

import java.util.List;
import java.util.Map;

public class ListCommand extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    Map<MessageAuthor, List<WatchObject>> watcherMap = watcher.getMap();
    MessageAuthor messageAuthor = event.getMessageAuthor();
    if (watcherMap.containsKey(messageAuthor) && !watcherMap.get(messageAuthor).isEmpty()) {
      StringBuilder sb = new StringBuilder();
      sb.append("Items for **");
      sb.append(messageAuthor.getName());
      sb.append("**\n");
      List<WatchObject> objList = watcherMap.get(messageAuthor);
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
      event.getChannel().sendMessage("No items found for _" + messageAuthor.getName() + "_");
    }
  }

  @Override
  public String getHelp() {
    return "shows your watch list";
  }

  @Override
  public List<String> getUsage() {
    return ImmutableList.of("!pw list");
  }

}
