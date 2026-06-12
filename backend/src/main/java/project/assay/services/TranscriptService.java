package project.assay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.TranscriptRequestDTO;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.Reason;
import project.assay.models.Transcript;
import project.assay.models.enums.IndicatorGender;
import project.assay.repositories.TranscriptRepository;

import java.util.List;
import java.util.Set;

import static java.lang.String.format;

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

    public Set<Transcript> findCorrect(String name, IndicatorGender gender) {
        Set<Transcript> correctTranscripts = repo.findByNameAndGender(name, gender);
        if (correctTranscripts.isEmpty()) {
            throw new EntityNotFoundException(
                    format("Транскрипция с именем='%s' и полом='%s' не найдена",
                            name,
                            gender.getValue()));
        }
        return correctTranscripts;
    }

    public Set<Transcript> findAllByNameIn(Set<String> names, IndicatorGender gender) {
        return repo.findAllByNameIn(names, gender);
    }


    public List<Transcript> findAll() {
        return repo.findAll();
    }

    @Transactional
    public Transcript save(TranscriptRequestDTO dto) {
        return repo.save(convertToEntity(dto));
    }

    @Transactional
    public Transcript update(TranscriptRequestDTO dto, int id) {
        Transcript updated = repo.save(convertToEntity(dto, findById(id)));
        return updated;
    }

    @Transactional
    public void delete(int id) {
        repo.deleteById(id);
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
