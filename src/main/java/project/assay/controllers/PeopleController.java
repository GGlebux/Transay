package project.assay.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.PersonRequestDTO;
import project.assay.dto.responses.PersonResponseDTO;
import project.assay.exceptions.EntityNotCreatedException;
import project.assay.models.Reason;
import project.assay.services.PeopleService;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

/**
 * REST Контроллер для работы с сущностью Person. Предоставляет API endpoint`ы для клиента
 *
 * @author GGlebux
 */
@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService service;


    @Autowired
    public PeopleController(PeopleService service) {
        this.service = service;
    }

    /**
     * @return @code{List<Person>} - список всех людей
     */
    @GetMapping
    public ResponseEntity<List<PersonResponseDTO>> getAllPeople() {
        return service.findAll();
    }

    /**
     * @return PersonRequestDTO - информация о конкретном человеке
     */
    @GetMapping("/{personId}")
    public ResponseEntity<PersonResponseDTO> show(@PathVariable("personId") int personId) {
        return service.find(personId);
    }

    /**
     * Создает человека
     *
     * @param personRequestDTO
     */
    @PostMapping
    public ResponseEntity<PersonResponseDTO> create(@RequestBody @Valid PersonRequestDTO personRequestDTO,
                                         BindingResult bindingResult) {
        throwValidException(bindingResult);
        return service.save(personRequestDTO);
    }

    /**
     * Обновляет данные человека
     *
     * @param personRequestDTO
     */
    @PatchMapping("/{personId}")
    public ResponseEntity<PersonResponseDTO> update(@RequestBody @Valid PersonRequestDTO personRequestDTO,
                                         @PathVariable("personId") int personId, BindingResult bindingResult) {
        throwValidException(bindingResult);
        return service.update(personId, personRequestDTO);
    }

    /**
     * Удаляет человека
     *
     * @param personId
     */
    @DeleteMapping("/{personId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("personId") int personId) {
        return service.delete(personId);
    }


    @GetMapping("/{personId}/ex_reasons")
    public ResponseEntity<Set<Reason>> getAllExReasons(@PathVariable("personId") int personId) {
        return service.findAllExWithResponse(personId);
    }


    @PostMapping("/{personId}/ex_reasons")
    public ResponseEntity<Set<Reason>> addExManyReason(@PathVariable("personId") int personId,
                                              @RequestBody Set<Integer> reasons) {
        return service.createManyEx(personId, reasons);
    }

    @DeleteMapping("/{personId}/ex_reasons/{reasonId}")
    public ResponseEntity<HttpStatus> deleteExReason(@PathVariable("personId") int personId,
                                                     @PathVariable("reasonId") Integer reasonId) {
        return service.deleteEx(personId, reasonId);
    }


    /**
     * Возвращает клиенту ошибки валидации
     *
     * @param bindingResult
     */
    private void throwValidException(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errMsg = bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(toMap(FieldError::getField, FieldError::getDefaultMessage));
            throw new EntityNotCreatedException(errMsg.toString());
        }
    }


}
