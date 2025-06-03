package project.assay.models;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import project.assay.utils.converters.JsonToListConverter;

import java.util.List;

import static org.hibernate.type.SqlTypes.JSON;

@Entity
@Table(name = "transcript")
@Data
public class Transcript {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "name")
  private String name;

  @Column(name = "gender")
  private String gender;

  @JdbcTypeCode(JSON)
  @Convert(converter = JsonToListConverter.class)
  @Column(name = "fall", columnDefinition = "jsonb")
  private List<String> fall;

  @JdbcTypeCode(JSON)
  @Convert(converter = JsonToListConverter.class)
  @Column(name = "raise", columnDefinition = "jsonb")
  private List<String> raise;
}
