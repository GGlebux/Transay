package project.assay.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "person_info")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @OneToOne
  @JoinColumn(name = "indicator_id", referencedColumnName = "id")
  private Indicator indicator;

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "referent_id", referencedColumnName = "id")
  private Referent referent;

  @ManyToOne
  @JoinColumn(name = "person_id", referencedColumnName = "id")
  private Person person;

  @Override
  public String toString() {
    return "PersonInfo{" +
        "id=" + id +
        ", indicator=" + indicator.getId() +
        ", referent=" + referent.getId() +
        ", person=" + person.getId() +
        '}';
  }
}
