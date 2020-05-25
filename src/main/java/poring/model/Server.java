package poring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="servers")
public class Server {

  @Id
  private int id;

  @Column(name="discord_id")
  private int discordId;

  @Column(name="discord_name")
  private String discordName;

  public Server() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getDiscordId() {
    return discordId;
  }

  public void setDiscordId(int discordId) {
    this.discordId = discordId;
  }

  public String getDiscordName() {
    return discordName;
  }

  public void setDiscordName(String discordName) {
    this.discordName = discordName;
  }
}
