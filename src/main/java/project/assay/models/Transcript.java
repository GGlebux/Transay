package project.assay.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.Set.of;

@Entity
@Table(name = "transcript")
@Data
public class Transcript {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "name")
  private String name;

  @Column(name = "gender")
  private String gender;

  @ManyToMany(fetch = EAGER)
  @JoinTable(
          name = "fall_reason",
          joinColumns = @JoinColumn(name = "transcript_id"),
          inverseJoinColumns = @JoinColumn(name = "reason_id")
  )
  private Set<Reason> falls = of();

  @ManyToMany(fetch = EAGER)
  @JoinTable(
          name = "raise_reason",
          joinColumns = @JoinColumn(name = "transcript_id"),
          inverseJoinColumns = @JoinColumn(name = "reason_id")
  )
  private Set<Reason> raises= of();
}
