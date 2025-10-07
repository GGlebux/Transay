package project.assay.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import static jakarta.persistence.GenerationType.IDENTITY;
import static java.lang.Integer.compare;

@Entity
@Table(name = "reason", indexes = @Index(columnList = "name"))
@Data
@NoArgsConstructor
public class Reason implements Comparable<Reason>{
    @Override
    public int compareTo(@NotNull Reason o) {
        return compare(this.id, o.id);
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    public Reason(String name) {
        this.name = name;
    }
}
