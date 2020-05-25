package poring.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="authors")
public class Author {

  @Id
  private int id;

  @Column(name="discord_id")
  private int discordId;

  @Column(name="discord_name")
  private String discordName;

  @OneToMany(mappedBy="author")
  private List<ListM> lists;

  public Author() {

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
