package poring.world.market.commands;

import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.List;

public class About extends Command {
  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    event.getChannel().sendMessage(
        "> **G**olden **T**hief **B**ot\n> Discord bot developed by _Ved_\n> \n> Many thanks to poring.world API"
    );
  }

  @Override
  public String getHelp() {
    return "shows some information about GTB";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of();
  }
}
