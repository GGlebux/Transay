package project.assay.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "name")
  @NotEmpty(message = "Name should not be empty")
  @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
  private String name;

  @Column(name = "gender")
  @Pattern(regexp = "^(male|female)$", message = "Gender should be 'male' or 'female'")
  @NotEmpty(message = "Gender should not be empty")
  private String gender;

  @Column(name = "date_of_birth")
  @NotNull(message = "Date_of_birthday should not be empty")
  private LocalDate dateOfBirth;

  @Column(name = "is_gravid")
  private Boolean isGravid;

  @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<Reason> excludedReasons;

  @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
  private List<Measure> measureList;
}
