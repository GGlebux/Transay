package project.assay.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.ErrorResponse.create;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotCreatedException.class)
    public ErrorResponse handleEntityNotCreatedException(EntityNotCreatedException e) {
        return create(e, BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse handleNoSuchElementException(NoSuchElementException e) {
        return create(e, BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        return create(e, NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return create(e, BAD_REQUEST, errors.toString());
    }


    @ExceptionHandler(InvalidFormatException.class)
    public ErrorResponse handleInvalidFormatException(InvalidFormatException e) {
        String errorField = e.getPath().getFirst().getFieldName();
        String errorMessage;
        if (errorField.equals("dateOfBirth")) {
            errorMessage = errorField + ": Недопустимый формат даты. Пожалуйста используйте 'yyyy-MM-dd'";
        } else if (errorField.equals("isGravid")) {
            errorMessage = errorField + ": Недопустимый формат isGravid. Пожалуйста используйте BOOLEAN";
        } else {
            errorMessage = e.getLocalizedMessage();
        }
        return create(e, BAD_REQUEST, errorMessage);
    }
}
