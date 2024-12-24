package project.assay.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
  @NotEmpty(message = "Gender should not be empty")
  private String gender;

  @Column(name = "date_of_birth")
  @NotNull(message = "Date_of_birthday should not be empty")
  private LocalDate dateOfBirth;

  @Column(name = "is_gravid")
  private Boolean isGravid;

  @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
  private List<Reason> excludedReasons;

//  @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//  private List<PersonInfo> personIndicators;
}
