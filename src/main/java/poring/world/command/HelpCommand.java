package poring.world.command;

import static poring.world.Constants.COMMAND_MAP;

import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.watcher.Watcher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HelpCommand extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    String query = Utils.getQuery(command);
    Set<String> helpMap = new HashSet<>();
    if (query.isEmpty()) {
      helpMap.addAll(COMMAND_MAP.keySet());
    } else if (COMMAND_MAP.containsKey(query)) {
      helpMap.add(query);
    } else {
      event.getChannel().sendMessage("Invalid command: **" + query + "**");
      return;
    }

    StringBuilder helpMessage = new StringBuilder();
    helpMessage.append("**Commands**\n");
    for (String key : helpMap) {
      helpMessage.append("  _");
      helpMessage.append(key);
      helpMessage.append("_ ");
      helpMessage.append(COMMAND_MAP.get(key).getHelp());
      helpMessage.append("\n");
      for (String usage : COMMAND_MAP.get(key).getUsage()) {
        helpMessage.append("       - ");
        helpMessage.append(usage);
        helpMessage.append("\n");
      }
    }
//    helpMessage.append("\nAdd me to your channel: ");
//    helpMessage.append(parameters.get("bot_url").toString());

    event.getChannel().sendMessage(helpMessage.toString());
  }

  @Override
  public String getHelp() {
    return "shows bot options";
  }

  @Override
  public List<String> getUsage() {
    return ImmutableList.of(
        "!pw help",
        "!pw help search"
    );
  }

}
