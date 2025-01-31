package project.assay.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.assay.dto.ExcludedReasonDTO;
import project.assay.models.ExcludedReason;
import project.assay.models.Person;
import project.assay.services.PeopleService;
import project.assay.services.ExcludedReasonService;

/**
 * REST Контроллер для работы с сущностью Reason (причины, которые исключил пользователь).
 * Предоставляет API endpoint`ы для клиента
 *
 * @author GGlebux
 */

@RestController
@RequestMapping
public class ReasonsController {

  private final ExcludedReasonService excludedReasonService;
  private final PeopleService peopleService;

  @Autowired
  public ReasonsController(ExcludedReasonService excludedReasonService, PeopleService peopleService) {
    this.excludedReasonService = excludedReasonService;
    this.peopleService = peopleService;
  }

  @GetMapping("reason/all")
  public ResponseEntity<List<String>> getAllReasons() {
    List<String> reasons = excludedReasonService.findAll();
    if (reasons.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return ResponseEntity.ok(reasons);
  }

  @GetMapping("/people/{personId}/reason")
  public ResponseEntity<List<ExcludedReasonDTO>> getPersonReasons(@PathVariable("personId") int personId) {
    List<ExcludedReasonDTO> reasons = excludedReasonService.findByPersonId(personId)
            .stream().map(this::convertToReasonDTO).toList();
    if (reasons.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return ResponseEntity.ok(reasons);
  }

  @PostMapping("/people/{personId}/reason")
  public ResponseEntity<String> createReason(@PathVariable("personId") int personId,
      @RequestBody ExcludedReasonDTO excludedReasonDTO) {
    Person owner = peopleService.findById(personId);
    List<String> reasons = excludedReasonService.findByPersonId(personId)
            .stream().map(ExcludedReason::getReason).toList();
    if (reasons.contains(excludedReasonDTO.getReason())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Reason already exists!");
    }
    ExcludedReason excludedReason = ExcludedReason.builder()
            .reason(excludedReasonDTO.getReason())
            .owner(owner).build();
    excludedReasonService.save(excludedReason);
    return ResponseEntity.ok("Create reason with id=" + excludedReason.getId());
  }

  @DeleteMapping("/people/{personId}/reason/{reasonId}")
  public ResponseEntity<HttpStatus> deleteReason(@PathVariable("reasonId") int reasonId) {
    excludedReasonService.delete(reasonId);
    return ResponseEntity.ok(HttpStatus.ACCEPTED);
  }

  private ExcludedReasonDTO convertToReasonDTO(ExcludedReason excludedReason) {
    return ExcludedReasonDTO.builder()
            .reason(excludedReason.getReason())
            .id(excludedReason.getId()).build();
  }
}
