package poring.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="WatchingChannels")
public class WatchingChannel implements Serializable {

  @Id
  @GeneratedValue
  private int id;

  @OneToOne
  @JoinColumn(name="id")
  @MapsId
  private Channel channel;

  @ElementCollection(fetch=FetchType.EAGER)
  private List<String> items;

  public WatchingChannel() {
    this.items = new LinkedList<>();
  }

  @Override
  public String toString() {
    return this.items.toString();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Channel getChannel() {
    return channel;
  }

  public void setChannel(Channel channel) {
    this.channel = channel;
  }

  public List<String> getItems() {
    return items;
  }

  public void setItems(List<String> items) {
    this.items = items;
  }

  public boolean add(String item) {
    return this.items.add(item);
  }

  public boolean remove(String item) {
    return this.items.remove(item);
  }

}
