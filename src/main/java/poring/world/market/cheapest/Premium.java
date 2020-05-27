package poring.world.market.cheapest;

import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import org.json.simple.JSONObject;
import poring.world.Fetcher;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.List;

public class Premium extends Command {
  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    JSONObject premiums = Fetcher.getCheapestPremiums();
    StringBuilder sb = new StringBuilder();
    sb.append("**Cheapest premiums**\n");
    if (premiums != null) {
      sb.append(Utils.getItemMessage(premiums));
      sb.append("\n");
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
