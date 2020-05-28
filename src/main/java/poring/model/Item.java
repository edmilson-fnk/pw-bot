package poring.model;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

@Entity
@Table(name="items")
public class Item implements Serializable {

  @Id
  @GeneratedValue
  private int id;

  @Column(name="query")
  private String query;

  @ManyToOne
  @JoinColumn(name="list_id", nullable=false)
  private WatchList list;

  @ElementCollection
  @CollectionTable(name="MAP")
  @MapKeyColumn(name="key")
  @Column(name="value")
  private Map<String, String> filters;

  public Item() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public WatchList getList() {
    return list;
  }

  public void setList(WatchList list) {
    this.list = list;
  }

  public Map<String, String> getFilters() {
    return filters;
  }

  public void setFilters(Map<String, String> filters) {
    this.filters = filters;
  }

  public Item withQuery(String query) {
    this.query = query;
    return this;
  }

  public Item withList(WatchList list) {
    this.list = list;
    return this;
  }

  public Item withFilters(Map<String, String> filters) {
    this.filters = filters;
    return this;
  }
}
