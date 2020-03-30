package poring.world.command;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import poring.world.Fetcher;
import poring.world.Utils;
import poring.world.listen.Listener;

import java.util.List;

public class SearchCommand extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Listener listener) {
    String query = Utils.getQuery(command);
    TextChannel channel = event.getChannel();
    if (query.isEmpty()) {
      channel.sendMessage("No query to search.\nTry _!pw help search_");
    }
    JSONArray itens = Fetcher.query(query);
    if (itens.size() == 0) {
      channel.sendMessage("No item found for \"" + query + "\"");
    }
    for (Object item : itens) {
      channel.sendMessage(Utils.getItemMessage((JSONObject) item));
    }
  }

  @Override
  public String getHelp() {
    return "searches for items in poring.world database";
  }

  @Override
  public List<String> getUsage() {
    return ImmutableList.of(
        "!pw search morale",
        "!pw search morale 4",
        "!pw search Eye of Dullahan [1]",
        "!pw search +4 Eye of Dullahan",
        "!pw search +4 Eye of Dullahan <Sharp Blade 1> (broken)"
    );
  }
}
