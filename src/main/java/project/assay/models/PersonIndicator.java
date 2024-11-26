package project.assay.models;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "person_indicator")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonIndicator {
  @EmbeddedId
  private PersonIndicatorId id;

  @Column(name = "current_value")
  private double currentValue;

  @Override
  public String toString() {
    return "PersonIndicator{" +
        "currentValue=" + currentValue +
        ", id=" + id +
        '}';
  }
}
