package project.assay.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ValidationException extends BaseAppException {
    public ValidationException(String message) {
        super(message, BAD_REQUEST, "Ошибка валидации данных!");
    }
}
