package poring.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="watchlists")
public class WatchList implements Serializable {

  @Id
  @GeneratedValue
  private int id;

  @OneToOne
  @JoinColumn(name="id")
  @MapsId
  private Author author;

  @OneToMany(mappedBy="list", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Item> items;

  public WatchList() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public WatchList withAuthor(Author author) {
    this.author = author;
    return this;
  }

  public WatchList withItems(List<Item> items) {
    this.items = items;
    return this;
  }

  public boolean isWatching(String query) {
    for (Item i : this.items) {
      if (i.getQuery().equalsIgnoreCase(query)) {
        return true;
      }
    }
    return false;
  }

  public WatchList addItem(Item item) {
    this.items.add(item);
    return this;
  }

}
