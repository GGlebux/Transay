package project.assay.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.net.URI.create;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BaseAppException.class)
    public ResponseEntity<ProblemDetail> handleBaseAppException(BaseAppException e, HttpServletRequest request) {
        ProblemDetail problemDetail = forStatusAndDetail(e.getStatus(), e.getMessage());
        problemDetail.setTitle(e.getTitle());
        problemDetail.setInstance(create(request.getRequestURI()));

        if (e.getProperties() != null && !e.getProperties().isEmpty()) {
            e.getProperties().forEach(problemDetail::setProperty);
        }

        return ResponseEntity.status(e.getStatus()).body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        // Собираем все ошибки валидации в Map
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        // Формируем детальное сообщение
        String detailMessage = format("Ошибка валидации для %s полей: %s",
                fieldErrors.size(),
                fieldErrors
        );

        // Создаем ProblemDetail с кастомными полями
        ProblemDetail problemDetail = forStatusAndDetail(BAD_REQUEST, detailMessage);
        problemDetail.setTitle("Ошибка валидации");
        problemDetail.setProperty("errors", fieldErrors);
        problemDetail.setInstance(create(request.getContextPath()));

        return new ResponseEntity<>(problemDetail, headers, BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String detailMessage = "Ошибка чтения тела запроса. ";
        Throwable cause = ex.getMostSpecificCause();

        if (cause instanceof InvalidFormatException invalidFormatEx) {
            String fieldName = invalidFormatEx.getPath().getFirst().getFieldName();
            Object value = invalidFormatEx.getValue();
            Class<?> targetType = invalidFormatEx.getTargetType();

            if (targetType == LocalDate.class) {
                detailMessage += format("Поле '%s' имеет неверный формат даты '%s'. Ожидаемый формат: dd.MM.yyyy (например, 16.08.2006)",
                        fieldName, value);
            } else if (targetType == Boolean.class || targetType == boolean.class) {
                detailMessage += format("Поле '%s' имеет неверное значение '%s'. Ожидается true или false",
                        fieldName, value);
            } else {
                detailMessage += format("Поле '%s' имеет неверное значение '%s'. Ожидается тип: %s",
                        fieldName, value, targetType.getSimpleName());
            }
        }
        else if (cause.getMessage() != null && cause.getMessage().contains("LocalDate")) {
            detailMessage += "Проблема с полем 'dateOfBirth'. Убедитесь, что дата в формате  dd.MM.yyyy (например, 16.08.2006)";
        }
        else {
            String message = cause.getMessage();
            if (message != null) {
                if (message.contains("LocalDate") || message.contains("dateOfBirth")) {
                    detailMessage += "Поле 'dateOfBirth' имеет неверный формат. Ожидаемый формат: dd.MM.yyyy (например, 16.08.2006)";
                } else if (message.contains("gender")) {
                    detailMessage += "Поле 'gender' имеет неверное значение. Ожидается 'male' или 'female'";
                } else if (message.contains("isGravid")) {
                    detailMessage += "Поле 'isGravid' имеет неверное значение. Ожидается true или false";
                } else {
                    detailMessage += "Проверьте тело запроса и типы полей. " + message;
                }
            } else {
                detailMessage += "Проверьте тело запроса и типы полей.";
            }
        }

        ProblemDetail problemDetail = forStatusAndDetail(status, detailMessage);
        problemDetail.setTitle("Неправильно сформированный запрос JSON");
        problemDetail.setInstance(URI.create(request.getContextPath()));

        problemDetail.setProperty("timestamp", now());

        return new ResponseEntity<>(problemDetail, headers, status);
    }
}
