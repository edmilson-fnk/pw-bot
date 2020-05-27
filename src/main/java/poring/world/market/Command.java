package poring.world.market;

import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.watcher.Watcher;

import java.util.List;

public abstract class Command {

  public abstract void run(String[] command, MessageCreateEvent event, Watcher watcher);

  public abstract String getHelp();

  public abstract List<String> getQueries();

}
