package project.assay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.Reason;
import project.assay.repositories.ReasonRepository;

import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Service
@Transactional(readOnly = true)
public class ReasonsService {
    private final ReasonRepository reasonRepository;

    @Autowired
    public ReasonsService(ReasonRepository reasonRepository) {
        this.reasonRepository = reasonRepository;
    }

    public Reason findById(int id) {
        return reasonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Reason with id=%d not found", id)));
    }

    public List<Reason> findAll(List<Integer> ids){
        return reasonRepository.findAllById(ids);
    }

    @Transactional
    public ResponseEntity<List<Reason>> findAll() {
        return ok(reasonRepository.findAll());
    }

    public ResponseEntity<Reason> find(int id){
        return ok(this.findById(id));
    }

    @Transactional
    public ResponseEntity<Reason> create(String name) {
        return status(CREATED)
                .body(reasonRepository.save(new Reason(name)));
    }

    @Transactional
    public ResponseEntity<Reason> update(String name, int id) {
        Reason reason = this.findById(id);
        reason.setName(name);
        return ok(reasonRepository.save(reason));
    }

    @Transactional
    public ResponseEntity<HttpStatus> delete(int id) {
        Reason reason = this.findById(id);
        reasonRepository.delete(reason);
        return status(NO_CONTENT).build();
    }
}
