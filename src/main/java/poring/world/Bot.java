package poring.world;

import static poring.world.Constants.ADMIN_MAP;
import static poring.world.Constants.ADM_CALL;
import static poring.world.Constants.API;
import static poring.world.Constants.BOT_URL;
import static poring.world.Constants.COMMAND_MAP;
import static poring.world.Constants.ENV;
import static poring.world.Constants.GLOBAL_CALL;
import static poring.world.Constants.IS_PRODUCTION;
import static poring.world.Constants.PARTIES;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import poring.world.market.Command;
import poring.world.party.PartyObject;
import poring.world.watcher.Watcher;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Bot {

  public static void main(String[] args) {
    System.out.println("Starting Golden Thief Bot");
    System.out.println("Environment: " + ENV);

    String token = System.getenv("DISCORD_TOKEN");
    DiscordApiBuilder discordApiBuilder = new DiscordApiBuilder();
    discordApiBuilder.setWaitForServersOnStartup(true);
    DiscordApi api = discordApiBuilder.setToken(token).login().join();

    Map<String, Object> parameters = new HashMap<>();
    parameters.put(API, api);
    parameters.put(PARTIES, new HashMap<Long, TreeMap<String, PartyObject>>());
    parameters.put(BOT_URL, api.createBotInvite());

    Watcher watcher = new Watcher(api);
    if (IS_PRODUCTION) {
      watcher.start();
    }

    api.addMessageCreateListener(getMarketListener(parameters, watcher));
  }

  private static MessageCreateListener getMarketListener(Map<String, Object> parameters, Watcher watcher) {
    return event -> {
      if (event.getMessageAuthor().isBotUser()) {
        return;
      }

      String msg = event.getMessageContent();
      if (msg.toLowerCase().startsWith("!" + ADM_CALL + " ")) {
        runCommand(msg, event, parameters, watcher, ADMIN_MAP);
      }

      if (msg.toLowerCase().startsWith("!" + GLOBAL_CALL + " ")) {
        runCommand(msg, event, parameters, watcher, COMMAND_MAP);
      }
    };
  }

  private static void runCommand(String msg, MessageCreateEvent event, Map<String, Object> parameters,
                                Watcher watcher, Map<String, Command> commands) {
    String[] command = msg.trim().replaceAll(" +", " ").split(" ");

    if (command.length <= 1 || command[1].trim().isEmpty()) {
      event.getChannel().sendMessage("No command for gtb");
    } else if (commands.keySet().contains(command[1].toLowerCase())) {
      commands.get(command[1]).run(command, event, watcher, parameters);
    } else {
      String nearestCommand = Utils.getNearestCommand(command[1].toLowerCase());
      if (nearestCommand != null) {
        event.getChannel().sendMessage(
            String.format("Invalid command **%s**, did you mean **%s**? Running it instead...",
                command[1], nearestCommand));
        commands.get(nearestCommand).run(command, event, watcher, parameters);
      } else {
        event.getChannel().sendMessage(String.format("Invalid command **%s**", command[1]));
      }
    }
  }

}
