package poring.world.market;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.watcher.Watcher;

import java.util.List;

public class MVPCards extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    TextChannel channel = event.getChannel();
    channel.sendMessage(String.format("Watching MVP card list for <@%s>", channel.getId()));
  }

  @Override
  public String getHelp() {
    return "watches mvp cards on market and notifies if found";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of();
  }

}
