package poring.world.party;

import static poring.world.Constants.JOIN_PARTY;
import static poring.world.Constants.GLOBAL_CALL;

import com.google.common.collect.ImmutableList;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.general.Command;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Map;

public class CreateParty extends Command {

  public Map<String, PartyObject> parties;

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    if (!event.getServer().isPresent()) {
      event.getChannel().sendMessage("Try it in a channel");
      return;
    }
    DiscordApi api = (DiscordApi) parameters.get("api");
    long serverId = event.getServer().get().getId();
    long authorId = event.getMessageAuthor().getId();
    String partyName = Utils.getQuery(command);
    StringBuilder sb = new StringBuilder();
    if (!parties.containsKey(partyName)) {
      parties.put(partyName, new PartyObject(authorId, serverId));
      sb.append(String.format("Party **%s** created!", partyName));
    } else {
      String leaderName = parties.get(partyName).getLeaderName(api);
      sb.append(String.format("Party **%s** already created by _%s_. Try !%s %s %s ",
          partyName, leaderName, GLOBAL_CALL, JOIN_PARTY, partyName));
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
