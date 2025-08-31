package project.assay.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.stream.Collectors.toSet;

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

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "referent_transcript",
            joinColumns = @JoinColumn(name = "referent_id"),
            inverseJoinColumns = @JoinColumn(name = "transcript_id")
    )
    private Set<Transcript> transcripts;

    @Override
    public String toString() {
        return "Referent{" +
                "id=" + id +
                ", currentValue=" + currentValue +
                ", regDate=" + regDate +
                ", status='" + status + '\'' +
                ", verdict=" + transcripts +
                '}';
    }

    public Set<Reason> getVerdict(){
        return switch (status) {
            case "fall" -> transcripts
                    .stream()
                    .flatMap(t -> t.getFalls().stream())
                    .collect(toSet());
            case "raise" -> transcripts
                    .stream()
                    .flatMap(t -> t.getRaises().stream())
                    .collect(toSet());
            default -> new HashSet<>();
        };
    }

}
