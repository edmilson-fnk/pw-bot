package poring.world;

import static poring.world.Constants.CLEAN;
import static poring.world.Constants.HELP;
import static poring.world.Constants.LIST;
import static poring.world.Constants.REMOVE;
import static poring.world.Constants.SEARCH;
import static poring.world.Constants.WATCH;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import poring.world.command.CleanCommand;
import poring.world.command.Command;
import poring.world.command.HelpCommand;
import poring.world.command.ListCommand;
import poring.world.command.RemoveCommand;
import poring.world.command.SearchCommand;
import poring.world.command.Validator;
import poring.world.command.WatchCommand;
import poring.world.listen.Watcher;

import java.util.HashMap;

public class Bot {

  public static HashMap<String, Command> COMMAND_MAP = new HashMap<String, Command>(){{
    this.put(HELP, new HelpCommand());
    this.put(SEARCH, new SearchCommand());
    this.put(WATCH, new WatchCommand());
    this.put(CLEAN, new CleanCommand());
    this.put(LIST, new ListCommand());
    this.put(REMOVE, new RemoveCommand());
  }};

  public static void main(String[] args) {
    String token = System.getenv("DISCORD_TOKEN");
    DiscordApiBuilder discordApiBuilder = new DiscordApiBuilder();
    discordApiBuilder.setWaitForServersOnStartup(true);
    DiscordApi api = discordApiBuilder.setToken(token).login().join();

    Watcher watcher = new Watcher();
    watcher.start();

    COMMAND_MAP.get(SEARCH).parameters.put("bot_url", api.createBotInvite());

    api.addMessageCreateListener(event -> {
      if (event.getMessageAuthor().isBotUser()) {
        event.getChannel().sendMessage("Bot user _" + event.getMessageAuthor().getName() + "_");
        return;
      }
      String msg = event.getMessageContent();
      if (msg.toLowerCase().startsWith("!poring-world") || msg.toLowerCase().startsWith("!pw")) {
        String[] command = msg.split(" ");

        if (!Validator.isValidCommand(command)) {
          return;
        }

        if (COMMAND_MAP.keySet().contains(command[1].toLowerCase())) {
          COMMAND_MAP.get(command[1]).run(command, event, watcher);
        } else {
          event.getChannel().sendMessage("Invalid command: **" + command[1] + "**");
        }
      }
    });
  }

}
