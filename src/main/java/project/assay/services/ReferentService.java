package project.assay.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.models.Indicator;
import project.assay.models.Referent;
import project.assay.models.Transcript;
import project.assay.repositories.ReferentRepository;

import java.time.LocalDate;

import static java.util.List.of;
import static java.util.Objects.isNull;

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

  @Transactional
  public void enrich(Referent referent, Indicator indicator) {
    // Если клиент не указал дату анализа, то ставим по умолчанию текущую
    if (isNull(referent.getRegDate())) {
      referent.setRegDate(LocalDate.now());
    }
    // ToDo: добавить единицы измерения

    // Проверяем референтое состояние на данных момент
    String verdict = indicatorService.checkValue(indicator, referent.getCurrentValue());
    Transcript transcript = transcriptService.findByName(indicator.getEng_name());
    switch (verdict) {
      case "lower":
        referent.setStatus("lower");
        referent.setReasons(transcript.getFall()); break;
      case "upper":
        referent.setStatus("upper");
        referent.setReasons(transcript.getRaise()); break;
      case "ok":
        referent.setStatus("ok");
        referent.setReasons(of()); break;
    }
  }

  @Transactional
  public void save(Referent referent) {
    referentRepository.save(referent);
  }
}
