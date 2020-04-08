package poring.world.party;

import org.javacord.api.DiscordApi;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class PartyObject {

  public static final int MAX_MEMBERS = 6;
  public long serverId;
  public long leaderId;
  public Set<Long> allMembers;

  public PartyObject(long leaderId, long serverId) {
    this.leaderId = leaderId;
    this.allMembers = new HashSet<>();
    this.allMembers.add(leaderId);
  }

  public boolean isFull() {
    return allMembers.size() >= MAX_MEMBERS;
  }

  public String getLeaderName(DiscordApi api) {
    try {
      api.getUserById(leaderId).get().getDisplayName(api.getServerById(serverId).get());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    return null;
  }

}
