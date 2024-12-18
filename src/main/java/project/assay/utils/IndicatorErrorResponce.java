package project.assay.utils;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class IndicatorErrorResponce {
  private String message;
  private LocalDateTime timestamp;

  public IndicatorErrorResponce(String message, LocalDateTime timestamp) {
    this.message = message;
    this.timestamp = timestamp;
  }
}
