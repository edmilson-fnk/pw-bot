package poring.world.market.commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.market.filter.FilterUtils;
import poring.world.watcher.Watcher;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static poring.world.Constants.Constants.*;

public class Watch extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    String query = Utils.getQuery(command);
    Map<String, String> filters = new HashMap<>();

    event.getMessage().addReaction(GLASSES);

    if (query.contains(FILTER_TOKEN)) {
      String[] queryFilters = query.split(FILTER_TOKEN);
      query = queryFilters[0].trim();

      for (int i = 1; i < queryFilters.length; i++) {
        String[] keyValue = queryFilters[i].split("=");
        String key = keyValue[0].trim().toLowerCase();
        String value = keyValue.length > 1 ? keyValue[1].trim() : "";
        for (String queryFilter : QUERY_FILTERS) {
          if (key.equalsIgnoreCase(queryFilter)) {
            String validate = FilterUtils.validate(key, value);
            if (validate != null) {
              event.getChannel().sendMessage(String.format("Invalid value for _%s_: _%s_ (**%s**)",
                  key, value, validate));
              return;
            }
            filters.put(key, value);
            break;
          }
        }
      }
    }

    if (query.isEmpty()) {
      event.getMessage().addReaction(X);
      event.getChannel().sendMessage("No query to watch");
      return;
    }

    TextChannel channel = event.getChannel();
    MessageAuthor messageAuthor = event.getMessageAuthor();

    String msg = watcher.getWatcherThread().add(query, messageAuthor, channel, filters);
    event.getChannel().sendMessage(msg);
    event.getMessage().addReaction(CHECK);
  }

  @Override
  public String getHelp() {
    return "hourly checks poring.world and privately alerts user if search query is found. " +
        "Filters available: _maxPrice_, _broken_, _enchant_, _except_, _refine_, _slots_";
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
      this.add("Eye of Dullahan ::broken=no");
      this.add("Eye of Dullahan ::broken=yes");
      this.add("Eye of Dullahan ::enchant=sharp blade");
      this.add("Eye of Dullahan ::enchant=sharp blade 1");
      this.add("Majestic Goat ::except=blueprint");
      this.add("Majestic Goat ::except=+10");
      this.add("Majestic Goat ::refine>=5");
      this.add("Majestic Goat ::refine<=3");
      this.add("Majestic Goat ::slots=1");
    }};
  }

}
