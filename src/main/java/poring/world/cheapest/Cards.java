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
      sb.append(String.format("(_%s_)\n", Utils.capitalize(CARD_COLOR_NAME.get(color))));
      if (cards.containsKey(color + "snap")) {
        sb.append("\t\t");
        sb.append(Utils.getItemMessage((JSONObject) cards.get(color + "snap")));
        sb.append("\n");
      }
      if (cards.containsKey(color + "nosnap")) {
        sb.append("\t\t");
        sb.append(Utils.getItemMessage((JSONObject) cards.get(color + "nosnap")));
        sb.append("\n");
      }
    }

    channel.sendMessage(sb.toString());
//    System.out.println(sb.toString());
  }

  @Override
  public String getHelp() {
    return "lists cheapest cards available on market for each of selected colors or every color";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("", "white", "green", "blue", "w", "g", "b");
  }

  public static void main(String[] args) {
    String[] command = new String[]{"!gtb", "cards", "w"};
    new Cards().run(command, null, null, null);
  }
}
