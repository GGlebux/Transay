package project.assay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assay.models.Reason;
import project.assay.services.ReasonsService;

import java.util.List;

/**
 * REST Контроллер для работы с сущностью Reason (причины повышения/понижения индикаторов).
 * Предоставляет API endpoint`ы для админа
 *
 * @author GGlebux
 */

@RestController
@RequestMapping("/reasons")
public class ReasonsController {

    private final ReasonsService reasonsService;

    @Autowired
    public ReasonsController(ReasonsService reasonsService) {
        this.reasonsService = reasonsService;
    }

    @GetMapping
    public ResponseEntity<List<Reason>> getAllReasons() {
        return reasonsService.findAllWithResponse();
    }

    @PostMapping
    public ResponseEntity<Reason> createReason(@RequestBody String name) {
        return reasonsService.create(name);
    }

    @GetMapping("/{reasonId}")
    public ResponseEntity<Reason> getReason(@PathVariable int reasonId) {
        return reasonsService.findByIdWithResponse(reasonId);
    }

    @PatchMapping("/{reasonId}")
    public ResponseEntity<Reason> updateReason(@RequestBody String name,
                                               @PathVariable int reasonId) {
        return reasonsService.update(name, reasonId);
    }

    @DeleteMapping("/{reasonId}")
    public ResponseEntity<HttpStatus> deleteReason(@PathVariable int reasonId) {
        return reasonsService.delete(reasonId);
    }
}
