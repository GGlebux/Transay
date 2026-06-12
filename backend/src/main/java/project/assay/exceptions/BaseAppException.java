package project.assay.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class BaseAppException extends RuntimeException {
    private final HttpStatus status;
    private final String title;
    private Map<String, Object> properties = new HashMap<>();

    public BaseAppException(String message,
                            HttpStatus status,
                            String title) {
        super(message);
        this.status = status;
        this.title = title;
    }

    public BaseAppException(String message,
                            HttpStatus status,
                            String title,
                            Throwable cause) {
        super(message, cause);
        this.status = status;
        this.title = title;
    }


    public BaseAppException addProperty(String key, Object value) {
        this.properties.put(key, value);
        return this;
    }
}
