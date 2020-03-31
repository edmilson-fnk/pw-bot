package poring.world.command;

import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.listen.Watcher;

import java.util.List;

public abstract class Command {

  public abstract void run(String[] command, MessageCreateEvent event, Watcher watcher);

  public abstract String getHelp();

  public abstract List<String> getUsage();

}
