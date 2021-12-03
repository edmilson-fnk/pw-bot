package poring.world.market.commands;

import org.javacord.api.event.message.MessageCreateEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import poring.world.Fetcher;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.market.filter.FilterUtils;
import poring.world.watcher.Watcher;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static poring.world.constants.Constants.*;

public class Search extends Command {

  private static final int MAX_RESULTS = 10;

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    Map<String, String> searchFilters = new HashMap<>();
    String query = Utils.getQuery(command);
    StringBuilder sb = new StringBuilder();
    if (query.isEmpty()) {
      sb.append(String.format("No query to search.\nTry _!%s %s %s_\n", GLOBAL_CALL, HELP, SEARCH));
      event.getMessage().addReaction(X);
      return;
    }

    event.getMessage().addReaction(MAGNIFIER);

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

    List<StringBuilder> msgs = new LinkedList<>();
    msgs.add(sb);
    List<String> queryNames = Utils.getNames(query);
    if (queryNames.size() > MAXIMUM_NAMES) {
      event.getChannel().sendMessage(String.format("Maximum of %d items to search", MAXIMUM_NAMES));
      event.getMessage().addReaction(X);
      return;
    }

    Fetcher fetcher = new Fetcher();
    for (String name : queryNames) {
      sb = new StringBuilder();
      JSONArray items = fetcher.query(name, searchFilters);
      if (items.size() == 0) {
        sb.append(String.format("No item found for _%s_ :poop:\n", name));
      }
      for (Object item : items.subList(0, Math.min(MAX_RESULTS, items.size()))) {
        sb.append(String.format("%s\n", Utils.getItemMessage((JSONObject) item)));
      }
      if (items.size() > MAX_RESULTS) {
        sb.append("More than 10 items found. Refine your search...");
      }
      msgs.add(sb);
    }

    msgs.forEach(s -> event.getChannel().sendMessage(s.toString()));
    event.getMessage().addReaction(CHECK);
  }

  @Override
  public String getHelp() {
    return "searches for items in poring.world database. Filters available: _maxPrice_, _broken_, _enchant_, " +
        "_except_, _slots_, _category_ (" + String.join(", ", CATEGORY_MAP.keySet()) + ")";
  }

  @Override
  public List<String> getQueries() {
    return new LinkedList<String>(){{
      this.add("morale");
      this.add("morale && sharp blade");
      this.add("morale 4");
      this.add("Eye of Dullahan [1]");
      this.add("+4 Eye of Dullahan");
      this.add("+4 Eye of Dullahan <Sharp Blade 1> (broken)");
      this.add("Eye of Dullahan ::maxPrice=2000000");
      this.add("Eye of Dullahan ::broken=yes");
      this.add("Eye of Dullahan ::broken=no");
      this.add("Eye of Dullahan ::enchant=sharp blade 1");
      this.add("Eye of Dullahan ::enchant=sharp blade&&morale");
      this.add("Majestic Goat ::except=blueprint");
      this.add("Majestic Goat ::except=+10");
      this.add("Majestic Goat ::except=blueprint&&+10");
      this.add("Majestic Goat && Coif ::except=blueprint");
      this.add("Majestic Goat ::refine>=5");
      this.add("Majestic Goat ::refine<=3");
      this.add("Majestic Goat ::slots=1");
      this.add("Morale 4 ::category=armor");
    }};
  }

}
