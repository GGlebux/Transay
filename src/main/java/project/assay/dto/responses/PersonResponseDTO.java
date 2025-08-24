package project.assay.dto.responses;

import lombok.Data;
import project.assay.models.Reason;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
public class PersonResponseDTO {
    private int id;
    private String name;
    private String gender;
    private LocalDate dateOfBirth;
    private Boolean isGravid;
    private Set<Reason> excludedReasons = new TreeSet<>();
}
