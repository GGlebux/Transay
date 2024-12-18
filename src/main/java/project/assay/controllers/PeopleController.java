package project.assay.controllers;


import jakarta.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

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
import project.assay.utils.PersonErrorResponce;

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

    @GetMapping()
    public List<PersonDTO> list() {
        return peopleService.findAll().stream().map(this::converToPersonDTO).toList();
    }

    @GetMapping("/{id}")
    public PersonDTO show(@PathVariable("id") int id) {
        return converToPersonDTO(peopleService.findById(id)); // TODO
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody @Valid PersonDTO personDTO,
                                         BindingResult bindingResult) throws URISyntaxException {
        throwValidException(bindingResult);
        Person savedPerson = peopleService.save(converToPerson(personDTO));
        return ResponseEntity.ok(converToPersonDTO(savedPerson).getId());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid PersonDTO personDTO, @PathVariable("id") int id,
                         BindingResult bindingResult) {
        throwValidException(bindingResult);
        peopleService.update(id, converToPerson(personDTO));
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private void throwValidException(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errMsg.append(error.getField()).append(": ")
                        .append(error.getDefaultMessage())
                        .append("; ");
            }
            throw new PersonNotCreatedException(errMsg.toString());
        }
    }

    private Person converToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO converToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponce> handleException(PersonNotFoundException e) {
        PersonErrorResponce responce = new PersonErrorResponce(
                "Person with this id was`n found!",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(responce, HttpStatus.NOT_FOUND); // NOT_FOUND - 404 статус
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponce> handleException(PersonNotCreatedException e) {
        PersonErrorResponce responce = new PersonErrorResponce(
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(responce, HttpStatus.BAD_REQUEST); // BAD_REQUEST
    }
}
