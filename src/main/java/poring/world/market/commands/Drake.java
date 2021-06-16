package poring.world.market.commands;

import com.google.common.collect.ImmutableList;
import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static poring.world.Constants.Constants.HANDSHAKE;

public class Drake extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    Optional<Message> referencedMessage = event.getMessage().getReferencedMessage();
    if (referencedMessage.isPresent()) {
      referencedMessage.get().reply("tá certo :handshake:");
      if (event.getMessage().canYouDelete()) {
        event.getMessage().delete();
      }
    }
  }

  @Override
  public String getHelp() {
    return "-";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of();
  }
}
