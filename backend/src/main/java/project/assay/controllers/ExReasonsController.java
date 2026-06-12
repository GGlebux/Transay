package project.assay.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.assay.models.Reason;
import project.assay.services.ExReasonService;

import java.util.List;
import java.util.Set;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static project.assay.models.Roles.USER;

@RestController
@RequestMapping("/api/people/ex_reasons")
@PreAuthorize(USER)
@Tag(name = "Исключенные причины людей")
public class ExReasonsController {
    private final ExReasonService service;

    @Autowired
    public ExReasonsController(ExReasonService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Получить список исключенных причин")
    public ResponseEntity<List<Reason>> getAllExReasons() {
        return ok(service.findAllExWithResponse());
    }

    @PostMapping
    @Operation(summary = "Добавить список исключенных причин (id-шники)")
    public ResponseEntity<List<Reason>> addExManyReason(@RequestBody Set<Integer> reasons) {
        return ok(service.addExcludedReasons(reasons));
    }

    @DeleteMapping("/{reasonId}")
    @Operation(summary = "Удалить исключенную причину")
    public ResponseEntity<HttpStatus> deleteExReason(@PathVariable("reasonId") Integer reasonId) {
        service.deleteEx(reasonId);
        return noContent().build();
    }
}
