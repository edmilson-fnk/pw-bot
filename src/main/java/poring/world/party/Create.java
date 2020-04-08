package poring.world.party;

import static poring.world.Constants.JOIN;
import static poring.world.Constants.GLOBAL_CALL;

import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.general.Command;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Map;

public class Create extends Command {

  public Map<String, PartyObject> parties;

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    long authorId = event.getMessageAuthor().getId();
    String partyName = Utils.getQuery(command);
    StringBuilder sb = new StringBuilder();
    if (!parties.containsKey(partyName)) {
      parties.put(partyName, new PartyObject());
      sb.append(String.format("Party **%s** created!", partyName));
    } else {
      String leaderName = "";
      sb.append(String.format("Party **%s** already created by _%s_. Try !%s %s %s ",
          partyName, leaderName, GLOBAL_CALL, JOIN, partyName));
    }
    event.getChannel().sendMessage(sb.toString());
  }

  @Override
  public String getHelp() {
    return "creates and join a party";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("");
  }
}
