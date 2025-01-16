package project.assay.controllers;


import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.assay.dto.IndicatorDTO;
import project.assay.dto.ReferentDTO;
import project.assay.exceptions.IndicatorNotFoundException;
import project.assay.models.Indicator;
import project.assay.models.Person;
import project.assay.models.PersonInfo;
import project.assay.models.Referent;
import project.assay.responces.IndicatorErrorResponce;
import project.assay.services.IndicatorService;
import project.assay.services.PeopleService;
import project.assay.services.PersonInfoService;
import project.assay.services.ReferentService;

/**
 *
 */
@RestController
@RequestMapping("/indicator")
public class IndicatorController {

  private final IndicatorService indicatorService;
  private final PeopleService peopleService;
  private final ReferentService referentService;
  private final ModelMapper modelMapper;
  private final PersonInfoService personInfoService;

  @Autowired
  public IndicatorController(IndicatorService indicatorService, PeopleService peopleService,
      ReferentService referentService,
      ModelMapper modelMapper, PersonInfoService personInfoService) {
    this.indicatorService = indicatorService;
    this.peopleService = peopleService;
    this.referentService = referentService;
    this.modelMapper = modelMapper;
    this.personInfoService = personInfoService;
  }

  /**
   * Отображение списка корректных индикаторов для конкретного человека
   */
  @GetMapping("/{personId}/correct")
  public List<IndicatorDTO> showCorrectList(@PathVariable("personId") int personId) {
    Person person = peopleService.findById(personId);
    List<Indicator> indicators = indicatorService.findAllCorrect(person);
    return indicators.stream().map(this::convertToIndicatorDTO).toList();
  }

  @GetMapping("/{personId}")
  public List<ReferentDTO> showPersonInfo(@PathVariable("personId") int personId) {

    List<PersonInfo> referents = personInfoService.findAllById(personId);
    return referents.stream().map(this::convertToReferentDTO).toList();
  }

  @PostMapping("/{personId}")
  public ResponseEntity<HttpStatus> create(@RequestBody @Valid ReferentDTO referentDTO,
      @PathVariable("personId") int personId,
      @RequestParam("indicatorId") int indicatorId,
      BindingResult bindingResult) {

    Person person = peopleService.findById(personId);
    Indicator indicator = indicatorService.findById(indicatorId);
    Referent referent = convertToReferent(referentDTO);

    referentService.save(referent, indicator);

    PersonInfo personInfo = new PersonInfo();
    personInfo.setPerson(person);
    personInfo.setIndicator(indicator);
    personInfo.setReferent(referent);
    personInfoService.save(personInfo);

    return ResponseEntity.ok(HttpStatus.CREATED);
  }

  // ToDo: Сделать рабочий метод
  @DeleteMapping("/{personId}")
  public ResponseEntity<HttpStatus> delete(@PathVariable("personId") int personId,
      @RequestParam("indicatorId") int indicatorId) {
    personInfoService.deleteByIds(personId, indicatorId);
    return ResponseEntity.ok(HttpStatus.OK);
  }

  private IndicatorDTO convertToIndicatorDTO(Indicator indicator) {
    return modelMapper.map(indicator, IndicatorDTO.class);
  }

  private Referent convertToReferent(ReferentDTO referentDTO) {
    return modelMapper.map(referentDTO, Referent.class);
  }

  private ReferentDTO convertToReferentDTO(PersonInfo personInfo) {
    ReferentDTO result = new ReferentDTO();
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    modelMapper.map(personInfo.getReferent(), result);
    modelMapper.map(personInfo.getIndicator(), result);
    return result;
  }

  @ExceptionHandler
  public ResponseEntity<IndicatorErrorResponce> handleException(IndicatorNotFoundException e) {
    IndicatorErrorResponce responce = new IndicatorErrorResponce(
        "Indicator with this id not found!",
        LocalDateTime.now()
    );
    return new ResponseEntity<>(responce, HttpStatus.NOT_FOUND);
  }
}