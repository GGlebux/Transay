package project.transay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transay.dto.requests.TranscriptRequestDTO;
import project.transay.exceptions.EntityNotFoundException;
import project.transay.models.GenderEnum;
import project.transay.models.Reason;
import project.transay.models.Transcript;
import project.transay.repositories.TranscriptRepository;

import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static project.transay.utils.StaticMethods.genderToWord;

@Service
@Transactional(readOnly = true)
public class TranscriptService {

    private final TranscriptRepository repo;
    private final ModelMapper modelMapper;
    private final ReasonsService reasonsService;

    @Autowired
    public TranscriptService(TranscriptRepository repo, ModelMapper modelMapper, ReasonsService reasonsService) {
        modelMapper
                .createTypeMap(TranscriptRequestDTO.class, Transcript.class)
                .addMappings(mapper -> mapper.skip(Transcript::setId));
        modelMapper
                .getConfiguration()
                .setSkipNullEnabled(true);

        this.modelMapper = modelMapper;
        this.repo = repo;
        this.reasonsService = reasonsService;
    }

    public Transcript findById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Транскрипция c id='%d' не найдена", id)));
    }

    public Set<Transcript> findCorrect(String name, GenderEnum gender) {
        Set<Transcript> correctTranscripts = repo.findByNameAndGender(name, gender);
        if (correctTranscripts.isEmpty()) {
            throw new EntityNotFoundException(
                    format("Транскрипция с именем='%s' и полом='%s' не найдена",
                            name,
                            genderToWord(gender)));
        }
        return correctTranscripts;


    }

    public Set<Transcript> findAllByNameIn(Set<String> names, GenderEnum gender) {
        return repo.findAllByNameIn(names, gender);
    }


    public ResponseEntity<List<Transcript>> findAll() {
        return ok(repo.findAll());
    }

    @Transactional
    public ResponseEntity<Transcript> save(TranscriptRequestDTO dto) {
        return ok(repo.save(convertToEntity(dto)));
    }

    @Transactional
    public ResponseEntity<Transcript> update(TranscriptRequestDTO dto, int id) {
        Transcript updated = repo.save(convertToEntity(dto, findById(id)));
        return ok(updated);
    }

    @Transactional
    public ResponseEntity<String> delete(int id) {
        repo.deleteById(id);
        return status(NO_CONTENT)
                .body(format("Удалена транскрипция с id='%d'", id));
    }

    private Transcript convertToEntity(TranscriptRequestDTO dto) {
        Transcript entity = modelMapper.map(dto, Transcript.class);
        enrichEntity(dto, entity);
        return entity;
    }

    private Transcript convertToEntity(TranscriptRequestDTO toUpdate, Transcript entity) {
        modelMapper.map(toUpdate, entity);
        enrichEntity(toUpdate, entity);
        return entity;
    }

    private void enrichEntity(TranscriptRequestDTO dto, Transcript entity) {
        Set<Reason> falls = reasonsService.findByIdIn(dto.getFallsIds());
        Set<Reason> raises = reasonsService.findByIdIn(dto.getRaisesIds());
        entity.setFalls(falls);
        entity.setRaises(raises);
    }
}
