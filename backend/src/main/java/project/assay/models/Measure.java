package project.assay.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import static java.lang.Integer.compare;


@Entity
@Table(name = "measure")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Measure implements Comparable<Measure>{
  @Override
  public int compareTo(@NotNull Measure o) {
    return compare(this.id, o.id);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "person_id", referencedColumnName = "id")
  private Person person;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "indicator_id")
  private Indicator indicator;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "referent_id", referencedColumnName = "id")
  private Referent referent;


  @Override
  public String toString() {
    return "Measure{" +
        "id=" + id +
        ", indicator=" + indicator.getId() +
        ", referent=" + referent.getId() +
        ", person=" + person.getId() +
        '}';
  }
}
