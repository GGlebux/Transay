package project.assay.models;

import jakarta.persistence.*;
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

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static java.util.List.of;


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

    @OneToMany(fetch = EAGER, cascade = ALL)
    @JoinTable(
            name = "excluded_reason",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "reason_id")
    )
    private List<Reason> excludedReasons = of();

    @OneToMany(mappedBy = "person", cascade = ALL, orphanRemoval = true, fetch = LAZY)
    private List<Measure> measureList = of();
}
