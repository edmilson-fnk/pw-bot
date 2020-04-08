package poring.world;

import static poring.world.Constants.BOT_URL;
import static poring.world.Constants.COMMAND_MAP;
import static poring.world.Constants.GLOBAL_CALL;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.listener.message.MessageCreateListener;
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
    parameters.put(BOT_URL, api.createBotInvite());

    Watcher watcher = new Watcher(api);
    watcher.start();

    api.addMessageCreateListener(getMarketListener(parameters, watcher));
  }

  private static MessageCreateListener getMarketListener(Map<String, Object> parameters, Watcher watcher) {
    return event -> {
      if (event.getMessageAuthor().isBotUser()) {
        return;
      }

      String msg = event.getMessageContent();
      if (msg.toLowerCase().startsWith("!" + GLOBAL_CALL + " ")) {
        String[] command = msg.split(" ");

        if (COMMAND_MAP.keySet().contains(command[1].toLowerCase())) {
          COMMAND_MAP.get(command[1]).run(command, event, watcher, parameters);
        } else {
          event.getChannel().sendMessage("Invalid command: **" + command[1] + "**");
        }
      }
    };
  }

}
