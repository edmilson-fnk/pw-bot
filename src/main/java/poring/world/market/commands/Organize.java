package poring.world.market.commands;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.market.Command;
import poring.world.market.filter.FilterUtils;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;
import poring.world.watcher.WatcherThread;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Organize extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    WatcherThread watcherThread = watcher.getWatcherThread();
    Map<Long, List<WatchObject>> watcherMap = watcherThread.getMap();
    Map<Long, Map<String, Map<String, String>>> filtersMap = watcherThread.getFilters();
    MessageAuthor messageAuthor = event.getMessageAuthor();
    if (watcherMap.containsKey(messageAuthor.getId()) && !watcherMap.get(messageAuthor.getId()).isEmpty()) {
      List<WatchObject> objList = watcherMap.get(messageAuthor.getId());
      Map<String, Map<String, String>> filtersList = filtersMap != null ? filtersMap.get(messageAuthor.getId()) : null;
      Comparator<? super WatchObject> comp = (Comparator<WatchObject>) (o1, o2) -> {
        Map<String, String> f1 = filtersList != null ? filtersList.get(o1.toString()) : null;
        Map<String, String> f2 = filtersList != null ? filtersList.get(o2.toString()) : null;

        return (o1.getQuery() + FilterUtils.translate(f1)).compareTo(o2.getQuery() + FilterUtils.translate(f2));
      };
      objList.sort(comp);
      watcherMap.put(messageAuthor.getId(), objList);
      watcherThread.saveMaps();
      event.getChannel().sendMessage(String.format("Organized **%s**'s list\n", messageAuthor.getDisplayName()));
    } else {
      event.getChannel().sendMessage("No item found for **" + messageAuthor.getDisplayName() + "**");
    }
  }

  @Override
  public String getHelp() {
    return "organizes your list lexicographically";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("");
  }
}
