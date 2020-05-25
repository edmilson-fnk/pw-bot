package poring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="itens")
public class Item {

  @Id
  private int id;

  @Column(name="query")
  private String query;

  @ManyToOne
  @JoinColumn(name="list_id", nullable=false)
  private ListM list;

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

  public ListM getList() {
    return list;
  }

  public void setList(ListM list) {
    this.list = list;
  }
}
