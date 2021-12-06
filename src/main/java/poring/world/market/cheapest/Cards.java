package poring.world.market.cheapest;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static poring.world.constants.Constants.*;

public class Cards extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    List<String> query = Arrays.asList(Utils.getQuery(command).split(" "));
    Set<String> colors = query.stream()
            .filter(c -> CARD_COLOR.containsKey(c.toLowerCase()))
            .map(c -> CARD_COLOR.get(c.toLowerCase()))
            .collect(Collectors.toSet());
    if (colors.isEmpty()) {
      colors.addAll(CARD_COLOR.values());
    }

    boolean snap = query.stream().anyMatch(c -> c.equalsIgnoreCase("snap"));

    TextChannel channel = event.getChannel();
    event.getMessage().addReaction(CARD);

    JSONObject cards = watcher.getFetcher().getCheapestCards(colors);
    EmbedBuilder embed = new EmbedBuilder()
        .setTitle("Cheapest Cards :black_joker:")
        .setColor(Color.ORANGE);
    for (String color : colors) {
      String snapKey = color + "snap";
      String noSnapKey = color + "nosnap";
      String fieldName = Utils.capitalize(CARD_COLOR_NAME.get(color));

      StringBuilder content = new StringBuilder();
      if (cards.containsKey(snapKey) && snap) {
        for (Object thisCard : ((JSONArray) cards.get(snapKey))) {
          JSONObject thisCardJson = (JSONObject) thisCard;
          appendDustData(thisCardJson, color, content);
        }
      }
      if (cards.containsKey(noSnapKey) && !snap) {
        for (Object thisCard : ((JSONArray) cards.get(noSnapKey))) {
          JSONObject thisCardJson = (JSONObject) thisCard;
          appendDustData(thisCardJson, color, content);
        }
      }

      embed.addField(fieldName, content.toString());
    }

    channel.sendMessage(embed);
    event.getMessage().addReaction(CHECK);
  }

  private void appendDustData(JSONObject card, String color, StringBuilder content) {
    double perDust = ((long) ((JSONObject) card.get("lastRecord")).get("price")) / COLOR_DUST.get(color);
    content.append(String.format("\t\t%s_ (%s /dust)_\n", Utils.getItemMessage(card),
            Utils.priceWithoutDecimal(perDust)));
  }


  @Override
  public String getHelp() {
    return "lists cheapest cards available on market for each of selected colors or every color. Use snap option to see cards in snap";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("", "white", "green", "blue", "w", "g", "b", "snap", "g snap");
  }

}
