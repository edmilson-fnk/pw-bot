package poring.world.party;

import org.javacord.api.DiscordApi;

import java.util.concurrent.ExecutionException;

public class PartyUtils {

  public static final int MAX_MEMBERS = 6;

  public static boolean add(PartyObject party, long memberId) {
    if (!isFull(party) && !party.getAllMembers().contains(memberId)) {
      party.getAllMembers().add(memberId);
      return true;
    }
    return false;
  }

  public static boolean isFull(PartyObject party) {
    return party.getAllMembers().size() >= MAX_MEMBERS;
  }

  public static String getLeaderName(PartyObject party, DiscordApi api) {
    try {
      return api.getUserById(party.getLeaderId()).get().getDisplayName(api.getServerById(party.getServerId()).get());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      return "";
    }
  }

}
