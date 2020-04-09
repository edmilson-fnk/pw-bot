package poring.world.thanatos;

import static poring.world.Constants.A;
import static poring.world.Constants.API;
import static poring.world.Constants.B;
import static poring.world.Constants.BACKUP;
import static poring.world.Constants.CALL;
import static poring.world.Constants.GLOBAL_CALL;
import static poring.world.Constants.LEAVE;
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
      DiscordApi api = (DiscordApi) parameters.get(API);
      String ttTeamStr = TTUtils.show(ttTeam, api);
      event.getChannel().sendMessage(ttTeamStr);
    } else if (option.equalsIgnoreCase(CALL)) {
      DiscordApi api = (DiscordApi) parameters.get(API);
      String ttTeamStr = TTUtils.call(ttTeam, api);
      event.getChannel().sendMessage(ttTeamStr);
    } else if (option.equalsIgnoreCase(A)) {
      if (TTUtils.add(authorId, ttTeam, A)) {
        channel.sendMessage(String.format("_%s_ joined _party A_ for Thanatos Tower", author.getDisplayName()));
      } else {
        channel.sendMessage("_Party A_ for Thanatos Tower is full, try joining backup party");
      }
    } else if (option.equalsIgnoreCase(B)) {
      if (TTUtils.add(authorId, ttTeam, B)) {
        channel.sendMessage(String.format("_%s_ joined _party B_ for Thanatos Tower", author.getDisplayName()));
      } else {
        channel.sendMessage("_Party B_ for Thanatos Tower is full, try joining backup party");
      }
    } else if (option.equalsIgnoreCase(BACKUP)) {
      if (TTUtils.add(authorId, ttTeam, BACKUP)) {
        channel.sendMessage(String.format("_%s_ joined _backup party_ for Thanatos Tower", author.getDisplayName()));
      }
    } else if (option.equalsIgnoreCase(LEAVE)) {
      TTUtils.remove(authorId, ttTeam);
      channel.sendMessage(String.format("_%s_ removed from Thanatos Tower **%s**",
          author.getDisplayName(), ttTeam.getName()));
    } else {
      channel.sendMessage(String.format("Invalid option **%s**", option));
    }
    this.saveMap();
  }

  @Override
  public String getHelp() {
    return "manages teams for Thanatos Tower on a server";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of("", A, B, BACKUP, LEAVE, CALL);
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
      System.out.println(String.format("Error on loading %s", THANATOS_TEAM_DAT));
    }
  }
}
