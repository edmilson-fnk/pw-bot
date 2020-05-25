package poring.world.party;

import static poring.world.Constants.CREATE_PARTY;
import static poring.world.Constants.GLOBAL_CALL;
import static poring.world.Constants.JOIN_PARTY;
import static poring.world.Constants.PARTIES;

import com.google.common.collect.ImmutableList;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateParty extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    TextChannel channel = event.getChannel();
    if (!event.getServer().isPresent()) {
      channel.sendMessage(String.format("Try _!%s %s_ in a server channel", GLOBAL_CALL, CREATE_PARTY));
      return;
    }

    String partyName = Utils.getQuery(command);
    if (partyName == null || partyName.isEmpty()) {
      channel.sendMessage(String.format("Party name not found. Try _!%s %s %s_", GLOBAL_CALL, CREATE_PARTY, partyName));
      return;
    }

    long serverId = event.getServer().get().getId();
    HashMap allParties = (HashMap) parameters.get(PARTIES);
    if (!allParties.containsKey(serverId)) {
      allParties.put(serverId, new HashMap<String, PartyObject>());
    }
    HashMap<String, PartyObject> parties = (HashMap) allParties.get(serverId);
    long authorId = event.getMessageAuthor().getId();

    if (!parties.containsKey(partyName)) {
      parties.put(partyName, new PartyObject(authorId, serverId));
      channel.sendMessage(String.format("Party **%s** created by _%s_.\nAnyone can join with _!%s %s %s_",
          partyName, event.getMessageAuthor().getDisplayName(), GLOBAL_CALL, JOIN_PARTY, partyName));
      return;
    }

    DiscordApi api = (DiscordApi) parameters.get("api");
    String leaderName = PartyUtils.getLeaderName(parties.get(partyName), api);
    channel.sendMessage(String.format("Party **%s** already created by _%s_. Try _!%s %s %s_",
        partyName, leaderName, GLOBAL_CALL, JOIN_PARTY, partyName));
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
