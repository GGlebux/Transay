package project.transay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.transay.models.Unit;
import project.transay.services.UnitsService;

import java.util.List;

@RestController
@RequestMapping("/indicators/units")
public class UnitsController {
    private final UnitsService service;

    @Autowired
    public UnitsController(UnitsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Unit>> findAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Unit> save(@RequestBody String name) {
        return service.save(name);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Unit> update(@PathVariable int id, @RequestBody String unit) {
        return service.update(id, unit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id) {
        return service.delete(id);
    }
}
