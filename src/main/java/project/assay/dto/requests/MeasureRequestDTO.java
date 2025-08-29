package project.assay.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class MeasureRequestDTO {
  @NotNull(message = "name should not be empty")
  private String name;

  @NotNull(message = "currentValue should not be empty")
  private double currentValue;

  @NotNull(message = "units should not be empty")
  private String units;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @NotNull(message = "regDate should not be empty")
  private LocalDate regDate;
}
