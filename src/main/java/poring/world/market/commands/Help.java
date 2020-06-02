package poring.world.market.commands;

import static poring.world.Constants.Constants.COMMAND_MAP;
import static poring.world.Constants.Constants.GLOBAL_CALL;

import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Help extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    String query = Utils.getQuery(command);
    Set<String> helpMap = new HashSet<>();
    boolean addQueries = true;
    if (query.isEmpty()) {
      helpMap.addAll(COMMAND_MAP.keySet());
      addQueries = false;
    } else if (COMMAND_MAP.containsKey(query)) {
      helpMap.add(query);
    } else {
      event.getChannel().sendMessage("Invalid command: **" + query + "**");
      return;
    }

    StringBuilder helpMessage = new StringBuilder();
    helpMessage.append(":question: **GTB help**\n");
    for (String key : helpMap) {
      helpMessage.append(String.format("  _%s_   %s\n", key, COMMAND_MAP.get(key).getHelp()));
      if (addQueries) {
        for (String commandQuery : COMMAND_MAP.get(key).getQueries()) {
          helpMessage.append(String.format("       - !%s %s %s\n", GLOBAL_CALL, key, commandQuery));
        }
      }
    }

    event.getChannel().sendMessage(helpMessage.toString());
  }

  @Override
  public String getHelp() {
    return "shows bot options";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of(
        "",
        "search"
    );
  }

}