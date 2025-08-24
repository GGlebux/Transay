package project.assay.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class TranscriptRequestDTO {
    @NotNull(message = "Name should not be empty")
    private String name;
    @NotNull(message = "Gender should not be empty")
    private String gender;
    private Set<Integer> fallsIds;
    private Set<Integer> raisesIds;
}
