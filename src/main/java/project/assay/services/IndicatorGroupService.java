package project.assay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.IndicatorGroupDTO;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.IndicatorGroup;
import project.assay.repositories.IndicatorGroupRepository;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Service
@Transactional(readOnly = true)
public class IndicatorGroupService {
    private final IndicatorGroupRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public IndicatorGroupService(IndicatorGroupRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    protected List<IndicatorGroup> find(){
        return repository.findAll();
    }

    public ResponseEntity<List<IndicatorGroup>> findAll() {
        return ok(repository.findAll());
    }

    private IndicatorGroup findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id=" + id +" not found!"));
    }

    @Transactional
    public ResponseEntity<IndicatorGroup> save(IndicatorGroupDTO dto) {
        return ok(repository.save(convertToEntity(dto)));
    }

    @Transactional
    public ResponseEntity<IndicatorGroup> update(int id, IndicatorGroupDTO dto) {
        IndicatorGroup entity = this.findById(id);
        entity.setGroupName(dto.getGroupName());
        entity.setIndicators(dto.getIndicators());
        return ok(repository.save(entity));
    }

    @Transactional
    public ResponseEntity<HttpStatus> delete(int id) {
        IndicatorGroup entity = this.findById(id);
        repository.delete(entity);
        return status(NO_CONTENT).build();
    }

    private IndicatorGroup convertToEntity(IndicatorGroupDTO dto) {
        return mapper.map(dto, IndicatorGroup.class);
    }
}
