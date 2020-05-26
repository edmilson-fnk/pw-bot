package poring.world.adm;

import static poring.world.Constants.BOT_URL;

import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Map;

public class URL extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    if (parameters.containsKey(BOT_URL)) {
      event.getChannel().sendMessage("Invite me to your channel! Click " + parameters.get(BOT_URL));
    } else {
      event.getChannel().sendMessage("Sorry, no support for URL yet!");
    }
  }

  @Override
  public String getHelp() {
    return "shows the URL to invite GTB to your channel";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("");
  }
}
