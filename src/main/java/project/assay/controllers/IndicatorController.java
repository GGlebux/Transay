package project.assay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.IndicatorRequestDTO;
import project.assay.models.Indicator;
import project.assay.services.IndicatorService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping
public class IndicatorController {
    private final IndicatorService indicatorService;

    @Autowired
    public IndicatorController(IndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }

    @GetMapping("/indicators")
    public ResponseEntity<List<Indicator>> getAllIndicators() {
        return indicatorService.findAll();
    }

    @PostMapping("/indicators")
    public ResponseEntity<Indicator> createIndicator(@RequestBody IndicatorRequestDTO dto) {
        return indicatorService.create(dto);
    }

    @GetMapping("/units")
    public ResponseEntity<Set<String>> getAllUnits() {
        return indicatorService.findAllUnits();
    }
}
