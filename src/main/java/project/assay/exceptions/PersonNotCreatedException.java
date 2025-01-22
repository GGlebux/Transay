package project.assay.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PersonNotCreatedException extends RuntimeException {
    public PersonNotCreatedException(String message) {
        super(message);
    }

}
