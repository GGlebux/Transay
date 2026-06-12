package project.assay.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.PersonRequestDTO;
import project.assay.dto.responses.PersonResponseDTO;
import project.assay.services.PeopleService;

import java.util.List;

import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.*;
import static project.assay.models.Roles.EDITOR;
import static project.assay.models.Roles.USER;


@RestController
@RequestMapping("/api/people")
@PreAuthorize(USER)
@Tag(name = "Люди - личные данные")
public class PeopleController {

    private final PeopleService service;


    @Autowired
    public PeopleController(PeopleService service) {
        this.service = service;
    }


    @GetMapping("/all")
    @PreAuthorize(EDITOR)
    @Operation(summary = "Получить список всех людей")
    public ResponseEntity<List<PersonResponseDTO>> getAllPeople() {
        return ok(service.findAll());
    }


    @GetMapping
    @Operation(summary = "Получить данные конкретного человека")
    public ResponseEntity<PersonResponseDTO> showPerson() {
        return ok(service.find());
    }


    @PostMapping
    @Operation(summary = "Создать человека")
    public ResponseEntity<PersonResponseDTO> createPerson(@RequestBody @Valid PersonRequestDTO personRequestDTO) {
        var response = service.save(personRequestDTO);
        var uri = create("/api/people/" + response.getId());
        return created(uri).body(response);
    }

    @PatchMapping
    @Operation(summary = "Обновить человека")
    public ResponseEntity<PersonResponseDTO> updatePerson(@RequestBody @Valid PersonRequestDTO personRequestDTO) {
        return ok(service.update(personRequestDTO));
    }

    @DeleteMapping
    @Operation(summary = "Удалить человека")
    public ResponseEntity<HttpStatus> delete() {
        service.delete();
        return noContent().build();
    }

}
