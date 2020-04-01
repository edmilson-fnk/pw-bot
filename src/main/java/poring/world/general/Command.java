package poring.world.general;

import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Map;

public abstract class Command {

  public abstract void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters);

  public abstract String getHelp();

  public abstract List<String> getQueries();

}
