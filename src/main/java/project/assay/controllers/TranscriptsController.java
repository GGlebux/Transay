package project.assay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.TranscriptRequestDTO;
import project.assay.models.Transcript;
import project.assay.services.TranscriptService;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/transcripts")
public class TranscriptsController {
    private final TranscriptService transcriptService;

    @Autowired
    public TranscriptsController(TranscriptService transcriptService) {
        this.transcriptService = transcriptService;
    }

    @GetMapping
    public ResponseEntity<List<Transcript>> getAllTranscripts() {
        return ok(transcriptService.findAll());
    }

    @PostMapping
    public ResponseEntity<Transcript> createTranscript(@RequestBody TranscriptRequestDTO dto) {
        return transcriptService.save(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTranscript(@PathVariable int id) {
        return transcriptService.delete(id);
    }

}
