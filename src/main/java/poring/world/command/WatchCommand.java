package poring.world.command;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.listen.Watcher;

import java.util.List;

public class WatchCommand extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    String query = Utils.getQuery(command);
    TextChannel channel = event.getChannel();
    MessageAuthor messageAuthor = event.getMessageAuthor();

    watcher.add(query, messageAuthor, channel);
    event.getChannel().sendMessage("Watcher event added for \"_" + query + "_\"");
  }

  @Override
  public String getHelp() {
    return "constantly checks poring.world and alerts user if search query is found";
  }

  @Override
  public List<String> getUsage() {
    return ImmutableList.of(
        "!pw watch morale",
        "!pw watch morale 4",
        "!pw watch Eye of Dullahan [1]",
        "!pw watch +4 Eye of Dullahan",
        "!pw watch +4 Eye of Dullahan <Sharp Blade 1> (broken)"
    );
  }

}
