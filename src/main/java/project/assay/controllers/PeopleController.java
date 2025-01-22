package project.assay.controllers;


import jakarta.validation.Valid;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.PersonDTO;
import project.assay.dto.PersonUpdateDTO;
import project.assay.dto.ReasonDTO;
import project.assay.exceptions.PersonNotCreatedException;
import project.assay.models.Person;
import project.assay.models.Reason;
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
  private final ModelMapper modelMapper;


  @Autowired
  public PeopleController(PeopleService peopleService, ModelMapper modelMapper) {
    this.peopleService = peopleService;
    this.modelMapper = modelMapper;
  }

  /**
   * @return PersonDTO - информация о конкретном человеке
   */
  @GetMapping("/{personId}")
  public ResponseEntity<PersonDTO> show(@PathVariable("personId") int personId) {
    Person person = peopleService.findById(personId);
    return ResponseEntity.ok(converToPersonDTO(person));
  }

  /**
   * Создает человека
   * @param personDTO
   */
  @PostMapping
  public ResponseEntity<String> create(@RequestBody @Valid PersonDTO personDTO,
      BindingResult bindingResult) {
    throwValidException(bindingResult);
    Person savedPerson = peopleService.save(convertToPerson(personDTO));
    return ResponseEntity.created(URI.create("/people/" + savedPerson.getId()))
        .body("Created person with id=" + savedPerson.getId());
  }

  /**
   * Обновляет данные человека
   * @param personUpdateDTO
   */
  @PatchMapping("/{personId}")
  public ResponseEntity<HttpStatus> update(@RequestBody @Valid PersonUpdateDTO personUpdateDTO,
      @PathVariable("personId") int personId, BindingResult bindingResult) {
    throwValidException(bindingResult);
    Person person = peopleService.findById(personId);
    peopleService.update(personId, convertToPerson(personUpdateDTO, person));
    return ResponseEntity.ok(HttpStatus.ACCEPTED);
  }

  /**
   * Удаляет человека
   * @param personId
   */
  @DeleteMapping("/{personId}")
  public ResponseEntity<HttpStatus> delete(@PathVariable("personId") int personId) {
    peopleService.delete(personId);
    return ResponseEntity.ok(HttpStatus.OK);
  }

  /**
   * Возвращает клиенту ошибки валидации
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

  private Person convertToPerson(PersonDTO personDTO) {
    return modelMapper.map(personDTO, Person.class);
  }

  private Person convertToPerson(PersonUpdateDTO personUpdateDTO, Person preparedPerson) {
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    modelMapper.map(personUpdateDTO, preparedPerson);
    return preparedPerson;
  }

  private PersonDTO converToPersonDTO(Person person) {
    List<ReasonDTO> reasons = converToReasonDTO(person.getExcludedReasons());
    PersonDTO personDTO = modelMapper.map(person, PersonDTO.class);
    personDTO.setExcludedReasons(reasons);
    return personDTO;
  }

  private List<ReasonDTO> converToReasonDTO(List<Reason> reasons) {
    return reasons.stream().map((reason -> modelMapper.map(reason, ReasonDTO.class))).toList();
  }

  private ReasonDTO convertToReason(ReasonDTO reasonDTO) {
    return modelMapper.map(reasonDTO, ReasonDTO.class);
  }
}
