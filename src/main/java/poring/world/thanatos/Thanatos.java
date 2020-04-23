package poring.world.thanatos;

import static poring.world.Constants.A;
import static poring.world.Constants.API;
import static poring.world.Constants.B;
import static poring.world.Constants.BACKUP;
import static poring.world.Constants.CALL;
import static poring.world.Constants.GENERAL_TIME_FORMAT;
import static poring.world.Constants.GLOBAL_CALL;
import static poring.world.Constants.LEAVE;
import static poring.world.Constants.RESET;
import static poring.world.Constants.THANATOS;
import static poring.world.s3.S3Files.THANATOS_TEAM_DAT;
import static poring.world.s3.S3Files.THANATOS_TIME_DAT;

import com.google.common.collect.ImmutableList;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.general.Command;
import poring.world.s3.S3Files;
import poring.world.watcher.Watcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
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
      String msg = showTT(parameters, ttTeam, ttTime);
      channel.sendMessage(msg);
    } else if (TTUtils.isDateTime(option)) {
      thanatosTime.put(timeKey, option);
      String ttTime = thanatosTime.get(timeKey);
      String msg = showTT(parameters, ttTeam, ttTime);
      channel.sendMessage(msg);
    } else if (option.equalsIgnoreCase(CALL)) {
      // call the team
      String ttTime = thanatosTime.get(timeKey);
      String msg = callTT(parameters, ttTeam, ttTime);
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
        String msg = String.format("Thanatos team **%s** was unmade.", ttName);
        channel.sendMessage(msg);
      } else {
        String msg = String.format("**%s** team not found. Try using _!%s %s %s %s_", ttName, GLOBAL_CALL, THANATOS,
            RESET, ttTeam.getName());
        channel.sendMessage(msg);
      }
    } else if (option.toLowerCase().startsWith(RESET)) {
      String msg = String.format("Try _!%s %s %s %s_", GLOBAL_CALL, THANATOS,
          RESET, ttTeam.getName());
      channel.sendMessage(msg);
    } else {
      // invalid option
      channel.sendMessage(String.format("Invalid option **%s**", option));
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

  private String callTT(Map<String, Object> parameters, ThanatosTeamObject ttTeam, String ttTime) {
    return TTUtils.call(ttTeam, (DiscordApi) parameters.get(API), ttTime);
  }

  private String showTT(Map<String, Object> parameters, ThanatosTeamObject ttTeam, String ttTime) {
    return TTUtils.show(ttTeam, (DiscordApi) parameters.get(API), ttTime);
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
    File teamFile = saveMapFile(this.thanatosTime, THANATOS_TIME_DAT);
    S3Files.uploadThanatosTeam(teamFile);
    File timeFile = saveMapFile(this.thanatosTeam, THANATOS_TEAM_DAT);
    S3Files.uploadThanatosTime(timeFile);
  }

  private synchronized File saveMapFile(Map map, String fileName) {
    try {
      FileOutputStream fos = new FileOutputStream(fileName);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(map);
      oos.close();
      fos.close();
      return new File(fileName);
    } catch (FileNotFoundException e) {
      System.out.println(String.format("File %s not found on saving", fileName));
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(String.format("Error on saving %s", fileName));
    }
    return null;
  }

  private synchronized void loadMaps() {
    this.thanatosTeam = loadMapFile(S3Files.downloadThanatosTeam(), THANATOS_TEAM_DAT);
    this.thanatosTime = loadMapFile(S3Files.downloadThanatosTime(), THANATOS_TIME_DAT);
  }

  private synchronized Map loadMapFile(File file, String fileName) {
    try {
      FileInputStream fis = new FileInputStream(file);
      ObjectInputStream ois = new ObjectInputStream(fis);
      Map<Long, ThanatosTeamObject> map = new HashMap<>((Map) ois.readObject());
      ois.close();
      return map;
    } catch (FileNotFoundException e) {
      System.out.println(String.format("File %s not found on reading", fileName));
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      System.out.println(String.format("Error on loading %s", fileName));
    }
    return new HashMap();
  }
}
