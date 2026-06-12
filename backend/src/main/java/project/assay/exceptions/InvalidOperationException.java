package project.assay.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class InvalidOperationException extends BaseAppException {
    public InvalidOperationException(String message) {
        super(message, BAD_REQUEST, "Некорректная операция!");
    }
}
