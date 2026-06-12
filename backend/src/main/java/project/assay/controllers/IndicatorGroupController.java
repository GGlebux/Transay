package project.assay.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.IndicatorGroupRequestDTO;
import project.assay.dto.responses.IndicatorGroupResponseDTO;
import project.assay.services.IndicatorGroupService;

import java.util.List;

import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.*;
import static project.assay.models.Roles.EDITOR;
import static project.assay.models.Roles.USER;

@RestController
@RequestMapping("/api/groups")
@PreAuthorize(EDITOR)
@Tag(name = "Группы индикаторов")
public class IndicatorGroupController {
    private final IndicatorGroupService service;

    @Autowired
    public IndicatorGroupController(IndicatorGroupService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize(USER)
    @Operation(summary = "Получить все группы")
    public ResponseEntity<List<IndicatorGroupResponseDTO>> findAll() {
        return ok(service.findAllWithResponse());
    }

    @PostMapping
    @Operation(summary = "Создать группу индикаторов")
    public ResponseEntity<IndicatorGroupResponseDTO> createGroup(@RequestBody IndicatorGroupRequestDTO dto) {
        var response = service.save(dto);
        var uri = create("/api/groups/" + response.getId());
        return created(uri).body(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить группу индикаторов")
    public ResponseEntity<IndicatorGroupResponseDTO> updateGroup(@PathVariable int id,
                                                       @RequestBody IndicatorGroupRequestDTO dto){
        return ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить группу индикаторов")
    public ResponseEntity<HttpStatus> deleteGroup(@PathVariable int id){
        service.delete(id);
        return noContent().build();
    }
}
