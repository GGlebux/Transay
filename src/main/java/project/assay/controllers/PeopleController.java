package project.assay.controllers;


import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.PersonDTO;
import project.assay.dto.PersonUpdateDTO;
import project.assay.exceptions.PersonNotCreatedException;
import project.assay.services.PeopleService;

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
     * @return PersonDTO - информация о конкретном человеке
     */
    @GetMapping("/{personId}")
    public ResponseEntity<PersonDTO> show(@PathVariable("personId") int personId) {
        return peopleService.findById(personId);
    }

    /**
     * Создает человека
     *
     * @param personDTO
     */
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid PersonDTO personDTO,
                                         BindingResult bindingResult) {
        throwValidException(bindingResult);
        return peopleService.save(personDTO);
    }

    /**
     * Обновляет данные человека
     *
     * @param personUpdateDTO
     */
    @PatchMapping("/{personId}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid PersonUpdateDTO personUpdateDTO,
                                             @PathVariable("personId") int personId, BindingResult bindingResult) {
        throwValidException(bindingResult);
        return peopleService.update(personId, personUpdateDTO);
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
    /**
     * Возвращает клиенту ошибки валидации
     *
     * @param bindingResult
     */
    private void throwValidException(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errMsg = new HashMap<>();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errMsg.put(error.getField(), error.getDefaultMessage());
            }
            throw new PersonNotCreatedException(errMsg.toString());
        }
    }



}
