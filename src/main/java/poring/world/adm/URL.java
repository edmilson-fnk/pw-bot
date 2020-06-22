package poring.world.adm;

import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.List;

public class URL extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    event.getChannel().sendMessage(String.format("Invite me to your channel! Click %s",
        event.getApi().createBotInvite()));
  }

  @Override
  public String getHelp() {
    return "shows an URL to invite GTB to your channel";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("");
  }
}
