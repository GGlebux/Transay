package project.assay.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static java.util.Set.of;


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
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "is_gravid")
    private Boolean isGravid;

    @OneToMany(fetch = EAGER, cascade = ALL)
    @JoinTable(
            name = "excluded_reason",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "reason_id")
    )
    private Set<Reason> excludedReasons = new TreeSet<>();

    @OneToMany(mappedBy = "person", orphanRemoval = true, fetch = LAZY)
    private Set<Measure> measures = of();
}
