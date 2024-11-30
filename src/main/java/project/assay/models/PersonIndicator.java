package project.assay.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "person_indicator")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonIndicator {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @ManyToOne
  @JoinColumn(name = "person_id", referencedColumnName = "id")
  private Person person;

  @ManyToOne
  @JoinColumn(name = "indicator_id", referencedColumnName = "id")
  private Indicator indicator;

  @Column(name = "current_value")
  private double currentValue;
}
