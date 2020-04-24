package poring.world.market;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.general.Command;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Map;

public class Clear extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    MessageAuthor messageAuthor = event.getMessageAuthor();

    if (watcher.getMap().containsKey(messageAuthor.getId())) {
      List<WatchObject> authorList = watcher.getMap().get(messageAuthor.getId());
      authorList.clear();
      Map<String, Map<String, String>> authorFilters = watcher.getFilters().get(messageAuthor.getId());
      authorFilters.clear();
      watcher.saveMap();
      event.getChannel().sendMessage(
          String.format("Removed _%s_ item(s) for _%s_", authorList.size(), messageAuthor.getDisplayName())
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
