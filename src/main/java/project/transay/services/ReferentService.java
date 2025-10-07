package project.transay.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transay.models.Indicator;
import project.transay.models.Referent;
import project.transay.models.ReferentStatusEnum;
import project.transay.models.Transcript;
import project.transay.repositories.ReferentRepository;

import java.util.Set;

import static project.transay.models.ReferentStatusEnum.*;

@Service
@Transactional(readOnly = true)
public class ReferentService {
    private final ReferentRepository repo;
    private final TranscriptService transcriptService;

    public ReferentService(ReferentRepository repo,
                           TranscriptService transcriptService) {
        this.repo = repo;
        this.transcriptService = transcriptService;
    }

    @Transactional
    public void enrichAndSave(Referent referent, Indicator indicator) {
        // Проверяем референтное состояние на данных момент
        ReferentStatusEnum verdict = this.checkValue(indicator, referent.getCurrentValue());
        referent.setStatus(verdict);

        Set<Transcript> transcripts = transcriptService.findCorrect(indicator.getEngName(), indicator.getGender());
        referent.setTranscripts(transcripts);
        repo.save(referent);
    }

    @Transactional
    public void save(Referent referent) {
        repo.save(referent);
    }

    private ReferentStatusEnum checkValue(Indicator indicator, double value) {
        double minValue = indicator.getMinValue();
        double maxValue = indicator.getMaxValue();
        if (value < minValue) {
            return FALL;
        } else if (value > maxValue) {
            return RAISE;
        } else {
            return OK;
        }
    }
}
