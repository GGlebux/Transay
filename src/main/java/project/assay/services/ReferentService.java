package project.assay.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.models.Indicator;
import project.assay.models.Referent;
import project.assay.models.Transcript;
import project.assay.repositories.ReferentRepository;

@Service
@Transactional(readOnly = true)
public class ReferentService {
  private final ReferentRepository referentRepository;
  private final IndicatorService indicatorService;
  private final TranscriptService transcriptService;

  public ReferentService(ReferentRepository referentRepository, IndicatorService indicatorService,
      TranscriptService transcriptService) {
    this.referentRepository = referentRepository;
    this.indicatorService = indicatorService;
    this.transcriptService = transcriptService;
  }

  public void enrich(Referent referent, Indicator indicator) {
    // Проверяем референтное состояние на данных момент
    String verdict = indicatorService.checkValue(indicator, referent.getCurrentValue());
    Transcript transcript = transcriptService.findCorrect(indicator.getEngName(), indicator.getGender());
    switch (verdict) {
      case "fall":
        referent.setStatus("fall"); break;
      case "raise":
        referent.setStatus("raise"); break;
      case "ok":
        referent.setStatus("ok"); break;
    }
    referent.setTranscript(transcript);
  }

  @Transactional
  public void save(Referent referent) {
    referentRepository.save(referent);
  }
}
