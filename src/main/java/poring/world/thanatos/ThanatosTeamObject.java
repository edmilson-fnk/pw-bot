package poring.world.thanatos;

import static poring.world.Constants.A;
import static poring.world.Constants.B;
import static poring.world.Constants.BACKUP;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ThanatosTeamObject implements Serializable {

  private String name;
  private long serverId;
  private long leaderA;
  private long leaderB;
  private Map<String, List<Long>> parties;

  public ThanatosTeamObject(String serverName, Long serverId) {
    this.name = serverName;
    this.serverId = serverId;
    this.parties = new HashMap<>();
    this.parties.put(A, new LinkedList<>());
    this.parties.put(B, new LinkedList<>());
    this.parties.put(BACKUP, new LinkedList<>());
  }

  public long getServerId() {
    return serverId;
  }

  public String getName() {
    return name;
  }

  public Map<String, List<Long>> getParties() {
    return this.parties;
  }

  public long getLeaderA() {
    return leaderA;
  }

  public long getLeaderB() {
    return leaderB;
  }
}
