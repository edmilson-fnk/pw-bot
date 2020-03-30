package poring.world;

import com.google.common.collect.ImmutableMap;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import poring.world.command.CleanCommand;
import poring.world.command.Command;
import poring.world.command.HelpCommand;
import poring.world.command.SearchCommand;
import poring.world.command.Validator;
import poring.world.command.WatchCommand;
import poring.world.listen.Listener;

import java.util.Map;

public class Bot {

  public static Map<String, Command> COMMAND_MAP = ImmutableMap.of(
      "search", new SearchCommand(),
      "watch", new WatchCommand(),
      "clean", new CleanCommand(),
      "help", new HelpCommand()
  );

  public static void main(String[] args) {
    String token = "NjkzNTM4MjA5OTEyODQ4NDE0.Xn-iJQ.2-vptTMc3ou2IZbe5sAbG5ayaSM";
    DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

    Listener listener = new Listener();
    listener.start();

    api.addMessageCreateListener(event -> {
      String msg = event.getMessageContent();
      if (msg.startsWith("!poring-world") || msg.startsWith("!pw")) {
        String[] command = msg.split(" ");

        if (!Validator.isValidCommand(command)) {
          return;
        }

        if (COMMAND_MAP.keySet().contains(command[1])) {
          COMMAND_MAP.get(command[1]).run(command, event, listener);
        }
      }
    });
  }

}
