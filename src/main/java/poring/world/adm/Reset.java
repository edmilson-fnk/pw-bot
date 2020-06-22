package poring.world.adm;

import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.WatcherThread;

import java.util.List;

public class Reset extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, WatcherThread watcher) {
    String query = Utils.getQuery(command);
    if (!watcher.isAlive() || query.equalsIgnoreCase("force")) {
      watcher.interrupt();
      watcher.run();
      event.getChannel().sendMessage("GTB WatcherThread reset");
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
