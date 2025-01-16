package project.assay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.models.Transcript;
import project.assay.repositories.TranscriptRepository;

@Service
@Transactional(readOnly = true)
public class TranscriptService {

  private final TranscriptRepository transcriptRepository;

  @Autowired
  public TranscriptService(TranscriptRepository transcriptRepository) {
    this.transcriptRepository = transcriptRepository;
  }

  public Transcript findById(int id) {
    return transcriptRepository.findById(id).orElse(null);
  }

  public Transcript findByName(String name) {
    return transcriptRepository.findByName(name).orElse(null);
  }


}
