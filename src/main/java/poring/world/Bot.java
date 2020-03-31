package poring.world;

import static poring.world.Constants.COMMAND_MAP;
import static poring.world.Constants.SEARCH;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import poring.world.command.Validator;
import poring.world.watcher.Watcher;

public class Bot {

  public static void main(String[] args) {
    String token = System.getenv("DISCORD_TOKEN");
    DiscordApiBuilder discordApiBuilder = new DiscordApiBuilder();
    discordApiBuilder.setWaitForServersOnStartup(true);
    DiscordApi api = discordApiBuilder.setToken(token).login().join();

    COMMAND_MAP.get(SEARCH).parameters.put("bot_url", api.createBotInvite());

    Watcher watcher = new Watcher(api);
    watcher.start();

    api.addMessageCreateListener(event -> {
      if (event.getMessageAuthor().isBotUser()) {
        event.getChannel().sendMessage("Bot user _" + event.getMessageAuthor().getName() + "_");
        return;
      }
      String msg = event.getMessageContent();
      if (msg.toLowerCase().startsWith("!poring-world") || msg.toLowerCase().startsWith("!pw")) {
        String[] command = msg.split(" ");

        if (!Validator.isValidCommand(command)) {
          event.getChannel().sendMessage("Invalid command: **" + command[1] + "**");
          return;
        }

        if (COMMAND_MAP.keySet().contains(command[1].toLowerCase())) {
          COMMAND_MAP.get(command[1]).run(command, event, watcher);
        }
      }
    });
  }

}
