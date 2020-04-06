package poring.world.cheapest;

import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import org.json.simple.JSONObject;
import poring.world.Fetcher;
import poring.world.Utils;
import poring.world.general.Command;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Map;

public class Premium extends Command {
  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    JSONObject premiums = Fetcher.getCheapestPremiums();
    StringBuilder sb = new StringBuilder();
    sb.append("**Cheapest premiums**\n");
    if (premiums != null) {
      String itemMessage = Utils.getItemMessage(premiums);
      if (itemMessage != null) {
        sb.append(itemMessage);
        sb.append("\n");
      } else {
        sb.append("No premium found");
      }
    } else {
      sb.append("No premium found");
    }
    event.getChannel().sendMessage(sb.toString());
  }

  @Override
  public String getHelp() {
    return "lists cheapest premiums available on market";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("");
  }
}
