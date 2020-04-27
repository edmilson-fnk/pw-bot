package poring.world.thanatos;

import static poring.world.Constants.A;
import static poring.world.Constants.B;
import static poring.world.Constants.BACKUP;
import static poring.world.Constants.GENERAL_TIME_FORMAT;

import org.javacord.api.DiscordApi;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.ExecutionException;

public class TTUtils {

  public static String show(ThanatosTeamObject tt, DiscordApi api, String ttTime) {
    return show(tt, api, ttTime, false);
  }

  public static String call(ThanatosTeamObject tt, DiscordApi api, String ttTime) {
    return show(tt, api, ttTime, true);
  }

  private static String show(ThanatosTeamObject tt, DiscordApi api, String ttTime, boolean call) {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format(":tokyo_tower: Thanatos Tower Team: **%s**\nDate: _%s_\n", tt.getName(), ttTime));
    sb.append(getTeamStr(tt, api, A, call));
    sb.append(getTeamStr(tt, api, B, call));
    if (!tt.getParties().get(BACKUP).isEmpty()) {
      sb.append(getTeamStr(tt, api, BACKUP, call));
    }
    return sb.toString();
  }

  public static boolean isDateTime(String dateTimeStr) {
    try {
      new SimpleDateFormat(GENERAL_TIME_FORMAT).parse(dateTimeStr);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static String getNextSaturday() {
    try {
      return LocalDateTime.now().atZone(ZoneId.systemDefault())
          .withHour(20)
          .withMinute(0)
          .with(TemporalAdjusters.next(DayOfWeek.SATURDAY))
          .format(DateTimeFormatter.ofPattern(GENERAL_TIME_FORMAT));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private static String getTeamStr(ThanatosTeamObject tt, DiscordApi api, String team, boolean call) {
    StringBuilder sb = new StringBuilder();
    if (team.equalsIgnoreCase(BACKUP)) {
      sb.append("Backup\n");
    } else {
      sb.append(String.format(" _Party %s_\n", team));
    }
    int i = 1;
    for (long memberId : tt.getParties().get(team)) {
      try {
        if (call) {
          sb.append(String.format("   %s. <@%s>\n", i, memberId));
        } else {
          String memberName = api.getUserById(memberId).get().getDisplayName(api.getServerById(tt.getServerId()).get());
          sb.append(String.format("   %s. %s\n", i, memberName));
        }
      } catch (InterruptedException | ExecutionException e) {
        sb.append(String.format("   %s.\n", i));
        e.printStackTrace();
      }
      i++;
    }

    return sb.toString();
  }

  public static boolean remove(Long authorId, ThanatosTeamObject tt) {
    return tt.getParties().get(A).remove(authorId)
        || tt.getParties().get(B).remove(authorId)
        || tt.getParties().get(BACKUP).remove(authorId);
  }

  public static boolean add(Long authorId, ThanatosTeamObject tt, String team) {
    if (full(tt, team)) {
      return false;
    }

    if (contains(tt, authorId)) {
      remove(authorId, tt);
    }

    tt.getParties().get(team).add(authorId);
    return true;
  }

  private static boolean full(ThanatosTeamObject tt, String team) {
    return
        (team.equalsIgnoreCase(A) || team.equalsIgnoreCase(B))
        && (tt.getParties().get(B).size() == 6);
  }

  private static boolean contains(ThanatosTeamObject tt, Long authorId) {
    return tt.getParties().get(A).contains(authorId)
        || tt.getParties().get(B).contains(authorId)
        || tt.getParties().get(BACKUP).contains(authorId);
  }

}
