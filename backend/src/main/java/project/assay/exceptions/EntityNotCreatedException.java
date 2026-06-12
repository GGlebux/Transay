package project.assay.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EntityNotCreatedException extends BaseAppException {
    public EntityNotCreatedException(String message) {
        super(message, BAD_REQUEST, "Ошибка создания сущности!");
    }
}
