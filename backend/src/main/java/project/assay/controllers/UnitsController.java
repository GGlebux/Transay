package project.assay.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.assay.models.Unit;
import project.assay.services.UnitsService;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;
import static project.assay.models.Roles.EDITOR;
import static project.assay.models.Roles.USER;
import static project.assay.utils.StaticMethods.getLocation;

@RestController
@RequestMapping("/api/indicators/units")
@PreAuthorize(EDITOR)
@Tag(name = "Единицы измерения")
public class UnitsController {
    private final UnitsService service;

    @Autowired
    public UnitsController(UnitsService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize(USER)
    @Operation(summary = "Получить все единицы измерения")
    public ResponseEntity<List<Unit>> findAll() {
        return ok(service.findAll());
    }

    @PostMapping
    @Operation(summary = "Сохранить единицу измерения")
    public ResponseEntity<Unit> save(@RequestBody String name) {
        var response = service.save(name);
        return created(getLocation(response.getId())).body(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить единицу измерения")
    public ResponseEntity<Unit> update(@PathVariable int id, @RequestBody String unit) {
        return ok(service.update(id, unit));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить единицу измерения")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id) {
        service.delete(id);
        return noContent().build();
    }
}
