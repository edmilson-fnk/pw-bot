package poring.world.market.cheapest;

import com.google.common.collect.ImmutableList;
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
import java.util.List;

public class Premium extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    EmbedBuilder embed = new EmbedBuilder().setTitle("Cheapest premiums").setColor(Color.ORANGE);
    JSONObject premiums = watcher.getFetcher().getCheapestPremiums();
    if (premiums != null) {
      embed.addField("Premium", Utils.getItemMessage(premiums));
    } else {
      embed.addField(">", "No premium found");
    }
    event.getChannel().sendMessage(embed).exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
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
