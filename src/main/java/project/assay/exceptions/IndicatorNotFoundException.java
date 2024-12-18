package project.assay.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IndicatorNotFoundException extends RuntimeException {

  public IndicatorNotFoundException(String message) {
    super(message);
  }
}
