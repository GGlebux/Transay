package project.assay.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReasonDTO {
  @JsonIgnore
  private int id;
  private String reason;
}
