package poring.world.thanatos;

import static poring.world.Constants.A;
import static poring.world.Constants.B;
import static poring.world.Constants.BACKUP;

import org.javacord.api.DiscordApi;

import java.util.concurrent.ExecutionException;

public class TTUtils {

  public static String show(ThanatosTeamObject tt, DiscordApi api) {
    return show(tt, api, false);
  }

  public static String call(ThanatosTeamObject tt, DiscordApi api) {
    return show(tt, api, true);
  }

  private static String show(ThanatosTeamObject tt, DiscordApi api, boolean call) {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("Thanatos Tower Team: **%s**\n", tt.getName()));
    sb.append(getTeamStr(tt, api, A, call));
    sb.append(getTeamStr(tt, api, B, call));
    if (!tt.getParties().get(BACKUP).isEmpty()) {
      sb.append(getTeamStr(tt, api, BACKUP, call));
    }
    return sb.toString();
  }

  private static String getTeamStr(ThanatosTeamObject tt, DiscordApi api, String team, boolean call) {
    StringBuilder sb = new StringBuilder();
    if (team.equalsIgnoreCase(BACKUP)) {
      sb.append("Backup\n");
    } else {
      sb.append(String.format(" _Team %s_\n", team));
    }
    int i = 1;
    for (long memberId : tt.getParties().get(team)) {
      try {
        if (call) {
          sb.append(String.format("   %s. <@%s>\n", i, memberId));
        } else {
          String name = api.getUserById(memberId).get().getDisplayName(api.getServerById(tt.getServerId()).get());
          sb.append(String.format("   %s. %s\n", i, name));
        }
      } catch (InterruptedException | ExecutionException e) {
        sb.append(String.format("   %s.\n", i));
        e.printStackTrace();
      }
      i++;
    }

    return sb.toString();
  }

  public static void remove(Long author, ThanatosTeamObject tt) {
    tt.getParties().get(A).remove(author);
    tt.getParties().get(B).remove(author);
    tt.getParties().get(BACKUP).remove(author);
  }

  public static boolean add(Long author, ThanatosTeamObject tt, String team) {
    if (full(tt, team)) {
      return false;
    }

    if (contains(tt, author)) {
      remove(author, tt);
    }

    tt.getParties().get(team).add(author);
    return true;
  }

  private static boolean full(ThanatosTeamObject tt, String team) {
    return
        (team.equalsIgnoreCase(A) || team.equalsIgnoreCase(B))
        && (tt.getParties().get(B).size() == 6);
  }

  private static boolean contains(ThanatosTeamObject tt, Long author) {
    return tt.getParties().get(A).contains(author)
        || tt.getParties().get(B).contains(author)
        || tt.getParties().get(BACKUP).contains(author);
  }

}
