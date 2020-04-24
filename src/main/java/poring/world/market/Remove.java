package poring.world.market;

import static poring.world.Constants.GLOBAL_CALL;
import static poring.world.Constants.HELP;
import static poring.world.Constants.LIST;
import static poring.world.Constants.REMOVE;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.general.Command;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Remove extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    MessageAuthor messageAuthor = event.getMessageAuthor();
    String query = Utils.getQuery(command);
    List<WatchObject> objList = watcher.getMap().get(messageAuthor.getId());
    Map<String, Map<String, String>> filters = watcher.getFilters().get(messageAuthor.getId());
    if (query == null || query.isEmpty()) {
      event.getChannel().sendMessage(
          String.format("No index, Try _!%s %s %s_ for more information", GLOBAL_CALL, HELP, REMOVE));
      return;
    }

    if (objList != null && !objList.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      List<String> numbers = new LinkedList<>(new HashSet<>(Arrays.asList(query.split(" "))));
      numbers.sort(Collections.reverseOrder());
      HashSet<String> numbersSet = new HashSet<>(numbers);
      for (String num : numbersSet) {
        try {
          Integer.parseInt(num);
        } catch (NumberFormatException e) {
          event.getChannel().sendMessage(
              String.format("Invalid option **%s**\nPlease check _!%s %s %s_", query, GLOBAL_CALL, HELP, REMOVE)
          );
          return;
        }
        int pos = Integer.parseInt(num);
        if (pos > objList.size()) {
          event.getChannel().sendMessage(String.format("Maximum value to remove is **%s**", objList.size()));
          return;
        }
        WatchObject removed = objList.remove(pos - 1);
        if (filters != null) {
          filters.remove(removed.toString());
        }
        sb.append(String.format("Removed _%s_ for _%s_", removed.getQuery(), messageAuthor.getDisplayName()));
      }
      event.getChannel().sendMessage(sb.toString());
      watcher.saveMap();
    } else {
      event.getChannel().sendMessage(String.format("No watch list for _%s_", messageAuthor.getDisplayName()));
    }
  }

  @Override
  public String getHelp() {
    return String.format(
        "removes selected items from your watch list. try _!%s %s_ and check indexes to remove", GLOBAL_CALL, LIST);
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of(
        "1", "2", "5 10 6 1"
    );
  }

}
