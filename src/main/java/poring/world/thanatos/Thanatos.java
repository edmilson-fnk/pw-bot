package poring.world.thanatos;

import static poring.world.Constants.A;
import static poring.world.Constants.API;
import static poring.world.Constants.B;
import static poring.world.Constants.BACKUP;
import static poring.world.Constants.CALL;
import static poring.world.Constants.GLOBAL_CALL;
import static poring.world.Constants.LEAVE;
import static poring.world.Constants.RESET;
import static poring.world.Constants.THANATOS;
import static poring.world.s3.S3Files.THANATOS_TEAM_DAT;

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

  public Map<Long, ThanatosTeamObject> thanatos = new HashMap<>();

  public Thanatos() {
    this.loadMap();
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
    if (!thanatos.containsKey(serverId)) {
      thanatos.put(serverId, new ThanatosTeamObject(event.getServer().get().getName(), serverId));
    }

    ThanatosTeamObject ttTeam = thanatos.get(serverId);
    MessageAuthor author = event.getMessageAuthor();
    long authorId = author.getId();

    if (option.isEmpty()) {
      // shows current team for TT
      String msg = showTT(parameters, ttTeam);
      channel.sendMessage(msg);
    } else if (option.equalsIgnoreCase(CALL)) {
      // call the team
      String msg = callTT(parameters, ttTeam);
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
        thanatos.put(serverId, new ThanatosTeamObject(event.getServer().get().getName(), serverId));
        String msg = String.format("Thanatos team **%s** was unmade.", ttName);
        channel.sendMessage(msg);
      } else {
        String msg = String.format("**%s** team not found. Try using _!%s %s %s %s_", ttName, GLOBAL_CALL, THANATOS,
            RESET, ttTeam.getName());
        channel.sendMessage(msg);
      }
    } else {
      // invalid option
      channel.sendMessage(String.format("Invalid option **%s**", option));
    }
    this.saveMap();
  }

  private String removePartyTT(ThanatosTeamObject ttTeam, MessageAuthor author) {
    StringBuilder sb = new StringBuilder();
    if (TTUtils.remove(author.getId(), ttTeam)) {
      sb.append(String.format("_%s_ removed from Thanatos Tower **%s**", author.getDisplayName(), ttTeam.getName()));
    } else {
      sb.append(String.format("_%s_ not in Thanatos Tower **%s**", author.getDisplayName(), ttTeam.getName()));
    }
    return sb.toString();
  }

  private String joinBackupTT(ThanatosTeamObject ttTeam, MessageAuthor author, long authorId) {
    if (TTUtils.add(authorId, ttTeam, BACKUP)) {
      return String.format("_%s_ joined _backup party_ for Thanatos Tower", author.getDisplayName());
    } else {
      return String.format("_%s_ couldn't Thanatos Tower", author.getDisplayName());
    }
  }

  private String joinPartyTT(ThanatosTeamObject tt, MessageAuthor author, String team) {
    return TTUtils.add(author.getId(), tt, team) ?
        String.format("_%s_ joined _party %s_ for Thanatos Tower", author.getDisplayName(), team) :
        String.format("_Party %s_ for Thanatos Tower is full, try joining backup party", team);
  }

  private String callTT(Map<String, Object> parameters, ThanatosTeamObject ttTeam) {
    return TTUtils.call(ttTeam, (DiscordApi) parameters.get(API));
  }

  private String showTT(Map<String, Object> parameters, ThanatosTeamObject ttTeam) {
    return TTUtils.show(ttTeam, (DiscordApi) parameters.get(API));
  }

  @Override
  public String getHelp() {
    return "manages teams for Thanatos Tower on a server";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("", A, B, BACKUP, LEAVE, CALL, RESET + "tt-name");
  }

  private synchronized void saveMap() {
    try {
      FileOutputStream fos = new FileOutputStream(THANATOS_TEAM_DAT);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(this.thanatos);
      oos.close();
      fos.close();
      S3Files.uploadThanatosTeam(new File(THANATOS_TEAM_DAT));
    } catch (FileNotFoundException e) {
      System.out.println(String.format("File %s not found on saving", THANATOS_TEAM_DAT));
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(String.format("Error on saving %s", THANATOS_TEAM_DAT));
    }
  }

  private synchronized void loadMap() {
    this.thanatos = new HashMap<>();
    try {
      File file = S3Files.downloadThanatosTeam();
      FileInputStream fis = new FileInputStream(file);
      ObjectInputStream ois = new ObjectInputStream(fis);
      Map<Long, ThanatosTeamObject> map = new HashMap<>((Map) ois.readObject());
      ois.close();
      this.thanatos = map;
    } catch (FileNotFoundException e) {
      System.out.println(String.format("File %s not found on reading", THANATOS_TEAM_DAT));
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      System.out.println(String.format("Error on loading %s", THANATOS_TEAM_DAT));
    }
  }
}
