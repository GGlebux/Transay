package project.assay.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class InvalidVerificationTokenException extends BaseAppException {
    public InvalidVerificationTokenException(String message) {
        super(message, BAD_REQUEST, "Некорректный токен верификации!");
    }
}
