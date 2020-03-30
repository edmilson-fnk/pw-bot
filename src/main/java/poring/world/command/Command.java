package poring.world.command;

import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.listen.Listener;

import java.util.List;

public abstract class Command {

  public abstract void run(String[] command, MessageCreateEvent event, Listener listener);

  public abstract String getHelp();

  public abstract List<String> getUsage();

}
