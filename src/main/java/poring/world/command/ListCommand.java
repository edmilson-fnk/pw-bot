package poring.world.command;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.listen.ListenObject;
import poring.world.listen.Listener;

import java.util.List;
import java.util.Map;

public class ListCommand extends Command {
  @Override
  public void run(String[] command, MessageCreateEvent event, Listener listener) {
    Map<MessageAuthor, List<ListenObject>> listenerMap = listener.getMap();
    MessageAuthor messageAuthor = event.getMessageAuthor();
    if (listenerMap.containsKey(messageAuthor) && !listenerMap.get(messageAuthor).isEmpty()) {
      StringBuilder sb = new StringBuilder();
      sb.append("Items for **");
      sb.append(messageAuthor.getName());
      sb.append("**\n");
      List<ListenObject> authorList = listenerMap.get(messageAuthor);
      for (int i = 0; i < authorList.size(); i++) {
        ListenObject obj = authorList.get(i);
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
