package project.assay.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.responses.HttpLogDetailDTO;
import project.assay.dto.responses.HttpLogSummaryDTO;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.repositories.HttpLogRepository;

@Service
@Transactional(readOnly = true)
public class HttpLogService {

    private final HttpLogRepository repository;

    public HttpLogService(HttpLogRepository repository) {
        this.repository = repository;
    }

    public Page<HttpLogSummaryDTO> find(boolean onlyErrors, Pageable pageable) {
        return (onlyErrors
                ? repository.findByErrorTrueOrderByCreatedAtDesc(pageable)
                : repository.findAllByOrderByCreatedAtDesc(pageable))
                .map(HttpLogSummaryDTO::from);
    }

    public HttpLogDetailDTO getById(Long id) {
        return repository.findById(id)
                .map(HttpLogDetailDTO::from)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Лог с id '%s' не найден".formatted(id)));
    }

    @Transactional
    public void clear() {
        repository.deleteAllInBatch();
    }
}
