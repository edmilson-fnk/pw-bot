package poring.world.command;

import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.listen.ListenObject;
import poring.world.listen.Listener;

import java.util.LinkedList;
import java.util.List;

public class CleanCommand extends Command {
  @Override
  public void run(String[] command, MessageCreateEvent event, Listener listener) {
    List<ListenObject> toRemove = new LinkedList<>();
    for (ListenObject obj : listener.getQueue()) {
      if (obj.getMessageAuthor().equals(event.getMessageAuthor())) {
        toRemove.add(obj);
      }
    }
    for (ListenObject obj : toRemove) {
      listener.remove(obj);
      event.getChannel().sendMessage("Removed \"" + obj.getQuery() + "\" for " + event.getMessageAuthor().getName());
    }
  }

  @Override
  public String getHelp() {
    return "cleans all objects being watched by you";
  }

  @Override
  public List<String> getUsage() {
    return ImmutableList.of("!pw clean");
  }
}
