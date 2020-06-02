package poring.world.thanatos;

import static poring.world.Constants.Constants.A;
import static poring.world.Constants.Constants.B;
import static poring.world.Constants.Constants.BACKUP;
import static poring.world.Constants.Constants.CALL;
import static poring.world.Constants.Constants.GENERAL_TIME_FORMAT;
import static poring.world.Constants.Constants.GLOBAL_CALL;
import static poring.world.Constants.Constants.HELP;
import static poring.world.Constants.Constants.LEAVE;
import static poring.world.Constants.Constants.RESET;
import static poring.world.Constants.Constants.THANATOS;
import static poring.world.s3.S3Files.THANATOS_TEAM_DAT;
import static poring.world.s3.S3Files.THANATOS_TIME_DAT;

import com.google.common.collect.ImmutableList;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.s3.S3Files;
import poring.world.watcher.Watcher;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Thanatos extends Command {

  public Map<Long, ThanatosTeamObject> thanatosTeam = new HashMap<>();
  public Map<String, String> thanatosTime = new HashMap<>();

  public Thanatos() {
    this.loadMaps();
  }

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    TextChannel channel = event.getChannel();
    if (!event.getServer().isPresent()) {
      channel.sendMessage(String.format("Try _!%s %s_ in a server channel", GLOBAL_CALL, THANATOS));
      return;
    }

    String option = Utils.getQuery(command);

    long serverId = event.getServer().get().getId();
    if (!thanatosTeam.containsKey(serverId)) {
      thanatosTeam.put(serverId, new ThanatosTeamObject(event.getServer().get().getName(), serverId));
    }

    ThanatosTeamObject ttTeam = thanatosTeam.get(serverId);
    MessageAuthor author = event.getMessageAuthor();
    long authorId = author.getId();

    String timeKey = ttTeam.getName() + serverId;
    if (option.isEmpty()) {
      // shows current team for TT
      if (thanatosTime.get(timeKey) == null) {
        thanatosTime.put(timeKey, TTUtils.getNextSaturday());
      }
      String ttTime = thanatosTime.get(timeKey);
      String msg = showTT(event.getApi(), ttTeam, ttTime);
      channel.sendMessage(msg);
    } else if (TTUtils.isDateTime(option)) {
      thanatosTime.put(timeKey, option);
      String ttTime = thanatosTime.get(timeKey);
      String msg = showTT(event.getApi(), ttTeam, ttTime);
      channel.sendMessage(msg);
    } else if (option.equalsIgnoreCase(CALL)) {
      // call the team
      String ttTime = thanatosTime.get(timeKey);
      String msg = callTT(event.getApi(), ttTeam, ttTime);
      channel.sendMessage(msg);
    } else if (option.equalsIgnoreCase(A)) {
      // joins party A
      String msg = joinPartyTT(ttTeam, author, A);
      channel.sendMessage(msg);
    } else if (option.equalsIgnoreCase(B)) {
      // joins party B
      String msg = joinPartyTT(ttTeam, author, B);
      channel.sendMessage(msg);
    } else if (option.equalsIgnoreCase(BACKUP)) {
      // joins backup party
      String msg = joinBackupTT(ttTeam, author, authorId);
      channel.sendMessage(msg);
    } else if (option.equalsIgnoreCase(LEAVE)) {
      // removes you for TT team
      String msg = removePartyTT(ttTeam, author);
      channel.sendMessage(msg);
    } else if (option.toLowerCase().startsWith(RESET + " ")) {
      String[] optionSplit = option.split(" ");
      StringJoiner joiner = new StringJoiner(" ");
      for (int i = 1; i < optionSplit.length; i++) {
        joiner.add(optionSplit[i]);
      }
      String ttName = joiner.toString();
      if (ttName.equals(ttTeam.getName())) {
        thanatosTeam.put(serverId, new ThanatosTeamObject(event.getServer().get().getName(), serverId));
        thanatosTime.remove(timeKey);
        channel.sendMessage(String.format("Thanatos team **%s** unmade", ttName));
      } else {
        channel.sendMessage(String.format("**%s** team not found. Try _!%s %s %s %s_", ttName, GLOBAL_CALL, THANATOS,
            RESET, ttTeam.getName()));
      }
    } else if (option.toLowerCase().startsWith(RESET)) {
      channel.sendMessage(String.format("Try _!%s %s %s %s_", GLOBAL_CALL, THANATOS, RESET, ttTeam.getName()));
    } else {
      // invalid option
      channel.sendMessage(String.format("Invalid option **%s**, try !%s %s %s", option, GLOBAL_CALL, HELP, THANATOS));
    }
    this.saveMaps();
  }

  private String removePartyTT(ThanatosTeamObject ttTeam, MessageAuthor author) {
    return TTUtils.remove(author.getId(), ttTeam) ?
        String.format("_%s_ removed from Thanatos Tower **%s**", author.getDisplayName(), ttTeam.getName()) :
        String.format("_%s_ not in Thanatos Tower **%s**", author.getDisplayName(), ttTeam.getName());
  }

  private String joinBackupTT(ThanatosTeamObject ttTeam, MessageAuthor author, long authorId) {
    return TTUtils.add(authorId, ttTeam, BACKUP) ?
        String.format("_%s_ joined _backup party_ for Thanatos Tower", author.getDisplayName()) :
        String.format("_%s_ couldn't join Thanatos Tower backup", author.getDisplayName());
  }

  private String joinPartyTT(ThanatosTeamObject tt, MessageAuthor author, String team) {
    return TTUtils.add(author.getId(), tt, team) ?
        String.format("_%s_ joined _party %s_ for Thanatos Tower", author.getDisplayName(), team) :
        String.format("_Party %s_ for Thanatos Tower is full, try joining backup party", team);
  }

  private String callTT(DiscordApi api, ThanatosTeamObject ttTeam, String ttTime) {
    return TTUtils.call(ttTeam, api, ttTime);
  }

  private String showTT(DiscordApi api, ThanatosTeamObject ttTeam, String ttTime) {
    return TTUtils.show(ttTeam, api, ttTime);
  }

  @Override
  public String getHelp() {
    return "manages teams for Thanatos Tower on a server. Default scheduled date is next Saturday";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("", "30/10/2020 20:30", GENERAL_TIME_FORMAT, A, B, BACKUP, LEAVE, CALL, RESET + " team-name");
  }

  private synchronized void saveMaps() {
    File teamFile = Utils.saveMapFile(this.thanatosTime, THANATOS_TIME_DAT);
    S3Files.uploadThanatosTeam(teamFile);
    File timeFile = Utils.saveMapFile(this.thanatosTeam, THANATOS_TEAM_DAT);
    S3Files.uploadThanatosTime(timeFile);
  }

  private synchronized void loadMaps() {
    this.thanatosTeam = Utils.loadMapFile(THANATOS_TEAM_DAT);
    this.thanatosTime = Utils.loadMapFile(THANATOS_TIME_DAT);
  }
}
