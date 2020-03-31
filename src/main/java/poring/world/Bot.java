package poring.world;

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
    this.put("help", new HelpCommand());
    this.put("search", new SearchCommand());
    this.put("watch", new WatchCommand());
    this.put("clean", new CleanCommand());
    this.put("list", new ListCommand());
    this.put("remove", new RemoveCommand());
  }};

  public static void main(String[] args) {
    String token = System.getenv("DISCORD_TOKEN");
    DiscordApiBuilder discordApiBuilder = new DiscordApiBuilder();
    discordApiBuilder.setWaitForServersOnStartup(true);
    DiscordApi api = discordApiBuilder.setToken(token).login().join();

    Watcher watcher = new Watcher();
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
          return;
        }

        if (COMMAND_MAP.keySet().contains(command[1].toLowerCase())) {
          COMMAND_MAP.get(command[1]).run(command, event, watcher);
        } else {
          event.getChannel().sendMessage("Invalid command: _" + command[1] + "_");
        }
      }
    });
  }

}
