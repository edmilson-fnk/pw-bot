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
  private ListM list;

  public Author() {

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

  public ListM getList() {
    return list;
  }

  public void setList(ListM list) {
    this.list = list;
  }
  public Author withDiscordName(String discordName) {
    this.discordName = discordName;
    return this;
  }

  public Author WithDiscordId(String discordId) {
    this.discordId = discordId;
    return this;
  }
}
