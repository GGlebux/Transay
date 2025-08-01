package project.assay.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.List.of;

@Entity
@Table(name = "indicator")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Indicator {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "eng_name")
  private String engName;

  @Column(name = "rus_name")
  private String rusName;

  @Column(name = "gender")
  private String gender;

  @Column(name = "is_gravid")
  private boolean isGravid;

  @Column(name = "min_age")
  private int minAge;

  @Column(name = "max_age")
  private int maxAge;

  @Column(name = "min_value")
  private double minValue;

  @Column(name = "max_value")
  private double maxValue;

  @Column(name = "units")
  private String units;

  @OneToMany(mappedBy = "indicator", fetch = LAZY, cascade = ALL)
  private List<Measure> measure = of();

  @Override
  public String toString() {
    return "Indicator{" +
            "id=" + id +
            ", eng_name='" + engName + '\'' +
            ", rus_name='" + rusName + '\'' +
            ", gender='" + gender + '\'' +
            ", isGravid=" + isGravid +
            ", minAge=" + minAge +
            ", maxAge=" + maxAge +
            ", minValue=" + minValue +
            ", maxValue=" + maxValue +
            ", units='" + units + '\'' +
            '}';
  }
}
