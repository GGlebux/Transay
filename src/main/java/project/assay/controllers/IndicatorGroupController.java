package project.assay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.IndicatorGroupDTO;
import project.assay.models.IndicatorGroup;
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
    public ResponseEntity<List<IndicatorGroup>> findAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<IndicatorGroup> create(@RequestBody IndicatorGroupDTO dto) {
        return service.save(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<IndicatorGroup> update(@PathVariable int id,
                                                       @RequestBody IndicatorGroupDTO dto){
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id){
        return service.delete(id);
    }
}
