package project.assay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.ExcludedReasonDTO;
import project.assay.services.PeopleService;
import project.assay.services.ReasonsService;

import java.util.List;
import java.util.Set;

/**
 * REST Контроллер для работы с сущностью Reason (причины, которые исключил пользователь).
 * Предоставляет API endpoint`ы для клиента
 *
 * @author GGlebux
 */

@RestController
@RequestMapping
public class ReasonsController {

    private final ReasonsService reasonsService;

    @Autowired
    public ReasonsController(ReasonsService reasonsService, PeopleService peopleService) {
        this.reasonsService = reasonsService;
    }

    @GetMapping("/reasons")
    public ResponseEntity<Set<String>> getAllReasons() {
        return reasonsService.findAll();
    }

    @GetMapping("/people/{personId}/reason")
    public ResponseEntity<List<ExcludedReasonDTO>> getPersonReasons(@PathVariable("personId") int personId) {
        return reasonsService.findByPersonId(personId);
    }

    @PostMapping("/people/{personId}/reason")
    public ResponseEntity<String> createReason(@PathVariable("personId") int personId,
                                               @RequestBody ExcludedReasonDTO excludedReasonDTO) {
        return reasonsService.save(excludedReasonDTO, personId);
    }

    @DeleteMapping("/people/{personId}/reason/{reasonId}")
    public ResponseEntity<HttpStatus> deleteReason(@PathVariable("reasonId") int reasonId) {
        return reasonsService.delete(reasonId);
    }
}
