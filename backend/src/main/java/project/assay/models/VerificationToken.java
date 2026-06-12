package project.assay.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Entity
@Table(name = "verification_token")
@Setter
@Getter
@NoArgsConstructor
public class VerificationToken {
    @Id
    @Column
    private String token;

    @ManyToOne
    private Customer customer;

    private LocalDateTime expiresAt =  now().plusMinutes(5);

    public VerificationToken(Customer customer, String token) {
        this.customer = customer;
        this.token = token;
    }
}
