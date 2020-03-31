package poring.world.command;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import poring.world.Fetcher;
import poring.world.Utils;
import poring.world.listen.Watcher;

import java.util.List;

public class SearchCommand extends Command {

  private static final int MAX_RESULTS = 10;

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    String query = Utils.getQuery(command);
    TextChannel channel = event.getChannel();
    StringBuilder sb = new StringBuilder();
    if (query.isEmpty()) {
      sb.append("No query to search.\nTry _!pw help search_\n");
    }
    JSONArray itens = Fetcher.query(query);
    if (itens.size() == 0) {
      sb.append("No item found for \"_");
      sb.append(query);
      sb.append("_\"\n");
    }
    for (Object item : itens.subList(0, Math.min(MAX_RESULTS, itens.size()))) {
      channel.sendMessage();
      sb.append(Utils.getItemMessage((JSONObject) item));
      sb.append("\n");
    }
    if (itens.size() > MAX_RESULTS) {
      sb.append("More than 10 items found. Refine your search...");
    }

    channel.sendMessage(sb.toString());
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
