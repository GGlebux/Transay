package project.assay.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.List.of;

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

    @OneToOne(fetch = EAGER)
    @JoinColumn(name = "transcript_id", referencedColumnName = "id")
    private Transcript transcript;

    @Override
    public String toString() {
        return "Referent{" +
                "id=" + id +
                ", currentValue=" + currentValue +
                ", regDate=" + regDate +
                ", status='" + status + '\'' +
                ", versict=" + transcript +
                '}';
    }

    public List<Reason> getVerdict(){
        return switch (status) {
            case "fall" -> transcript.getFalls();
            case "raise" -> transcript.getRaises();
            default -> of();
        };
    }

}
