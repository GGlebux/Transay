package project.assay.controllers;

import java.net.URI;
import java.util.List;
import org.modelmapper.ModelMapper;
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
import project.assay.dto.ReasonDTO;
import project.assay.models.Person;
import project.assay.models.Reason;
import project.assay.services.PeopleService;
import project.assay.services.ReasonService;

/**
 * REST Контроллер для работы с сущностью Reason (причины, которые исключил пользователь).
 * Предоставляет API endpoint`ы для клиента
 *
 * @author GGlebux
 */

@RestController
@RequestMapping("/people/{personId}/reason")
public class ReasonsController {

  private final ReasonService reasonService;
  private final PeopleService peopleService;
  private final ModelMapper modelMapper;

  @Autowired
  public ReasonsController(ReasonService reasonService, PeopleService peopleService,
      ModelMapper modelMapper) {
    this.reasonService = reasonService;
    this.peopleService = peopleService;
    this.modelMapper = modelMapper;
  }

  @GetMapping
  public ResponseEntity<List<Reason>> getAllReasons(@PathVariable("personId") int personId) {
    return ResponseEntity.ok(reasonService.findByPersonId(personId));
  }

  @PostMapping
  public ResponseEntity<Integer> createReason(@PathVariable("personId") int personId,
      @RequestBody ReasonDTO reasonDTO) {
    Person owner = peopleService.findById(personId);
    Reason reason = modelMapper.map(reasonDTO, Reason.class);
    reason.setOwner(owner);
    reasonService.save(reason);
    URI location = URI.create("/people/" + personId + "/reason/" + reason.getId());
    return ResponseEntity.created(location).body(reason.getId());
  }

  @DeleteMapping("/{reasonId}")
  public ResponseEntity<HttpStatus> deleteReason(@PathVariable("reasonId") int reasonId) {
    reasonService.delete(reasonId);
    return ResponseEntity.ok(HttpStatus.ACCEPTED);
  }
}
