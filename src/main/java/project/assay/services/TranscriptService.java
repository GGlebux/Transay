package project.assay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.TranscriptRequestDTO;
import project.assay.models.Transcript;
import project.assay.repositories.TranscriptRepository;

import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Service
@Transactional(readOnly = true)
public class TranscriptService {

  private final TranscriptRepository transcriptRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public TranscriptService(TranscriptRepository transcriptRepository, ModelMapper modelMapper) {
    this.transcriptRepository = transcriptRepository;
      this.modelMapper = modelMapper;
  }

  public Transcript findById(int id) {
    return transcriptRepository.findById(id).orElseThrow();
  }

  public Transcript findByName(String name) {
    return transcriptRepository.findByName(name).orElse(null);
  }

  public List<Transcript> findAll() {
    return transcriptRepository.findAll();
  }

  @Transactional
  public ResponseEntity<Transcript> save(TranscriptRequestDTO dto) {
    return ok(transcriptRepository.save(convertToEntity(dto)));
  }

  @Transactional
  public ResponseEntity<String> delete(int id) {

    transcriptRepository.deleteById(id);
    return status(NO_CONTENT)
            .body(format("Удалена транскрипция с id=%d", id));
  }

  private Transcript convertToEntity(TranscriptRequestDTO dto) {
    return modelMapper.map(dto, Transcript.class);
  }

}
