package poring.world.market.commands;

import com.google.common.collect.ImmutableList;
import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.List;

public class Alive extends Command {
  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    event.getMessage().addReaction(EmojiParser.parseToUnicode(":handshake:"));
  }

  @Override
  public String getHelp() {
    return "is GTB alive? Reacts :handshake: if so";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of();
  }
}
