package poring.world.market.commands;

import static poring.world.constants.Constants.GLOBAL_CALL;
import static poring.world.constants.Constants.HELP;
import static poring.world.constants.Constants.LIST;
import static poring.world.constants.Constants.REMOVE;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;
import poring.world.watcher.WatcherThread;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Remove extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    WatcherThread watcherThread = watcher.getWatcherThread();
    MessageAuthor messageAuthor = event.getMessageAuthor();
    String query = Utils.getQuery(command);
    List<WatchObject> objList = watcherThread.getMap().get(messageAuthor.getId());
    Map<String, Map<String, String>> filters = watcherThread.getFilters().get(messageAuthor.getId());
    if (query.isEmpty()) {
      event.getChannel().sendMessage(
          String.format("No index, Try _!%s %s %s_ for more information", GLOBAL_CALL, HELP, REMOVE));
      return;
    }

    if (objList != null && !objList.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      List<String> numbers = new LinkedList<>(new HashSet<>(Arrays.asList(query.split(" "))));
      List<String> toRemove = new LinkedList<>();
      for (String num : numbers) {
        if (num.isEmpty()) {
          toRemove.add(num);
        }
        try {
          Integer.parseInt(num);
        } catch (NumberFormatException e) {
          event.getChannel().sendMessage(
              String.format("Invalid option **%s**\nPlease check _!%s %s %s_", query, GLOBAL_CALL, HELP, REMOVE)
          );
          return;
        }
      }
      numbers.removeAll(toRemove);
      for (Integer pos : numbers.stream().map(Integer::parseInt).sorted(Collections.reverseOrder()).collect(Collectors.toList())) {
        if (pos > objList.size()) {
          event.getChannel().sendMessage(String.format("Maximum value to remove is **%s**", objList.size()));
          return;
        }
        WatchObject removed = objList.remove(pos - 1);
        if (filters != null) {
          filters.remove(removed.toString());
        }
        sb.append(String.format("Removed _%s_ for _%s_\n", removed.getQuery(), messageAuthor.getDisplayName()));
      }
      event.getChannel().sendMessage(sb.toString());
      watcherThread.saveMaps();
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
