package poring.world.party;

import static poring.world.Constants.CREATE_PARTY;
import static poring.world.Constants.END_PARTY;
import static poring.world.Constants.GLOBAL_CALL;
import static poring.world.Constants.JOIN_PARTY;
import static poring.world.Constants.PARTIES;

import com.google.common.collect.ImmutableList;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EndParty extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    // Validate it's a server channel
    TextChannel channel = event.getChannel();
    if (!event.getServer().isPresent()) {
      channel.sendMessage(String.format("Try _!%s %s_ in a channel", GLOBAL_CALL, JOIN_PARTY));
      return;
    }

    // Validate there's a party name in sent command
    String partyName = Utils.getQuery(command);
    if (partyName == null || partyName.isEmpty()) {
      channel.sendMessage(String.format("Party name not found. Try _!%s %s %s_", GLOBAL_CALL, END_PARTY, partyName));
      return;
    }

    // Validate there's any party in this server
    TreeMap allParties = (TreeMap) parameters.get(PARTIES);
    long serverId = event.getServer().get().getId();
    if (!allParties.containsKey(serverId)) {
      channel.sendMessage(String.format("Party **%s** not found. Try _!%s %s %s_",
          partyName, GLOBAL_CALL, CREATE_PARTY, partyName));
      return;
    }

    // Validate there's specific party in this server
    HashMap<String, PartyObject> parties = (HashMap) allParties.get(serverId);
    StringBuilder sb = new StringBuilder();
    MessageAuthor messageAuthor = event.getMessageAuthor();
    if (!parties.containsKey(partyName)) {
      channel.sendMessage(String.format("Party **%s** not found. Try _!%s %s %s_",
          partyName, GLOBAL_CALL, CREATE_PARTY, partyName));
      return;
    }

    // Validate author's message is party's leader
    if (parties.get(partyName).getLeaderId() != messageAuthor.getId()) {
      DiscordApi api = (DiscordApi) parameters.get("api");
      String leaderName = PartyUtils.getLeaderName(parties.get(partyName), api);
      channel.sendMessage(String.format("Party **%s** leader is _%s_, only him can use _!%s %s %s_",
          partyName, leaderName, GLOBAL_CALL, END_PARTY, partyName));
      return;
    }

    parties.remove(partyName);
    channel.sendMessage(String.format("Party **%s** was dissolved", partyName));
  }

  @Override
  public String getHelp() {
    return "removes everyone from a party";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("my-party");
  }
}
