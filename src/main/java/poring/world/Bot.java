package poring.world;

import static poring.world.constants.Constants.ADMIN_MAP;
import static poring.world.constants.Constants.ADM_CALL;
import static poring.world.constants.Constants.COMMAND_MAP;
import static poring.world.constants.Constants.ENV;
import static poring.world.constants.Constants.GLOBAL_CALL;
import static poring.world.constants.Constants.IS_PRODUCTION;
import static poring.world.constants.Constants.MAINTENANCE;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import poring.world.adm.Validator;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.Map;

public class Bot {

  public static void main(String[] args) {
    System.out.println("Starting Golden Thief Bot");
    System.out.println("Environment: " + ENV);

    String token = System.getenv("DISCORD_TOKEN");
    DiscordApiBuilder discordApiBuilder = new DiscordApiBuilder();
    discordApiBuilder.setWaitForServersOnStartup(true);
    DiscordApi api = discordApiBuilder.setToken(token).login().join();

    Watcher watcher = new Watcher(api);
    if (IS_PRODUCTION && !MAINTENANCE) {
      watcher.start();
      System.out.println("GTB Watcher started");
    }

    api.addMessageCreateListener(getMarketListener(watcher));
    System.out.println("GTB running...");
  }

  private static MessageCreateListener getMarketListener(Watcher watcher) {
    return event -> {
      MessageAuthor messageAuthor = event.getMessageAuthor();
      if (messageAuthor.isBotUser()) {
        return;
      }

      String msg = event.getMessageContent().toLowerCase();
      if (msg.startsWith("!" + ADM_CALL + " ") && Validator.validAdmCall(messageAuthor.getId())) {
        runCommand(msg, event, watcher, ADMIN_MAP);
      }

      if (msg.startsWith("!" + GLOBAL_CALL + " ")) {
        runCommand(msg, event, watcher, COMMAND_MAP);
      }
    };
  }

  private static void runCommand(String msg, MessageCreateEvent event,
                                 Watcher watcher, Map<String, Command> commands) {
    if (MAINTENANCE) {
      event.getChannel().sendMessage("_GTB is under maintenance for a while_");
      return;
    }

    String[] command = msg.trim()
        .replaceAll("\n", " ")
        .replaceAll(" +", " ")
        .split(" ");

    if (command.length <= 1 || command[1].trim().isEmpty()) {
      event.getChannel().sendMessage("No command for gtb, try **!" + GLOBAL_CALL + " help**");
    } else if (commands.containsKey(command[1])) {
      commands.get(command[1].trim()).run(command, event, watcher);
    } else {
      String nearestCommand = Utils.getNearestCommand(command[1], commands.keySet());
      if (nearestCommand != null) {
        event.getChannel().sendMessage(
            String.format("Invalid command **%s**, did you mean **%s**? Running it instead...",
                command[1], nearestCommand));
        commands.get(nearestCommand).run(command, event, watcher);
      } else {
        event.getChannel().sendMessage(String.format("Invalid command **%s**", command[1]));
      }
    }
  }
}
