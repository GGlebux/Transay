package project.assay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import jdk.jfr.BooleanFlag;
import lombok.Data;

@Data
public class PersonUpdateDTO {
  @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
  private String name;

  @Pattern(regexp = "^(male|female)$", message = "Gender should be 'male' or 'female'")
  private String gender;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dateOfBirth;

  @BooleanFlag
  private Boolean isGravid;

  private List<ReasonDTO> excludedReasons;
}
