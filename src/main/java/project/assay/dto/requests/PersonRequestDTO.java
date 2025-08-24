package project.assay.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonRequestDTO  {
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Pattern(regexp = "^(male|female)$", message = "Gender should be 'male' or 'female'")
    @NotEmpty(message = "Gender should not be empty")
    private String gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date_of_birthday should not be empty")
    private LocalDate dateOfBirth;

    @NotNull(message = "isGravid should not be empty")
    private Boolean isGravid;
}
