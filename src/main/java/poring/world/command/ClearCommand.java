package poring.world.command;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.List;

public class ClearCommand extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    MessageAuthor messageAuthor = event.getMessageAuthor();

    if (watcher.getMap().containsKey(messageAuthor.getId())) {
      List<WatchObject> authorList = watcher.getMap().get(messageAuthor.getId());
      event.getChannel().sendMessage("Removed " + authorList.size() + " item(s) for " + messageAuthor.getName());
      authorList.clear();
      watcher.saveMap();
    }
  }

  @Override
  public String getHelp() {
    return "removes every object being watched by you";
  }

  @Override
  public List<String> getUsage() {
    return ImmutableList.of("!pw clear");
  }

}
