package poring.world.thanatos;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class ThanatosTeamObject {

  public String name;
  public long serverId;
  public long leaderA;
  public long leaderB;
  public TreeMap<String, List<Long>> parties;

  public ThanatosTeamObject(String serverName, Long serverId) {
    this.name = serverName;
    this.serverId = serverId;
    this.parties = new TreeMap<>();
    this.parties.put("A", new LinkedList<>());
    this.parties.put("B", new LinkedList<>());
    this.parties.put("backup", new LinkedList<>());
  }

  public long getServerId() {
    return serverId;
  }

  public String getName() {
    return name;
  }

  public TreeMap<String, List<Long>> getParties() {
    return this.parties;
  }

  public long getLeaderA() {
    return leaderA;
  }

  public long getLeaderB() {
    return leaderB;
  }
}
