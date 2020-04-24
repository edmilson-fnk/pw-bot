package poring.world.market;

import static poring.world.Constants.GLOBAL_CALL;
import static poring.world.Constants.HELP;
import static poring.world.Constants.REMOVE;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.general.Command;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.Arrays;
import java.util.Collections;
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
          String.format("No index, Try _!%s %s %s_ for more information", GLOBAL_CALL, HELP, REMOVE));
      return;
    }

    if (objList != null && !objList.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      List<String> numbers = Arrays.asList(query.split(" "));
      Collections.sort(numbers, Collections.reverseOrder());
      for (String num : numbers) {
        try {
          Integer.parseInt(num);
        } catch (NumberFormatException e) {
          event.getChannel().sendMessage(
              String.format("Invalid option **%s**\nPlease see !%s %s %s", query, GLOBAL_CALL, HELP, REMOVE)
          );
          return;
        }
        int pos = Integer.parseInt(num);
        if (pos > objList.size()) {
          event.getChannel().sendMessage(String.format("Maximum value to remove is **%s**", objList.size()));
          return;
        }
        WatchObject removed = objList.remove(pos - 1);
        watcher.saveMap();

        sb.append(String.format("Removed _%s_ for _%s_", removed.getQuery(), messageAuthor.getDisplayName()));
      }
      event.getChannel().sendMessage(sb.toString());
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
        "2", "13"
    );
  }

}
