package project.assay.controllers;


import jakarta.validation.Valid;

import java.io.IOException;
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
import project.assay.exceptions.PersonNotCreatedException;
import project.assay.exceptions.PersonNotFoundException;
import project.assay.models.Person;
import project.assay.services.PeopleService;
import project.assay.utils.converters.JsonToListConverter;
import project.assay.utils.responces.PersonErrorResponce;

@RestController
@RequestMapping("/profile")
public class PeopleController {

  private final PeopleService peopleService;
  private final ModelMapper modelMapper;
  private final JsonToListConverter jsonToListConverter;

  @Autowired
  public PeopleController(PeopleService peopleService, ModelMapper modelMapper,
      JsonToListConverter jsonToListConverter) {
    this.peopleService = peopleService;
    this.modelMapper = modelMapper;
    this.jsonToListConverter = jsonToListConverter;
  }

  @GetMapping("/{id}")
  public ResponseEntity<PersonDTO> show(@PathVariable("id") int id) throws IOException {
    Person person = peopleService.findById(id);
    return ResponseEntity.ok(converToPersonDTO(person));
  }

  @PostMapping
  public ResponseEntity<Integer> create(@RequestBody @Valid PersonDTO personDTO,
      BindingResult bindingResult) throws IOException {
    throwValidException(bindingResult);
    if (bindingResult.hasErrors()) {

    }
    Person savedPerson = peopleService.save(converToPerson(personDTO));
    return ResponseEntity.created(URI.create("/profile/" + savedPerson.getId())).build();
  }

  @PatchMapping("/{id}")
  public ResponseEntity<HttpStatus> update(@RequestBody @Valid PersonDTO personDTO,
      @PathVariable("id") int id,
      BindingResult bindingResult) throws IOException {
    throwValidException(bindingResult);
    peopleService.update(id, converToPerson(personDTO));
    return ResponseEntity.ok(HttpStatus.ACCEPTED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
    peopleService.delete(id);
    return ResponseEntity.ok(HttpStatus.OK);
  }

  private Person converToPerson(PersonDTO personDTO) throws IOException {
    Person person = modelMapper.map(personDTO, Person.class);

    return person;
  }

  private PersonDTO converToPersonDTO(Person person) throws IOException {
    PersonDTO personDTO = modelMapper.map(person, PersonDTO.class);
    return personDTO;
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
