package project.assay.models;


import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import project.assay.utils.converters.JsonToListConverter;

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

  @Convert(converter = JsonToListConverter.class)
  @Column(name = "fall", columnDefinition = "jsonb")
  private List<String> fall;

  @Convert(converter = JsonToListConverter.class)
  @Column(name = "raise", columnDefinition = "jsonb")
  private List<String> raise;
}
