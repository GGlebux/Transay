package project.assay.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.assay.models.ExcludedReason;
import project.assay.models.Transcript;
import project.assay.repositories.ExcludedReasonRepository;

@Service
public class ExcludedReasonService {
    private final ExcludedReasonRepository excludedReasonRepository;
    private final TranscriptService transcriptService;

    @Autowired
    public ExcludedReasonService(ExcludedReasonRepository excludedReasonRepository, TranscriptService transcriptService) {
        this.excludedReasonRepository = excludedReasonRepository;
        this.transcriptService = transcriptService;
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

    public List<ExcludedReason> findByPersonId(int personId) {
        return excludedReasonRepository.findByOwnerId(personId);
    }

    public ExcludedReason save(ExcludedReason excludedReason) {
        return excludedReasonRepository.save(excludedReason);
    }

    public void delete(int id) {
        excludedReasonRepository.deleteById(id);
    }
}
