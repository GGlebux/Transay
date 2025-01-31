package project.assay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonDTO {
  @NotEmpty(message = "Name should not be empty")
  @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
  private String name;

  @NotEmpty(message = "Gender should not be empty")
  @Pattern(regexp = "^(male|female)$", message = "Gender should be 'male' or 'female'")
  private String gender;

  @NotNull(message = "Date_of_birthday should not be empty")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dateOfBirth;

  @NotNull(message = "isGravid should not be empty")
  private Boolean isGravid;

  private List<ExcludedReasonDTO> excludedReasons;
}
