package project.assay.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EntityNotFoundException extends BaseAppException {
    public EntityNotFoundException(String message) {
        super(message, BAD_REQUEST, "Сущность не найдена!");
    }
}
