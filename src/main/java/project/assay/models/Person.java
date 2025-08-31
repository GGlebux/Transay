package project.assay.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static java.lang.String.format;
import static java.time.LocalDate.now;
import static java.util.Set.of;
import static project.assay.utils.StaticMethods.*;


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
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "is_gravid")
    private Boolean isGravid;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "excluded_reason",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "reason_id")
    )
    private Set<Reason> excludedReasons = new TreeSet<>();

    @OneToMany(mappedBy = "person", orphanRemoval = true, fetch = LAZY)
    private Set<Measure> measures = of();


    @Override
    public String toString() {
        return format("Человек: имя='%s',\nпол='%s',\nвозраст в днях='%d',\nбеременность='%s'",
                name,
                genderToWord(gender),
                getDaysBetween(dateOfBirth, now()),
                boolToWord(isGravid));

    }
}
