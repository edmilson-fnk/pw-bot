package poring.world.adm;

import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.List;

public class Reset extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    if (watcher.isAlive()) {
      event.getChannel().sendMessage("GTB Watcher is still alive, no need to reset");
    } else {
      watcher.start();
      event.getChannel().sendMessage("GTB Watcher reset");
    }
  }

  @Override
  public String getHelp() {
    return null;
  }

  @Override
  public List<String> getQueries() {
    return null;
  }
}
