package project.assay.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExcludedReasonDTO {
  private int id;
  private String reason;
}
