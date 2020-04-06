package poring.world.cheapest;

import static poring.world.Constants.CARD_COLOR;
import static poring.world.Constants.CARD_COLOR_NAME;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.json.simple.JSONObject;
import poring.world.Fetcher;
import poring.world.Utils;
import poring.world.general.Command;
import poring.world.watcher.Watcher;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Cards extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    String query = Utils.getQuery(command);
    Set<String> colors = new HashSet<>();
    TextChannel channel = event.getChannel();
    if (query.isEmpty()) {
      colors.addAll(CARD_COLOR.values());
    } else if (CARD_COLOR.keySet().contains(query.toLowerCase())) {
      colors.add(CARD_COLOR.get(query));
    } else {
      channel.sendMessage("Invalid color: **" + query + "**");
      return;
    }

    JSONObject cards = Fetcher.getCheapestCards(colors);
    StringBuilder sb = new StringBuilder();
    sb.append("**Cheapest cards**\n");
    for (String color : colors) {
      String snapKey = color + "snap";
      String noSnapKey = color + "nosnap";
      sb.append(String.format("(_%s_)\n", Utils.capitalize(CARD_COLOR_NAME.get(color))));
      if (cards.containsKey(snapKey) && cards.containsKey(noSnapKey)) {
        long snapPrice =
            Long.parseLong(((JSONObject) ((JSONObject) cards.get(snapKey)).get("lastRecord")).get("price").toString());
        long noSnapPrice =
            Long.parseLong(((JSONObject) ((JSONObject) cards.get(noSnapKey)).get("lastRecord")).get("price").toString());
        if (snapPrice > noSnapPrice) {
          cards.remove(snapKey);
        }
      }

      if (cards.containsKey(snapKey)) {
        sb.append("\t\t");
        sb.append(Utils.getItemMessage((JSONObject) cards.get(snapKey)));
        sb.append("\n");
      }
      if (cards.containsKey(noSnapKey)) {
        sb.append("\t\t");
        sb.append(Utils.getItemMessage((JSONObject) cards.get(noSnapKey)));
        sb.append("\n");
      }
    }

    channel.sendMessage(sb.toString());
  }

  @Override
  public String getHelp() {
    return "lists cheapest cards available on market for each of selected colors or every color";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("", "white", "green", "blue", "w", "g", "b");
  }

  // for tests
  public static void main(String[] args) {
    String[] command = new String[]{"!gtb", "cards", "b"};
    new Cards().run(command, null, null, null);
  }
}
