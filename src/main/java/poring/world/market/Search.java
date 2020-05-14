package poring.world.market;

import static poring.world.Constants.GLOBAL_CALL;
import static poring.world.Constants.FILTER_TOKEN;
import static poring.world.Constants.HELP;
import static poring.world.Constants.QUERY_FILTERS;
import static poring.world.Constants.SEARCH;

import org.javacord.api.event.message.MessageCreateEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import poring.world.Fetcher;
import poring.world.Utils;
import poring.world.general.Command;
import poring.world.market.filter.FilterUtils;
import poring.world.watcher.Watcher;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Search extends Command {

  private static final int MAX_RESULTS = 10;

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    Map<String, String> searchFilters = new HashMap<>();
    String query = Utils.getQuery(command);
    StringBuilder sb = new StringBuilder();
    if (query.isEmpty()) {
      sb.append(String.format("No query to search.\nTry _!%s %s %s_\n", GLOBAL_CALL, HELP, SEARCH));
    }

    if (query.contains(FILTER_TOKEN)) {
      String[] queryFilters = query.split(FILTER_TOKEN);
      query = queryFilters[0].trim();

      for (int i = 1; i < queryFilters.length; i++) {
        String[] keyValue = queryFilters[i].split("=");
        String key = keyValue[0];
        String value = keyValue[1];
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

    JSONArray itens = Fetcher.query(query, searchFilters);
    if (itens.size() == 0) {
      sb.append(String.format("No item found for _%s_ :poop:", query));
    }
    for (Object item : itens.subList(0, Math.min(MAX_RESULTS, itens.size()))) {
      sb.append(String.format("%s\n", Utils.getItemMessage((JSONObject) item)));
    }
    if (itens.size() > MAX_RESULTS) {
      sb.append("More than 10 items found. Refine your search...");
    }

    event.getChannel().sendMessage(sb.toString());
  }

  @Override
  public String getHelp() {
    return "searches for items in poring.world database. Filters available: _maxPrice_, _broken_, see filter example";
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
    }};
  }

}
