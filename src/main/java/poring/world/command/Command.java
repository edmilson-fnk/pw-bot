package poring.world.command;

import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.listen.Watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Command {

  public Map<String, Object> parameters = new HashMap<>();

  public abstract void run(String[] command, MessageCreateEvent event, Watcher watcher);

  public abstract String getHelp();

  public abstract List<String> getUsage();

}
