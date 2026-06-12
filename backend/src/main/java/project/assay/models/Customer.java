package project.assay.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.assay.models.enums.CustomerRole;
import project.assay.models.enums.CustomerStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.time.LocalDateTime.now;
import static java.util.List.of;
import static project.assay.models.enums.CustomerRole.USER;
import static project.assay.models.enums.CustomerStatus.ACTIVE;
import static project.assay.models.enums.CustomerStatus.PENDING;

@Entity
@Table(name = "customer")
@Setter
@Getter
@NoArgsConstructor
public class Customer implements UserDetails {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Enumerated(STRING)
    @Column(name = "status", nullable = false)
    private CustomerStatus status = PENDING;

    @Enumerated(STRING)
    @Column(name = "role",  nullable = false)
    private CustomerRole role = USER;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = now();

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name = "password",  nullable = false)
    private String password;

    @OneToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToMany(fetch = LAZY, mappedBy = "customer", cascade = ALL, orphanRemoval = true)
    private List<VerificationToken> tokens = new ArrayList<>();

    @Override
    public boolean isEnabled() {
        return status == ACTIVE && isVerified;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void enable() {
        this.status = ACTIVE;
        this.isVerified = true;
    }
}
