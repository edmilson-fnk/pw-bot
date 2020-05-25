package poring.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="lists")
public class ListM {

  @Id
  private int id;

  @ManyToOne
  @JoinColumn(name="author_id", nullable=false)
  private Author author;

  @OneToMany(mappedBy="list")
  private List<Item> itens;

  public ListM() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<Item> getItens() {
    return itens;
  }

  public void setItens(List<Item> itens) {
    this.itens = itens;
  }
}
