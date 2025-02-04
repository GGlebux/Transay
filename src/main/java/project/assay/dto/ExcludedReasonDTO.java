package project.assay.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ExcludedReasonDTO {
  private int id;
  private String reason;
}
