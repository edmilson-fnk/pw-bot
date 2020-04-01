package poring.world;

import static poring.world.Constants.CALL;
import static poring.world.Constants.COMMAND_MAP;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import poring.world.market.Validator;
import poring.world.watcher.Watcher;

import java.util.HashMap;
import java.util.Map;

public class Bot {

  public static void main(String[] args) {
    String token = System.getenv("DISCORD_TOKEN");
    DiscordApiBuilder discordApiBuilder = new DiscordApiBuilder();
    discordApiBuilder.setWaitForServersOnStartup(true);
    DiscordApi api = discordApiBuilder.setToken(token).login().join();

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("bot_url", api.createBotInvite());

    Watcher watcher = new Watcher(api);
    watcher.start();

    api.addMessageCreateListener(marketEvent -> {
      if (marketEvent.getMessageAuthor().isBotUser()) {
        return;
      }
      String msg = marketEvent.getMessageContent();
      if (msg.toLowerCase().startsWith("!" + CALL)) {
        String[] command = msg.split(" ");

        if (!Validator.isValidCommand(command)) {
          marketEvent.getChannel().sendMessage("Invalid command: **" + command[1] + "**");
          return;
        }

        if (COMMAND_MAP.keySet().contains(command[1].toLowerCase())) {
          COMMAND_MAP.get(command[1]).run(command, marketEvent, watcher, parameters);
        }
      }
    });
  }

}
