package poring.world.command;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.listen.ListenObject;
import poring.world.listen.Listener;

import java.util.List;

public class RemoveCommand extends Command {
  @Override
  public void run(String[] command, MessageCreateEvent event, Listener listener) {
    MessageAuthor messageAuthor = event.getMessageAuthor();
    List<ListenObject> objList = listener.getMap().get(messageAuthor);
    if (objList != null) {
      int pos = Integer.parseInt(Utils.getQuery(command));
      ListenObject removed = objList.remove(pos);

      String sb = "Removed _" +
          removed.getQuery() +
          "_ for _" +
          messageAuthor.getName() +
          "_\n";
      event.getChannel().sendMessage(sb);
    }
  }

  @Override
  public String getHelp() {
    return "removes selected item from you watch list. try _!pw list_ before removing";
  }

  @Override
  public List<String> getUsage() {
    return ImmutableList.of(
        "!pw remove 2"
    );
  }
}
