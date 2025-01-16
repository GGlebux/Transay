package project.assay.controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.assay.exceptions.PersonNotFoundException;
import project.assay.utils.responces.PersonErrorResponse;
import project.assay.exceptions.PersonNotCreatedException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(PersonNotCreatedException.class)
  public ResponseEntity<PersonErrorResponse> handlePersonNotCreatedException(
      PersonNotCreatedException e) {
    PersonErrorResponse response = new PersonErrorResponse(
        e.getMessage(),
        LocalDateTime.now()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(PersonNotFoundException.class)
  private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
    PersonErrorResponse responce = new PersonErrorResponse(
        e.getMessage(),
        LocalDateTime.now()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responce);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<PersonErrorResponse> handleValidationException(
      MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();
    List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
    for (FieldError error : fieldErrors) {
      errors.put(error.getField(), error.getDefaultMessage());
    }
    PersonErrorResponse response = new PersonErrorResponse(errors.toString(), LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * @param ex
   * @return
   */
  @ExceptionHandler(InvalidFormatException.class)
  public ResponseEntity<String> handleInvalidFormatException(InvalidFormatException ex) {
    String errorField = ex.getPath().getFirst().getFieldName();
    if (errorField.equals("dateOfBirth")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(errorField + ": Invalid date format. Please use 'yyyy-MM-dd'");
    } else if (errorField.equals("isGravid")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(errorField + ": Invalid isGravid format. Please use BOOLEAN");
    }

    return new ResponseEntity<>(errorField + ": " + ex.getOriginalMessage(),
        HttpStatus.BAD_REQUEST);
  }
}
