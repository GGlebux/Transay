package project.assay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.TranscriptRequestDTO;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.Reason;
import project.assay.models.Transcript;
import project.assay.repositories.TranscriptRepository;

import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Service
@Transactional(readOnly = true)
public class TranscriptService {

    private final TranscriptRepository transcriptRepository;
    private final ModelMapper modelMapper;
    private final ReasonsService reasonsService;

    @Autowired
    public TranscriptService(TranscriptRepository transcriptRepository, ModelMapper modelMapper, ReasonsService reasonsService) {
        modelMapper
                .createTypeMap(TranscriptRequestDTO.class, Transcript.class)
                .addMappings(mapper -> mapper.skip(Transcript::setId));
        modelMapper
                .getConfiguration()
                .setSkipNullEnabled(true);

        this.modelMapper = modelMapper;
        this.transcriptRepository = transcriptRepository;
        this.reasonsService = reasonsService;
    }

    public Transcript findById(int id) {
        return transcriptRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Transcript with id=%d not found", id)));
    }

    public Transcript findCorrect(String name, String gender) {
        return transcriptRepository.findByNameAndGender(name, gender)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Transcript with name='%s' and gender='%s' not found", name, gender)));
    }

    public ResponseEntity<List<Transcript>> findAll() {
        return ok(transcriptRepository.findAll());
    }

    @Transactional
    public ResponseEntity<Transcript> save(TranscriptRequestDTO dto) {
        return ok(transcriptRepository.save(convertToEntity(dto)));
    }

    @Transactional
    public ResponseEntity<Transcript> update(TranscriptRequestDTO dto, int id) {
        Transcript updated = transcriptRepository.save(convertToEntity(dto, findById(id)));
        return ok(updated);
    }

    @Transactional
    public ResponseEntity<String> delete(int id) {
        transcriptRepository.deleteById(id);
        return status(NO_CONTENT)
                .body(format("Удалена транскрипция с id=%d", id));
    }

    private Transcript convertToEntity(TranscriptRequestDTO dto) {
        Transcript entity = modelMapper.map(dto, Transcript.class);
        enrichEntity(dto, entity);
        System.out.println(entity);
        return entity;
    }

    private Transcript convertToEntity(TranscriptRequestDTO toUpdate, Transcript entity) {
        modelMapper.map(toUpdate, entity);
        enrichEntity(toUpdate, entity);
        return entity;
    }

    private void enrichEntity(TranscriptRequestDTO dto, Transcript entity) {
        List<Reason> falls = reasonsService.findAll(dto.getFallsIds());
        List<Reason> raises = reasonsService.findAll(dto.getRaisesIds());
        entity.setFalls(falls);
        entity.setRaises(raises);
    }
}
