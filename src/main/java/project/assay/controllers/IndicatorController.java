package project.assay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.IndicatorRequestDTO;
import project.assay.dto.responces.IndicatorResponceDTO;
import project.assay.services.IndicatorService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/indicators")
public class IndicatorController {
    private final IndicatorService indicatorService;

    @Autowired
    public IndicatorController(IndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }

    @GetMapping
    public ResponseEntity<List<IndicatorResponceDTO>> getAllIndicators() {
        return indicatorService.findAll();
    }

    @PostMapping
    public ResponseEntity<IndicatorResponceDTO> createIndicator(@RequestBody IndicatorRequestDTO dto) {
        return indicatorService.create(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndicatorResponceDTO> updateIndicator(@PathVariable("id") int indicatorId, @RequestBody IndicatorRequestDTO dto) {
        return indicatorService.update(indicatorId, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteIndicator(@PathVariable int id) {
        return indicatorService.delete(id);
    }


    @GetMapping("/units")
    public ResponseEntity<Set<String>> getAllUnits() {
        return indicatorService.findAllUnits();
    }
}
