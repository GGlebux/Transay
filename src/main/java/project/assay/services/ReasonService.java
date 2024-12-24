package project.assay.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.assay.models.Reason;
import project.assay.repositories.ReasonRepository;

@Service
public class ReasonService {
  private final ReasonRepository reasonRepository;

  @Autowired
  public ReasonService(ReasonRepository reasonRepository) {
    this.reasonRepository = reasonRepository;
  }

  public Reason findById(int id) {
    return reasonRepository.findById(id).orElse(null);
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
}
