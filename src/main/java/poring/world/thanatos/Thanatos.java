package poring.world.thanatos;

import static poring.world.Constants.API;
import static poring.world.Constants.CREATE_PARTY;
import static poring.world.Constants.GLOBAL_CALL;
import static poring.world.Constants.THANATOS;
import static poring.world.thanatos.TTUtils.A;
import static poring.world.thanatos.TTUtils.B;
import static poring.world.thanatos.TTUtils.BACKUP;

import com.google.common.collect.ImmutableList;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.general.Command;
import poring.world.watcher.Watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Thanatos extends Command {

  public HashMap<Long, ThanatosTeamObject> thanatos;

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    TextChannel channel = event.getChannel();
    if (!event.getServer().isPresent()) {
      channel.sendMessage(String.format("Try _!%s %s_ in a channel", GLOBAL_CALL, THANATOS));
      return;
    }
    String option = Utils.getQuery(command);

    long serverId = event.getServer().get().getId();
    if (!thanatos.containsKey(serverId)) {
      thanatos.put(serverId, new ThanatosTeamObject(serverId));
    }

    ThanatosTeamObject ttTeam = thanatos.get(serverId);
    MessageAuthor author = event.getMessageAuthor();
    long authorId = author.getId();

    if (option.isEmpty()) {
      DiscordApi api = (DiscordApi) parameters.get(API);
      String ttTeamStr = TTUtils.showsTT(ttTeam, api);
      event.getChannel().sendMessage(ttTeamStr);
    } else if (option.equalsIgnoreCase(A)) {
      if (TTUtils.add(authorId, ttTeam, A)) {
        channel.sendMessage(String.format("%s joined _team A_ for Thanatos Tower", author.getDisplayName()));
      } else {
        channel.sendMessage("Team A for Thanatos Tower is full");
      }
    } else if (option.equalsIgnoreCase(B)) {
      if (TTUtils.add(authorId, ttTeam, B)) {
        channel.sendMessage(String.format("%s joined _team B_ for Thanatos Tower", author.getDisplayName()));
      } else {
        channel.sendMessage("Team B for Thanatos Tower is full");
      }
    } else if (option.equalsIgnoreCase(BACKUP)) {
      if (TTUtils.add(authorId, ttTeam, BACKUP)) {
        channel.sendMessage(String.format("%s joined _backup team_ for Thanatos Tower", author.getDisplayName()));
      }
    } else if (option.equalsIgnoreCase("leave")) {
    } else {

    }
  }

  @Override
  public String getHelp() {
    return "shows or creates a team for Thanatos Tower. also joins you to a Thanatos Tower team";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("", "A", "B", "backup", "leave");
  }
}
