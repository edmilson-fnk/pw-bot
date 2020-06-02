package poring.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="channels")
public class Channel implements Serializable {

  @Id
  @GeneratedValue
  private int id;

  @Column(name="discord_id")
  private String discordId;

  @OneToOne
  @JoinColumn(name="id")
  private WatchingChannel list;

  public Channel() {
    this.list = new WatchingChannel();
    this.list.setChannel(this);
  }

  @Override
  public String toString() {
    return String.format("%s - <#%s> with %s", this.id, this.discordId, this.list);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDiscordId() {
    return discordId;
  }

  public void setDiscordId(String discordId) {
    this.discordId = discordId;
  }

  public WatchingChannel getList() {
    return list;
  }

  public void setList(WatchingChannel list) {
    list.setChannel(this);
    this.list = list;
  }

  public Channel withDiscordId(String discordId) {
    this.discordId = discordId;
    return this;
  }
}
