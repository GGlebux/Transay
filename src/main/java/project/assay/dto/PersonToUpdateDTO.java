package project.assay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PersonToUpdateDTO {
  @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
  private String name;

  @Pattern(regexp = "^(male|female|both)$", message = "Gender should be 'male' or 'female' or 'both'")
  private String gender;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dateOfBirth;

  private Boolean isGravid;

  private List<ExcludedReasonDTO> excludedReasons;
}
