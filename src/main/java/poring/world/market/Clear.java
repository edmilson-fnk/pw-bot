package poring.world.market;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Map;

public class Clear extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    MessageAuthor messageAuthor = event.getMessageAuthor();

    if (watcher.getMap().containsKey(messageAuthor.getId())) {
      List<WatchObject> authorList = watcher.getMap().get(messageAuthor.getId());
      int size = 0;
      if (authorList != null) {
        authorList.clear();
        size = authorList.size();
      }
      Map<String, Map<String, String>> authorFilters = watcher.getFilters().get(messageAuthor.getId());
      if (authorFilters != null) {
        authorFilters.clear();
      }
      watcher.saveMaps();
      event.getChannel().sendMessage(
          String.format("Removed _%s_ %s for _%s_", size, Utils.pluralItem(size), messageAuthor.getDisplayName())
      );
    }
  }

  @Override
  public String getHelp() {
    return "removes every query on your watch list";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("");
  }

}
