package poring.world.market;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Map;

public class WatchCommand extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    String query = Utils.getQuery(command);
    TextChannel channel = event.getChannel();
    MessageAuthor messageAuthor = event.getMessageAuthor();

    watcher.add(query, messageAuthor, channel);
    event.getChannel().sendMessage("Watcher event added for \"_" + query + "_\"");
  }

  @Override
  public String getHelp() {
    return "constantly checks poring.world and privately alerts user if search query is found";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of(
        "morale",
        "morale 4",
        "Eye of Dullahan [1]",
        "+4 Eye of Dullahan",
        "+4 Eye of Dullahan <Sharp Blade 1> (broken)"
    );
  }

}
