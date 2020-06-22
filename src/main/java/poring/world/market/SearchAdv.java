package poring.world.market;

import static poring.world.Constants.Constants.FILTER_TOKEN;
import static poring.world.Constants.Constants.GLOBAL_CALL;
import static poring.world.Constants.Constants.HELP;
import static poring.world.Constants.Constants.QUERY_FILTERS;
import static poring.world.Constants.Constants.SEARCH;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.exception.MissingPermissionsException;
import org.javacord.api.util.logging.ExceptionLogger;
import org.json.simple.JSONArray;
import poring.world.Fetcher;
import poring.world.Utils;
import poring.world.market.filter.FilterUtils;
import poring.world.watcher.WatcherThread;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SearchAdv extends Command {

  private static final int MAX_RESULTS = 10;

  @Override
  public void run(String[] command, MessageCreateEvent event, WatcherThread watcher) {
    Map<String, String> searchFilters = new HashMap<>();
    String query = Utils.getQuery(command);
    if (query.isEmpty()) {
      event.getChannel().sendMessage(
          String.format("No query to search.\nTry _!%s %s %s_\n", GLOBAL_CALL, HELP, SEARCH));
      return;
    }

    if (query.contains(FILTER_TOKEN)) {
      String[] queryFilters = query.split(FILTER_TOKEN);
      query = queryFilters[0].trim();

      for (int i = 1; i < queryFilters.length; i++) {
        String[] keyValue = queryFilters[i].split("=");
        String key = keyValue[0].trim();
        String value = keyValue.length > 1 ? keyValue[1].trim() : "";
        for (String queryFilter : QUERY_FILTERS) {
          if (key.equalsIgnoreCase(queryFilter)) {
            String validate = FilterUtils.validate(key, value);
            if (validate != null) {
              event.getChannel().sendMessage(String.format("Invalid value for _%s_: _%s_ (**%s**)",
                  key, value, validate));
              return;
            }
            searchFilters.put(key, value);
            break;
          }
        }
      }
    }

    JSONArray items = Fetcher.query(query, searchFilters);
    if (items.size() == 0) {
      event.getChannel().sendMessage(String.format("No item found for _%s_ :poop:", query));
      return;
    }

    EmbedBuilder embed = new EmbedBuilder()
        .setTitle(query)
        .setAuthor(event.getMessageAuthor())
        .setColor(Color.ORANGE);
    StringBuilder fieldMsg = new StringBuilder();
    for (Object item : items.subList(0, Math.min(MAX_RESULTS, items.size()))) {
      fieldMsg.append(
          String.format("%s  **%s** for _%s_\n",
              Utils.getItemStock(item),
              Utils.getItemName(item),
              Utils.getItemPrice(item)
          )
      );

      String snap = Utils.getItemSnap(item);
      if (!snap.isEmpty()) {
        fieldMsg.append(String.format("> in snap %s\n", snap));
      }
    }
    embed.addField("Items", fieldMsg.toString());

    if (items.size() > MAX_RESULTS) {
      embed.setFooter("More than 10 items found. Refine your search...");
    }

    event.getChannel().sendMessage(embed).exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
  }

  @Override
  public String getHelp() {
    return "searches for items in poring.world database. Filters available: _maxPrice_, _broken_, _enchant_";
  }

  @Override
  public List<String> getQueries() {
    return new LinkedList<String>(){{
      this.add("morale");
      this.add("morale 4");
      this.add("Eye of Dullahan [1]");
      this.add("+4 Eye of Dullahan");
      this.add("+4 Eye of Dullahan <Sharp Blade 1> (broken)");
      this.add("Eye of Dullahan ::maxPrice=2000000");
      this.add("Eye of Dullahan ::broken=yes");
      this.add("Eye of Dullahan ::broken=no");
      this.add("Eye of Dullahan ::enchant=sharp blade");
      this.add("Eye of Dullahan ::enchant=sharp blade 1");
    }};
  }

}
