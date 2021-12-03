package poring.world.market.cheapest;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.json.simple.JSONObject;
import poring.world.Fetcher;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static poring.world.constants.Constants.*;

public class Cards extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    String query = Utils.getQuery(command);
    Set<String> colors = new HashSet<>();
    TextChannel channel = event.getChannel();
    if (query.isEmpty()) {
      colors.addAll(CARD_COLOR.values());
    } else if (CARD_COLOR.containsKey(query)) {
      colors.add(CARD_COLOR.get(query));
    } else {
      channel.sendMessage(String.format("Invalid color: **%s**", query));
      event.getMessage().addReaction(X);
      return;
    }

    event.getMessage().addReaction(CARD);

    JSONObject cards = new Fetcher().getCheapestCards(colors);
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
        appendDustData(cards, color, snapKey, content);
      }
      if (cards.containsKey(noSnapKey)) {
        appendDustData(cards, color, noSnapKey, content);
      }

      embed.addField(fieldName, content.toString());
    }

    channel.sendMessage(embed);
    event.getMessage().addReaction(CHECK);
  }

  private void appendDustData(JSONObject cards, String color, String key, StringBuilder content) {
    JSONObject card = (JSONObject) cards.get(key);
    double perDust = ((long) ((JSONObject) card.get("lastRecord")).get("price")) / COLOR_DUST.get(color);
    content.append(String.format("\t\t%s_ (%s /dust)_\n", Utils.getItemMessage(card),
            Utils.priceWithoutDecimal(perDust)));
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
