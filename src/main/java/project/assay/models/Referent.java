package project.assay.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import project.assay.utils.converters.JsonToListConverter;

@Entity
@Table(name = "referent")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Referent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "current_value")
  private double currentValue;

  @Column(name = "reg_date")
  private LocalDate regDate;

  @Column(name = "units")
  private String units;

  @Column(name = "status")
  private String status;

  @OneToOne(mappedBy = "referent", cascade = CascadeType.PERSIST)
  private PersonInfo personInfo;

  @Convert(converter = JsonToListConverter.class)
  @Column(name = "transcript", columnDefinition = "jsonb")
  @JdbcTypeCode(SqlTypes.JSON)
  private List<String> reasons;

  @Override
  public String toString() {
    return "Referent{" +
        "id=" + id +
        ", currentValue=" + currentValue +
        ", regDate=" + regDate +
        ", units='" + units + '\'' +
        ", status='" + status + '\'' +
        ", description=" + reasons +
        '}';
  }
}
