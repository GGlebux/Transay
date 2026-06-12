package project.assay.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.models.Indicator;
import project.assay.models.Referent;
import project.assay.models.Transcript;
import project.assay.models.enums.ReferentStatus;
import project.assay.repositories.ReferentRepository;

import java.util.Set;

import static project.assay.models.enums.IndicatorGender.convert;

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
    ReferentStatus verdict = indicatorService
            .checkValue(indicator,
                    referent.getCurrentValue());

    Set<Transcript> transcripts = transcriptService
            .findCorrect(indicator.getEngName(),
                    convert((indicator.getGender())));

    referent.setStatus(verdict);
    referent.setTranscripts(transcripts);
  }

  @Transactional
  public void save(Referent referent) {
    referentRepository.save(referent);
  }
}
