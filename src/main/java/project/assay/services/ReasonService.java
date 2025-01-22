package project.assay.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.assay.dto.ReasonDTO;
import project.assay.models.Reason;
import project.assay.models.Transcript;
import project.assay.repositories.ReasonRepository;

@Service
public class ReasonService {
  private final ReasonRepository reasonRepository;
  private final TranscriptService transcriptService;

  @Autowired
  public ReasonService(ReasonRepository reasonRepository, TranscriptService transcriptService) {
    this.reasonRepository = reasonRepository;
    this.transcriptService = transcriptService;
  }

  public Reason findById(int id) {
    return reasonRepository.findById(id).orElse(null);
  }

  public List<String> findAll() {
    List<Transcript> transcripts = transcriptService.findAll();
    Set<String> stringReasons = new HashSet<>();
    for (Transcript transcript : transcripts) {
      stringReasons.addAll(transcript.getFall());
      stringReasons.addAll(transcript.getRaise());
    }
    return stringReasons.stream().sorted().toList();
  }

  public List<Reason> findByPersonId(int personId) {
    return reasonRepository.findByOwnerId(personId);
  }

  public Reason save(Reason reason) {
    return reasonRepository.save(reason);
  }

  public void delete(int id) {
    reasonRepository.deleteById(id);
  }

  private ReasonDTO convertToReasonDTO(String reason) {
    return ReasonDTO.builder().reason(reason).build();
  }
}
