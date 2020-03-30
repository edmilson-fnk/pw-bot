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
    MessageAuthor messageAuthor = event.getMessageAuthor();

    if (listener.getMap().containsKey(messageAuthor)) {
      List<ListenObject> authorList = listener.getMap().get(messageAuthor);
      event.getChannel().sendMessage("Removed " + authorList.size() + " item(s) for " + messageAuthor.getName());
      authorList.clear();
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
