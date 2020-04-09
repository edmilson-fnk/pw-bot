package poring.world.party;

import static poring.world.Constants.CREATE_PARTY;
import static poring.world.Constants.GLOBAL_CALL;
import static poring.world.Constants.JOIN_PARTY;
import static poring.world.Constants.PARTIES;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.general.Command;
import poring.world.watcher.Watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JoinParty extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    TextChannel channel = event.getChannel();
    if (!event.getServer().isPresent()) {
      channel.sendMessage(String.format("Try _!%s %s_ in a server channel", GLOBAL_CALL, JOIN_PARTY));
      return;
    }

    String partyName = Utils.getQuery(command);
    if (partyName == null || partyName.isEmpty()) {
      channel.sendMessage(String.format("Party name not found. Try _!%s %s %s_", GLOBAL_CALL, JOIN_PARTY, partyName));
      return;
    }

    HashMap allParties = (HashMap) parameters.get(PARTIES);
    long serverId = event.getServer().get().getId();
    if (!allParties.containsKey(serverId)) {
      channel.sendMessage(String.format("Party **%s** not found. Try _!%s %s %s_",
          partyName, GLOBAL_CALL, CREATE_PARTY, partyName));
      return;
    }

    TreeMap<String, PartyObject> parties = (TreeMap) allParties.get(serverId);
    StringBuilder sb = new StringBuilder();
    MessageAuthor messageAuthor = event.getMessageAuthor();

    if (!parties.containsKey(partyName)) {
      channel.sendMessage(String.format("Party **%s** not found. Try _!%s %s %s_",
          partyName, GLOBAL_CALL, CREATE_PARTY, partyName));
      return;
    }

    if (PartyUtils.add(parties.get(partyName), messageAuthor.getId())){
      sb.append(String.format("_%s_ joined **%s**", messageAuthor.getDisplayName(), partyName));
    } else {
      sb.append(String.format("Party **%s** is full or you're the leader", partyName));
    }

    channel.sendMessage(sb.toString());
  }

  @Override
  public String getHelp() {
    return "to be done";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("");
  }

}
