package project.assay.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException(String message) {
        super(message);
    }
}
