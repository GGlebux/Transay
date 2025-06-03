package project.assay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.ExcludedReasonDTO;
import project.assay.models.ExcludedReason;
import project.assay.models.Person;
import project.assay.repositories.ReasonRepository;

import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static project.assay.utils.StaticMethods.parseExcelColumn;

@Service
@Transactional(readOnly = true)
public class ReasonsService {
    private final ReasonRepository reasonRepository;
    private final PeopleService peopleService;
    private static final String REASONS_PATH = "src/main/resources/static/reasons.xlsx";

    @Autowired
    public ReasonsService(ReasonRepository reasonRepository, PeopleService peopleService, TranscriptService transcriptService) {
        this.reasonRepository = reasonRepository;
        this.peopleService = peopleService;
    }

    public ResponseEntity<Set<String>> findAll() {
        return ok(parseExcelColumn(REASONS_PATH, 0));
    }

    protected List<ExcludedReason> findAll(int person) {
        return reasonRepository.findByOwnerId(person);
    }

    public ResponseEntity<List<ExcludedReasonDTO>> findByPersonId(int personId) {
        List<ExcludedReasonDTO> reasons = reasonRepository
                .findByOwnerId(personId)
                .stream()
                .map(this::convertToReasonDTO)
                .toList();
        if (reasons.isEmpty()) {
            return status(NO_CONTENT).body(reasons);
        }
        return ok(reasons);
    }

    @Transactional
    public ResponseEntity<String> save(ExcludedReasonDTO excludedReasonDTO, int personId) {
        Person owner = peopleService.find(personId);
        List<String> reasons = this
                .findAll(personId)
                .stream()
                .map(ExcludedReason::getReason)
                .toList();
        if (reasons.contains(excludedReasonDTO.getReason())) {
            return status(CONFLICT).body("Reason already exists!");
        }
        ExcludedReason excludedReason = ExcludedReason
                .builder()
                .reason(excludedReasonDTO.getReason())
                .owner(owner)
                .build();
        reasonRepository.save(excludedReason);
        return ok("Create reason with id=" + excludedReason.getId());
    }

    @Transactional
    public ResponseEntity<HttpStatus> delete(int id) {
        reasonRepository.deleteById(id);
        return status(NO_CONTENT).build();
    }

    private ExcludedReasonDTO convertToReasonDTO(ExcludedReason excludedReason) {
        return ExcludedReasonDTO
                .builder()
                .reason(excludedReason.getReason())
                .id(excludedReason.getId())
                .build();
    }
}
