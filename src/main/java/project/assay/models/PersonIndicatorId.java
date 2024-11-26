package project.assay.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonIndicatorId implements Serializable {

  @Column(name = "person_id")
  private Integer personId;

  @Column(name = "indicator_id")
  private Integer indicatorId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    PersonIndicatorId that = (PersonIndicatorId) o;
    return Objects.equals(personId, that.personId) && Objects.equals(indicatorId, that.indicatorId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personId, indicatorId);
  }
}
