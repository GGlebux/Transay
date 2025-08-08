package project.assay.controllers;


import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.PersonRequestDTO;
import project.assay.dto.requests.PersonToUpdateDTO;
import project.assay.exceptions.EntityNotCreatedException;
import project.assay.models.Person;
import project.assay.models.Reason;
import project.assay.services.PeopleService;

import static java.util.stream.Collectors.toMap;

/**
 * REST Контроллер для работы с сущностью Person. Предоставляет API endpoint`ы для клиента
 *
 * @author GGlebux
 */
@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;


    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    /**
     * @return PersonRequestDTO - информация о конкретном человеке
     */
    @GetMapping("/{personId}")
    public ResponseEntity<Person> show(@PathVariable("personId") int personId) {
        return peopleService.find(personId);
    }

    /**
     * Создает человека
     *
     * @param personRequestDTO
     */
    @PostMapping
    public ResponseEntity<Person> create(@RequestBody @Valid PersonRequestDTO personRequestDTO,
                                         BindingResult bindingResult) {
        throwValidException(bindingResult);
        return peopleService.save(personRequestDTO);
    }

    /**
     * Обновляет данные человека
     *
     * @param personToUpdateDTO
     */
    @PatchMapping("/{personId}")
    public ResponseEntity<Person> update(@RequestBody @Valid PersonToUpdateDTO personToUpdateDTO,
                                         @PathVariable("personId") int personId, BindingResult bindingResult) {
        throwValidException(bindingResult);
        return peopleService.update(personId, personToUpdateDTO);
    }

    /**
     * Удаляет человека
     *
     * @param personId
     */
    @DeleteMapping("/{personId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("personId") int personId) {
        return peopleService.delete(personId);
    }


    @GetMapping("/{personId}/ex_reasons")
    public ResponseEntity<List<Reason>> getAllExReasons(@PathVariable("personId") int personId) {
        return peopleService.findAllEx(personId);
    }

    @PostMapping("/{personId}/ex_reasons")
    public ResponseEntity<Reason> addExReason(@PathVariable("personId") int personId,
                                              @RequestBody Integer reasonId) {
        return peopleService.createEx(personId, reasonId);
    }

    @DeleteMapping("/{personId}/ex_reasons/{reasonId}")
    public ResponseEntity<HttpStatus> deleteExReason(@PathVariable("personId") int personId,
                                                     @PathVariable("reasonId") int reasonId) {
        return peopleService.deleteEx(personId, reasonId);
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
