package poring.world.market.cheapest;

import static poring.world.Constants.Constants.CARD_COLOR;
import static poring.world.Constants.Constants.CARD_COLOR_NAME;
import static poring.world.Constants.Constants.COLOR_DUST;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.exception.MissingPermissionsException;
import org.javacord.api.util.logging.ExceptionLogger;
import org.json.simple.JSONObject;
import poring.world.Fetcher;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cards extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    String query = Utils.getQuery(command);
    Set<String> colors = new HashSet<>();
    TextChannel channel = event.getChannel();
    if (query.isEmpty()) {
      colors.addAll(CARD_COLOR.values());
    } else if (CARD_COLOR.keySet().contains(query.toLowerCase())) {
      colors.add(CARD_COLOR.get(query));
    } else {
      channel.sendMessage(String.format("Invalid color: **%s**", query));
      return;
    }

    JSONObject cards = Fetcher.getCheapestCards(colors);
    EmbedBuilder embed = new EmbedBuilder()
        .setTitle("Cheapest Cards :black_joker:")
        .setColor(Color.ORANGE);
    for (String color : colors) {
      String snapKey = color + "snap";
      String noSnapKey = color + "nosnap";
      String fieldName = Utils.capitalize(CARD_COLOR_NAME.get(color));
      if (cards.containsKey(snapKey) && cards.containsKey(noSnapKey)) {
        long snapPrice =
            Long.parseLong(((JSONObject) ((JSONObject) cards.get(snapKey)).get("lastRecord")).get("price").toString());
        long noSnapPrice =
            Long.parseLong(((JSONObject) ((JSONObject) cards.get(noSnapKey)).get("lastRecord")).get("price").toString());
        if (snapPrice > noSnapPrice) {
          cards.remove(snapKey);
        }
      }

      StringBuilder content = new StringBuilder();
      if (cards.containsKey(snapKey)) {
        JSONObject card = (JSONObject) cards.get(snapKey);
        double perDust = ((long) ((JSONObject) card.get("lastRecord")).get("price")) / COLOR_DUST.get(color);
        content.append(String.format("\t\t%s_ (%s /dust)_\n", Utils.getItemMessage(card),
            Utils.priceWithoutDecimal(perDust)));
      }
      if (cards.containsKey(noSnapKey)) {
        JSONObject card = (JSONObject) cards.get(noSnapKey);
        double perDust = ((long) ((JSONObject) card.get("lastRecord")).get("price")) / COLOR_DUST.get(color);
        content.append(String.format("\t\t%s_ (%s /dust)_\n", Utils.getItemMessage(card),
            Utils.priceWithoutDecimal(perDust)));
      }

      embed.addField(fieldName, content.toString());
    }

//    channel.sendMessage(embed)
//        .exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
    channel.sendMessage(embed);
  }

  @Override
  public String getHelp() {
    return "lists cheapest cards available on market for each of selected colors or every color";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("", "white", "green", "blue", "w", "g", "b");
  }

}
