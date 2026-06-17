package project.assay.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.assay.models.enums.Condition;
import project.assay.models.enums.PersonGender;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static java.lang.String.format;
import static java.time.LocalDate.now;
import static java.util.Set.of;
import static project.assay.utils.StaticMethods.getDaysBetween;


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
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(STRING)
    @Column(name = "gender")
    private PersonGender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(STRING)
    @Column(name = "condition")
    private Condition condition;

    @ManyToMany(fetch = LAZY, cascade = {MERGE, PERSIST, REFRESH})
    @JoinTable(
            name = "excluded_reason",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "reason_id")
    )
    private Set<Reason> excludedReasons = new TreeSet<>();

    @OneToMany(mappedBy = "person", orphanRemoval = true, fetch = LAZY)
    private Set<Measure> measures = of();

    @OneToOne(mappedBy = "person")
    private Customer customer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner_id")
    private Customer owner;

    @Override
    public String toString() {
        return format("Человек: имя='%s',\nпол='%s',\nвозраст в днях='%d',\nсостояние='%s'",
                name,
                gender.getValue(),
                getDaysBetween(dateOfBirth, now()),
                condition.getValue());
    }
}
