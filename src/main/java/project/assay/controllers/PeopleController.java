package project.assay.controllers;


import jakarta.validation.Valid;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.PersonDTO;
import project.assay.dto.ReasonDTO;
import project.assay.exceptions.PersonNotCreatedException;
import project.assay.exceptions.PersonNotFoundException;
import project.assay.models.Person;
import project.assay.models.Reason;
import project.assay.services.PeopleService;
import project.assay.utils.responces.PersonErrorResponce;

/**
 * REST Контроллер для работы с сущностью Person. Предоставляет API endpoint`ы для клиента
 *
 * @author GGlebux
 */
@RestController
@RequestMapping("/profile")
public class PeopleController {

  private final PeopleService peopleService;
  private final ModelMapper modelMapper;


  @Autowired
  public PeopleController(PeopleService peopleService, ModelMapper modelMapper) {
    this.peopleService = peopleService;
    this.modelMapper = modelMapper;
  }

  @GetMapping("/{personId}")
  public ResponseEntity<PersonDTO> show(@PathVariable("personId") int personId) {
    Person person = peopleService.findById(personId);
    return ResponseEntity.ok(converToPersonDTO(person));
  }

  @PostMapping
  public ResponseEntity<Integer> create(@RequestBody @Valid PersonDTO personDTO,
      BindingResult bindingResult) {
    throwValidException(bindingResult);
    Person savedPerson = peopleService.save(converToPerson(personDTO));
    return ResponseEntity.created(URI.create("/profile/" + savedPerson.getId())).body(savedPerson.getId());
  }

  @PatchMapping("/{personId}")
  public ResponseEntity<HttpStatus> update(@RequestBody @Valid PersonDTO personDTO,
      @PathVariable("personId") int personId,
      BindingResult bindingResult) {
    Person person = peopleService.findById(personId);
    throwValidException(bindingResult);
    peopleService.update(personId, converToPerson(personDTO));
    return ResponseEntity.ok(HttpStatus.ACCEPTED);
  }

  @DeleteMapping("/{personId}")
  public ResponseEntity<HttpStatus> delete(@PathVariable("personId") int personId) {
    peopleService.delete(personId);
    return ResponseEntity.ok(HttpStatus.OK);
  }

  private Person converToPerson(PersonDTO personDTO) {
    return modelMapper.map(personDTO, Person.class);
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

  @ExceptionHandler
  private ResponseEntity<PersonErrorResponce> handleException(PersonNotFoundException e) {
    PersonErrorResponce responce = new PersonErrorResponce(
        e.getMessage(),
        LocalDateTime.now()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responce);
  }

  @ExceptionHandler
  private ResponseEntity<PersonErrorResponce> handleException(PersonNotCreatedException e) {
    PersonErrorResponce responce = new PersonErrorResponce(
        e.getMessage(),
        LocalDateTime.now()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responce);
  }
}
