package project.assay.exceptions;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Ошибка при обращении к БД (например, сбой SQL-функции расчёта).
 * Пробрасывается клиенту в едином формате ProblemDetail через GlobalExceptionHandler.
 */
public class DatabaseException extends BaseAppException {
    public DatabaseException(String message, Throwable cause) {
        super(message, INTERNAL_SERVER_ERROR, "Ошибка базы данных", cause);
    }
}
