package poring.world.cards;

import static poring.world.Constants.CARD_COLOR;

import org.javacord.api.event.message.MessageCreateEvent;
import org.json.simple.JSONObject;
import poring.world.Fetcher;
import poring.world.Utils;
import poring.world.general.Command;
import poring.world.watcher.Watcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Cheapest extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    String query = Utils.getQuery(command);
    List<String> colors = new LinkedList<>();
    if (query.isEmpty()) {
      colors.addAll(CARD_COLOR.values());
    } else if (CARD_COLOR.values().contains(query)) {
      colors.add(CARD_COLOR.get(query));
    } else {
      event.getChannel().sendMessage("Invalid color: **" + query + "**");
    }

    JSONObject cards = Fetcher.getCheapestCards(colors);
    StringBuilder sb = new StringBuilder();
    sb.append("**Cheapest cards right now**\n");
    for (Object color : cards.keySet()) {
      sb.append(color.toString());
      sb.append(": ");
      sb.append(Utils.getItemMessage((JSONObject) cards.get(color)));
      sb.append("\n");
    }
    event.getChannel().sendMessage(sb.toString());
  }

  @Override
  public String getHelp() {
    return "lists cheapest card for each of selected colors or every color";
  }

  @Override
  public List<String> getQueries() {
    return null;
  }
}
