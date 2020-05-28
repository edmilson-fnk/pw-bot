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
@Table(name="authors")
public class Author implements Serializable {

  @Id
  @GeneratedValue
  private int id;

  @Column(name="discord_id")
  private String discordId;

  @Column(name="discord_name")
  private String discordName;

  @OneToOne
  @JoinColumn(name="id")
  private WatchList list;

  public Author() {
    this.list = new WatchList();
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

  public String getDiscordName() {
    return discordName;
  }

  public void setDiscordName(String discordName) {
    this.discordName = discordName;
  }

  public WatchList getList() {
    return list;
  }

  public void setList(WatchList list) {
    this.list = list;
  }

  public Author withDiscordName(String discordName) {
    this.discordName = discordName;
    return this;
  }

  public Author withDiscordId(String discordId) {
    this.discordId = discordId;
    return this;
  }

  public Author withList(WatchList list) {
    this.list = list;
    return this;
  }
}
