package project.assay.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.assay.models.Reason;
import project.assay.services.ReasonsService;

import java.util.List;

import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.*;
import static project.assay.models.Roles.EDITOR;
import static project.assay.models.Roles.USER;

@RestController
@RequestMapping("/api/reasons")
@PreAuthorize(EDITOR)
@Tag(name = "Причины (повышения/понижения)")
public class ReasonsController {

    private final ReasonsService reasonsService;

    @Autowired
    public ReasonsController(ReasonsService reasonsService) {
        this.reasonsService = reasonsService;
    }

    @GetMapping
    @PreAuthorize(USER)
    @Operation(summary = "Получить список всех причин")
    public ResponseEntity<List<Reason>> getAllReasons() {
        return ok(reasonsService.findAllWithResponse());
    }

    @PostMapping
    @Operation(summary = "Создать причину")
    public ResponseEntity<Reason> createReason(@RequestBody String name) {
        var response = reasonsService.create(name);
        var uri = create("/api/reasons/" + response.getId());
        return created(uri).body(response);
    }

    @GetMapping("/{reasonId}")
    @PreAuthorize(USER)
    @Operation(summary = "Получить причину")
    public ResponseEntity<Reason> getReason(@PathVariable int reasonId) {
        return ok(reasonsService.findByIdWithResponse(reasonId));
    }

    @PatchMapping("/{reasonId}")
    @Operation(summary = "Обновить причину")
    public ResponseEntity<Reason> updateReason(@RequestBody String name,
                                               @PathVariable int reasonId) {
        return ok(reasonsService.update(name, reasonId));
    }

    @DeleteMapping("/{reasonId}")
    @Operation(summary = "Удалить причину")
    public ResponseEntity<HttpStatus> deleteReason(@PathVariable int reasonId) {
        reasonsService.delete(reasonId);
        return noContent().build();
    }
}
