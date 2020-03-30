package poring.world.command;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.listen.ListenObject;
import poring.world.listen.Listener;

import java.util.LinkedList;
import java.util.List;

public class CleanCommand extends Command {
  @Override
  public void run(String[] command, MessageCreateEvent event, Listener listener) {
    List<ListenObject> toRemove = new LinkedList<>();
    MessageAuthor messageAuthor = event.getMessageAuthor();

    if (listener.getMap().containsKey(messageAuthor)) {
      listener.getMap().get(messageAuthor).clear();
    }

    deprecatedQueueRemove(event, listener, toRemove, messageAuthor);
  }

  private void deprecatedQueueRemove(MessageCreateEvent event, Listener listener, List<ListenObject> toRemove,
                                     MessageAuthor messageAuthor) {
    for (ListenObject obj : listener.getQueue()) {
      if (obj.getMessageAuthor().equals(messageAuthor)) {
        toRemove.add(obj);
      }
    }
    for (ListenObject obj : toRemove) {
      listener.remove(obj);
      event.getChannel().sendMessage("Removed \"_" + obj.getQuery() + "_\" for " + messageAuthor.getName());
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
