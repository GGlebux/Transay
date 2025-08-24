package project.assay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.SimpleIndicatorDTO;
import project.assay.dto.requests.IndicatorGroupRequestDTO;
import project.assay.dto.responses.IndicatorGroupResponseDTO;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.IndicatorGroup;
import project.assay.repositories.IndicatorGroupRepository;

import java.util.List;

import static java.lang.String.join;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Service
@Transactional(readOnly = true)
public class IndicatorGroupService {
    private final IndicatorGroupRepository repository;


    @Autowired
    public IndicatorGroupService(IndicatorGroupRepository repository) {
        this.repository = repository;
    }

    protected List<IndicatorGroup> find(){
        return repository.findAll();
    }

    public ResponseEntity<List<IndicatorGroupResponseDTO>> findAllWithResponse() {
        return ok(this.findAll());
    }

    public List<IndicatorGroupResponseDTO> findAll() {
        return repository
                .findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private IndicatorGroup findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id=" + id +" not found!"));
    }

    @Transactional
    public ResponseEntity<IndicatorGroupResponseDTO> save(IndicatorGroupRequestDTO dto) {
        IndicatorGroup entity = repository.save(convertToEntity(dto));
        return ok(convertToResponse(entity));
    }

    @Transactional
    public ResponseEntity<IndicatorGroupResponseDTO> update(int id, IndicatorGroupRequestDTO dto) {
        this.findById(id);
        IndicatorGroup newEntity = this.convertToEntity(dto);
        newEntity.setId(id);
        return ok(convertToResponse( repository.save(newEntity)));
    }

    @Transactional
    public ResponseEntity<HttpStatus> delete(int id) {
        IndicatorGroup entity = this.findById(id);
        repository.delete(entity);
        return status(NO_CONTENT).build();
    }

    private IndicatorGroup convertToEntity(IndicatorGroupRequestDTO dto) {
        IndicatorGroup entity = new IndicatorGroup();
        entity.setGroupName(dto.getGroupName());
        List<String> indicatorsWithUnit = dto
                .getIndicators()
                .stream()
                .map(sid -> join("|", sid.getName(), sid.getUnits()))
                .toList();
        entity.setIndicatorsWithUnit(indicatorsWithUnit);
        return entity;
    }



    private IndicatorGroupResponseDTO convertToResponse(IndicatorGroup entity) {
        IndicatorGroupResponseDTO dto = new IndicatorGroupResponseDTO();
        dto.setId(entity.getId());
        dto.setGroupName(entity.getGroupName());
        List<SimpleIndicatorDTO> indicatorDTOS = entity
                .getIndicatorsWithUnit()
                .stream()
                .map(str -> {
                    String[] split = str.split("\\|");
                    return new SimpleIndicatorDTO(split[0], split[1]);
                })
                .toList();
        dto.setIndicators(indicatorDTOS);
        return dto;
    }
}
