package poring.world.party;

import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.general.Command;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Map;

public class Join extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    event.getChannel().sendMessage("to be done");
  }

  @Override
  public String getHelp() {
    return "";
  }

  @Override
  public List<String> getQueries() {
    return null;
  }

}
