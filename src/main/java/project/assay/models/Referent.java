package project.assay.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import project.assay.utils.converters.JsonToListConverter;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static org.hibernate.type.SqlTypes.JSON;

@Entity
@Table(name = "referent")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Referent {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private int id;

  @Column(name = "current_value")
  private double currentValue;

  @Column(name = "reg_date")
  private LocalDate regDate;

  @Column(name = "status")
  private String status;

  @JdbcTypeCode(JSON)
  @Convert(converter = JsonToListConverter.class)
  @Column(name = "transcript", columnDefinition = "jsonb")
  private List<String> reasons;

  @Override
  public String toString() {
    return "Referent{" +
        "id=" + id +
        ", currentValue=" + currentValue +
        ", regDate=" + regDate +
        ", status='" + status + '\'' +
        ", description=" + reasons +
        '}';
  }
}
