package project.assay.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.models.Indicator;
import project.assay.models.PersonIndicator;
import project.assay.models.Transcript;
import project.assay.repositories.TranscriptRepository;

@Service
@Transactional(readOnly = true)
public class TranscriptService {

  private final TranscriptRepository transcriptRepository;
  private final MainService mainService;

  @Autowired
  public TranscriptService(TranscriptRepository transcriptRepository, MainService mainService) {
    this.transcriptRepository = transcriptRepository;
    this.mainService = mainService;
  }

  public Transcript getTranscript(int id) {
    return transcriptRepository.findById(id).orElse(null);
  }

  public List<Transcript> getAllTranscripts() {
    return transcriptRepository.findAll();
  }

  public Optional<Transcript> findByName(String name) {
    return transcriptRepository.findByName(name);
  }

  public List<String> getFall(String name) {
    Optional<Transcript> transcript = findByName(name);
    return transcript.map(Transcript::getFall).orElse(null);
  }

  public List<String> getRaise(String name) {
    Optional<Transcript> transcript = findByName(name);
    return transcript.map(Transcript::getRaise).orElse(null);
  }

  public Map<String, List<String>> getDecrypt(List<PersonIndicator> personIndicators) {
    Map<String, List<String>> decryptMap = new HashMap<>();
    for (PersonIndicator pi : personIndicators) {

      Indicator indicator = pi.getIndicator();
      String iName = indicator.getName();

      double current = pi.getCurrentValue();
      double min = indicator.getMinValue();
      double max = indicator.getMaxValue();

      System.out.println("Имя= " + iName + " Min= " + min +" Текущее= "+ current + " Max= " + max);

      Optional<Transcript> transcript = transcriptRepository.findByName(iName);
      if (transcript.isPresent()) {
        if (current <= min) {
          decryptMap.put(iName, findByName(iName).get().getFall());
        } else if (current >= max) {
          decryptMap.put(iName, findByName(iName).get().getRaise());
        }
      }


    }
    return decryptMap;
  }

}
