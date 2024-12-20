//package project.assay.controllers;
//
//
//import jakarta.validation.Valid;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import project.assay.dto.IndicatorDTO;
//import project.assay.exceptions.IndicatorNotFoundException;
//import project.assay.models.Indicator;
//import project.assay.models.Person;
//import project.assay.models.PersonInfo;
//import project.assay.services.IndicatorService;
//import project.assay.services.MainService;
//import project.assay.services.PeopleService;
//import project.assay.services.TranscriptService;
//import project.assay.utils.responces.IndicatorErrorResponce;
//
//import static project.assay.utils.converters.DayConverter.*;
//
//@RestController
//@RequestMapping("profile/{personId}/indicators")
//public class IndicatorController {
//
//  private final IndicatorService indicatorService;
//  private final PeopleService peopleService;
//  private final TranscriptService transcriptService;
//  private final MainService mainService;
//  private final ModelMapper modelMapper;
//
//  @Autowired
//  public IndicatorController(IndicatorService indicatorService, PeopleService peopleService,
//      TranscriptService transcriptService,
//      MainService mainService, ModelMapper modelMapper) {
//    this.indicatorService = indicatorService;
//    this.peopleService = peopleService;
//    this.transcriptService = transcriptService;
//    this.mainService = mainService;
//    this.modelMapper = modelMapper;
//  }
//
//  @GetMapping
//  public List<IndicatorDTO> list(@PathVariable("personId") int personId) {
//    Person person = peopleService.findById(personId);
//    List<PersonInfo> personIndicators = mainService.findPI(person.getId());
//    return personIndicators.stream().map(this::convertToIndicatorDTO).toList();
//  }
//
//  @GetMapping("/{id}")
//  public IndicatorDTO show(@PathVariable int id) {
//    return convertToIndicatorDTO(mainService.findById(id));
//  }
//
//  @PostMapping
//  public ResponseEntity<HttpStatus> create(@RequestBody @Valid IndicatorDTO indicatorDTO,
//      @PathVariable("personId") int personId) {
//    mainService.save(convertToPersonIndicator(indicatorDTO, personId));
//    return ResponseEntity.ok(HttpStatus.CREATED);
//  }
//
//  @DeleteMapping("/{id}")
//  public ResponseEntity<HttpStatus> delete(@PathVariable int id) {
//    mainService.delete(id);
//    return ResponseEntity.ok(HttpStatus.OK);
//  }
//
//  @GetMapping("/correct")
//  public List<String> correct(@PathVariable("personId") int personId) {
//    Person person = peopleService.findById(personId);
//    List<Indicator> indicators = indicatorService.findAllCorrect(person);
//    return indicators.stream().map(Indicator::getName).toList();
//  }
//
//  @GetMapping("/decrypt")
//  public Map<String, List<String>> decrypt(@PathVariable("personId") int personId) {
//    Person person = peopleService.findById(personId);
//    List<PersonInfo> personIndicators = mainService.findPI(person.getId());
//    return transcriptService.getDecrypt(personIndicators);
//  }
//
//  private IndicatorDTO convertToIndicatorDTO(PersonInfo personIndicator) {
//    Indicator indicator = personIndicator.getIndicator();
//    Map<String, Integer> maxPeriod = convertToPeriod(indicator.getMaxAge());
//    Map<String, Integer> minPeriod = convertToPeriod(indicator.getMinAge());
//
//    IndicatorDTO result = modelMapper.map(indicator, IndicatorDTO.class);
//    result.setCurrentValue(personIndicator.getCurrentValue());
//    result.setMaxAge(maxPeriod);
//    result.setMinAge(minPeriod);
//    result.setId(personIndicator.getId());
//    return result;
//  }
//
//  private PersonInfo convertToPersonIndicator(IndicatorDTO indicatorDTO, int personId) {
//    Person person = peopleService.findById(personId);
//    Indicator indicator = indicatorService.findOneCorrect(indicatorDTO.getName(), personId);
//    PersonInfo personIndicator = new PersonInfo();
//    personIndicator.setPerson(person);
//    personIndicator.setIndicator(indicator);
//    personIndicator.setCurrentValue(indicatorDTO.getCurrentValue());
//    return personIndicator;
//  }
//
//  @ExceptionHandler
//  public ResponseEntity<IndicatorErrorResponce> handleException(IndicatorNotFoundException e) {
//    IndicatorErrorResponce responce = new IndicatorErrorResponce(
//        "Indicator with this id not found!",
//        LocalDateTime.now()
//    );
//    return new ResponseEntity<>(responce, HttpStatus.NOT_FOUND);
//  }
//}