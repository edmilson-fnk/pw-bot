package poring.world.adm;

import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Map;

public class Metrics extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    Map<Long, List<WatchObject>> m = watcher.getMap();

    int numLists = 0;
    int numItems = 0;
    int maxSize = 0;
    int minSize = 99999;

    for (Long id : m.keySet()) {
      List<WatchObject> list = m.get(id);
      if (list.size() > 0) {
        numLists++;
        numItems += list.size();
        if (maxSize < list.size()) {
          maxSize = list.size();
        }
        if (minSize > list.size()) {
          minSize = list.size();
        }
      }
    }

    event.getChannel().sendMessage(String.format("%s lists; %s items; largest list: %s %s; smallest list: %s %s",
        numLists, numItems, maxSize, Utils.pluralItem(maxSize), minSize, Utils.pluralItem(minSize)));
  }

  @Override
  public String getHelp() {
    return "shows metrics for the bot usage";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("");
  }
}
