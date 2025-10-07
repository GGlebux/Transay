package project.transay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.transay.dto.requests.TranscriptRequestDTO;
import project.transay.models.Transcript;
import project.transay.services.TranscriptService;

import java.util.List;

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
        return transcriptService.findAll();
    }

    @PostMapping
    public ResponseEntity<Transcript> createTranscript(@RequestBody TranscriptRequestDTO dto) {
        return transcriptService.save(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transcript> updateTranscript(@PathVariable int id,
                                                       @RequestBody TranscriptRequestDTO dto) {
        return transcriptService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTranscript(@PathVariable int id) {
        return transcriptService.delete(id);
    }
}
