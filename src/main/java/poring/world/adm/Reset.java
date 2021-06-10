package poring.world.adm;

import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.List;

public class Reset extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    String query = Utils.getQuery(command);
    if (!watcher.getWatcherThread().isAlive() || query.equalsIgnoreCase("force")) {
      watcher.restart();
      String user = event.getMessageAuthor().getDisplayName();
      event.getChannel().sendMessage(String.format("GTB WatcherThread reset by %s", user));
      System.out.printf("GTB WatcherThread reset by %s%n", user);
    } else {
      event.getChannel().sendMessage("GTB WatcherThread is still alive, no need to reset");
    }
  }

  @Override
  public String getHelp() {
    return "resets GTB watcher";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("");
  }
}
