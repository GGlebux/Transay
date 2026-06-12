package project.assay.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.TranscriptRequestDTO;
import project.assay.models.Transcript;
import project.assay.services.TranscriptService;

import java.net.URI;
import java.util.List;

import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.*;
import static project.assay.models.Roles.EDITOR;
import static project.assay.models.Roles.USER;

@RestController
@RequestMapping("/api/transcripts")
@PreAuthorize(EDITOR)
@Tag(name = "Транскрипции", description = "Связь причин (повышения/понижения) с индикатором")
public class TranscriptsController {
    private final TranscriptService transcriptService;

    @Autowired
    public TranscriptsController(TranscriptService transcriptService) {
        this.transcriptService = transcriptService;
    }

    @GetMapping
    @PreAuthorize(USER)
    @Operation(summary = "Получить все транскрипции")
    public ResponseEntity<List<Transcript>> getAllTranscripts() {
        return ok(transcriptService.findAll());
    }

    @PostMapping
    @Operation(summary = "Создать транскрипцию")
    public ResponseEntity<Transcript> createTranscript(@RequestBody TranscriptRequestDTO dto) {
        var response = transcriptService.save(dto);
        var uri = create("/api/transcripts/" + response.getId());
        return created(uri).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить транскрипцию")
    public ResponseEntity<Transcript> updateTranscript(@PathVariable int id,
                                                       @RequestBody TranscriptRequestDTO dto) {
        return ok(transcriptService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить транскрипцию")
    public ResponseEntity<String> deleteTranscript(@PathVariable int id) {
        transcriptService.delete(id);
        return noContent().build();
    }
}
