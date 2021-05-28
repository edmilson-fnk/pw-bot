package poring.world.market.commands;

import com.google.common.collect.ImmutableList;
import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Alive extends Command {

  private static final Map<Long, String> CUSTOM_REACTION = new HashMap<Long, String>(){{
    this.put(775727348183466025L, "sqbobo:778345736806596628");
    this.put(585914389979463680L, "pepecoberto:778227749977587784");
  }};
  public static final String DEFAULT_VALUE = ":handshake:";

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    long userId = event.getMessageAuthor().getId();
    if (CUSTOM_REACTION.containsKey(userId)) {
      event.getMessage().addReaction(CUSTOM_REACTION.get(userId));
    } else {
      event.getMessage().addReaction(EmojiParser.parseToUnicode(DEFAULT_VALUE));
    }
  }

  @Override
  public String getHelp() {
    return "is GTB alive? Reacts if so";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of();
  }
}
