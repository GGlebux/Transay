package project.assay.exceptions;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class InvalidAccessTokenException extends BaseAppException {
  public InvalidAccessTokenException(String message) {
    super(message, UNAUTHORIZED, "Некорректный токен доступа!");
  }
}
