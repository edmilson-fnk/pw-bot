package poring.world;

import static poring.world.Constants.BOT_URL;
import static poring.world.Constants.CARDS_CALL;
import static poring.world.Constants.CARDS_CALL_SHORT;
import static poring.world.Constants.CARDS_COMMAND_MAP;
import static poring.world.Constants.MARKET_COMMAND_MAP;
import static poring.world.Constants.MARKET_CALL;
import static poring.world.Constants.THANATOS_CALL;
import static poring.world.Constants.TT_COMMAND_MAP;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
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

    api.addMessageCreateListener(marketEvent -> {
      if (marketEvent.getMessageAuthor().isBotUser()) {
        return;
      }
      String msg = marketEvent.getMessageContent();
      if (msg.toLowerCase().startsWith("!" + MARKET_CALL)) {
        String[] command = msg.split(" ");

        if (MARKET_COMMAND_MAP.keySet().contains(command[1].toLowerCase())) {
          MARKET_COMMAND_MAP.get(command[1]).run(command, marketEvent, watcher, parameters);
        }
      }
    });

    api.addMessageCreateListener(getCardsListener());

    api.addMessageCreateListener(marketEvent -> {
      if (marketEvent.getMessageAuthor().isBotUser()) {
        return;
      }
      String msg = marketEvent.getMessageContent();
      if (msg.toLowerCase().startsWith("!" + THANATOS_CALL)) {
        String[] command = msg.split(" ");

        if (TT_COMMAND_MAP.keySet().contains(command[1].toLowerCase())) {
          TT_COMMAND_MAP.get(command[1]).run(command, marketEvent, watcher, parameters);
        }
      }
    });
  }

  private static MessageCreateListener getCardsListener() {
    return cardsEvent -> {
      String msg = cardsEvent.getMessageContent();
      if (msg.toLowerCase().startsWith("!" + CARDS_CALL)
       || msg.toLowerCase().startsWith("!" + CARDS_CALL_SHORT)) {
        String[] command = msg.split(" ");

        CARDS_COMMAND_MAP.get(command[0]).run(command, cardsEvent, null, null);
      }
    };
  }

}
