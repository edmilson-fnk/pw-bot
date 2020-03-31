package poring.world.command;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.listen.ListenObject;
import poring.world.listen.Watcher;

import java.util.List;

public class RemoveCommand extends Command {
  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    MessageAuthor messageAuthor = event.getMessageAuthor();
    String query = Utils.getQuery(command);
    List<ListenObject> objList = watcher.getMap().get(messageAuthor);
    if (query == null || query.isEmpty()) {
      event.getChannel().sendMessage("No index specified, _" + messageAuthor.getName() +
          "_. Try _!pw help list_ for more information");
      return;
    }

    if (objList != null) {
      int pos = Integer.parseInt(query);
      ListenObject removed = objList.remove(pos - 1);

      String sb = "Removed _" +
          removed.getQuery() +
          "_ for _" +
          messageAuthor.getName() +
          "_\n";
      event.getChannel().sendMessage(sb);
    } else {
      event.getChannel().sendMessage("No watch list for _" + messageAuthor.getName() + "_");
    }
  }

  @Override
  public String getHelp() {
    return "removes selected item from you watch list. try _!pw list_ to check the index before removing";
  }

  @Override
  public List<String> getUsage() {
    return ImmutableList.of(
        "!pw remove 2"
    );
  }
}
