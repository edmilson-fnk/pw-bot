package poring.world.market;

import static poring.world.Constants.FILTER_TOKEN;
import static poring.world.Constants.QUERY_FILTERS;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.general.Command;
import poring.world.market.filter.FilterUtils;
import poring.world.watcher.Watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Watch extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    String query = Utils.getQuery(command);
    StringBuilder filtersStr = new StringBuilder();
    Map<String, String> filters = new HashMap<>();

    if (query.contains(FILTER_TOKEN)) {
      String[] queryFilters = query.split(FILTER_TOKEN);
      query = queryFilters[0].trim();

      filtersStr.append("Filters: ");
      for (int i = 1; i < queryFilters.length; i++) {
        String[] keyValue = queryFilters[i].split("=");
        String key = keyValue[0].trim();
        String value = keyValue[1].trim();
        for (String queryFilter : QUERY_FILTERS) {
          if (key.equalsIgnoreCase(queryFilter)) {
            String validate = FilterUtils.validate(key, value);
            if (validate != null) {
              event.getChannel().sendMessage(String.format("Invalid value for _%s_: _%s_ (**%s**)",
                  key, value, validate));
              return;
            }
            filtersStr.append(FilterUtils.translate(key, value));
            filters.put(key, value);
            break;
          }
        }
      }
    }

    if (query.isEmpty()) {
      event.getChannel().sendMessage("No query to watch");
      return;
    }

    TextChannel channel = event.getChannel();
    MessageAuthor messageAuthor = event.getMessageAuthor();

    watcher.add(query, messageAuthor, channel, filters);
    event.getChannel().sendMessage(String.format("GTB is watching _%s_ for _%s_. %s",
        query, messageAuthor.getDisplayName(), filtersStr.toString()));
  }

  @Override
  public String getHelp() {
    return "hourly checks poring.world and privately alerts user if search query is found. " +
        "Filter available: _maxPrice_, see filter example";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of(
        "morale",
        "morale 4",
        "Eye of Dullahan [1]",
        "+4 Eye of Dullahan",
        "+4 Eye of Dullahan <Sharp Blade 1> (broken)",
        "Eye of Dullahan ::maxPrice=2000000"
    );
  }

}
