package poring.world.market;

import static poring.world.Constants.GLOBAL_CALL;
import static poring.world.Constants.HELP;
import static poring.world.Constants.LIST;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.general.Command;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Map;

public class Remove extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    MessageAuthor messageAuthor = event.getMessageAuthor();
    String query = Utils.getQuery(command);
    List<WatchObject> objList = watcher.getMap().get(messageAuthor.getId());
    if (query == null || query.isEmpty()) {
      event.getChannel().sendMessage(
          String.format("No index, Try _!%s %s %s_ for more information", GLOBAL_CALL, HELP, LIST));
      return;
    }

    if (objList != null && !objList.isEmpty()) {
      int pos = Integer.parseInt(query);
      if (pos > objList.size()) {
        event.getChannel().sendMessage(String.format("Maximum value to remove is **%s**", objList.size()));
        return;
      }
      WatchObject removed = objList.remove(pos - 1);
      watcher.saveMap();

      event.getChannel().sendMessage(
          String.format("Removed _%s_ for _%s_", removed.getQuery(), messageAuthor.getDisplayName()));
    } else {
      event.getChannel().sendMessage("No watch list for _" + messageAuthor.getDisplayName() + "_");
    }
  }

  @Override
  public String getHelp() {
    return "removes selected item from you watch list. try _!" + GLOBAL_CALL +
        " list_ to check the index before removing";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of(
        "2"
    );
  }

}
