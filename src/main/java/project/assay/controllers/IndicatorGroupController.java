package project.assay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.IndicatorGroupRequestDTO;
import project.assay.dto.responses.IndicatorGroupResponseDTO;
import project.assay.services.IndicatorGroupService;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class IndicatorGroupController {
    private final IndicatorGroupService service;

    @Autowired
    public IndicatorGroupController(IndicatorGroupService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<IndicatorGroupResponseDTO>> findAll() {
        return service.findAllWithResponse();
    }

    @PostMapping
    public ResponseEntity<IndicatorGroupResponseDTO> create(@RequestBody IndicatorGroupRequestDTO dto) {
        return service.save(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<IndicatorGroupResponseDTO> update(@PathVariable int id,
                                                       @RequestBody IndicatorGroupRequestDTO dto){
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id){
        return service.delete(id);
    }
}
