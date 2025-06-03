package project.assay.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import project.assay.models.AgeRange;

@Data
public class IndicatorRequestDTO {
    @NotNull(message = "eng_name should not be empty")
    private String eng_name;

    @NotNull(message = "rus_name should not be empty")
    private String rus_name;

    @Pattern(regexp = "^(male|female)$", message = "Gender should be 'male' or 'female'")
    private String gender;

    @NotNull(message = "isGravid should not be empty")
    private boolean isGravid;

    @NotNull(message = "minAge should not be empty")
    private AgeRange minAge;

    @NotNull(message = "maxAge should not be empty")
    private AgeRange maxAge;

    @NotNull(message = "minValue should not be empty")
    private double minValue;

    @NotNull(message = "maxValue should not be empty")
    private double maxValue;

    private String units;
}
