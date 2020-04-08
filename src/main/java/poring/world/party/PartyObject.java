package poring.world.party;


import java.util.HashSet;
import java.util.Set;

public class PartyObject {

  public long serverId;
  public long leaderId;
  public Set<Long> allMembers;

  public PartyObject(long leaderId, long serverId) {
    this.leaderId = leaderId;
    this.serverId = serverId;
    this.allMembers = new HashSet<>();
    this.allMembers.add(leaderId);
  }

  public long getServerId() {
    return serverId;
  }

  public long getLeaderId() {
    return leaderId;
  }

  public Set<Long> getAllMembers() {
    return allMembers;
  }

}
