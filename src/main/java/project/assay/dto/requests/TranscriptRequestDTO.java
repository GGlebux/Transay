package project.assay.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TranscriptRequestDTO {
    @NotNull(message = "Name should not be empty")
    private String name;
    @NotNull(message = "Gender should not be empty")
    private String gender;
    private List<Integer> fallsIds;
    private List<Integer> raisesIds;
}
