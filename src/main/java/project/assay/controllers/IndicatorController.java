package project.assay.controllers;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.assay.models.Indicator;
import project.assay.models.Person;
import project.assay.models.PersonIndicator;
import project.assay.services.IndicatorService;
import project.assay.services.MainService;
import project.assay.services.PeopleService;
import project.assay.services.TranscriptService;

@Controller
@RequestMapping("/indicators")
public class IndicatorController {
  private final IndicatorService indicatorService;
  private final PeopleService peopleService;
  private final TranscriptService transcriptService;
  private final MainService mainService;

  @Autowired
  public IndicatorController(IndicatorService indicatorService, PeopleService peopleService,
      TranscriptService transcriptService,
      MainService mainService) {
    this.indicatorService = indicatorService;
    this.peopleService = peopleService;
    this.transcriptService = transcriptService;
    this.mainService = mainService;
  }

  @GetMapping
  public String index(Model model) {
    Optional<Person> person = peopleService.findOne();
    if (person.isPresent()) {
      List<PersonIndicator> personIndicators = mainService.findPI(person.get().getId());
      model.addAttribute("pi", personIndicators);
      return "indicators/index";
    }
    else {
      return "redirect:/people";
    }
  }

  @GetMapping("/{id}")
  public String show(@PathVariable int id, Model model) {
    PersonIndicator pi = mainService.findById(id);
    model.addAttribute("pi", pi);
    return "indicators/show";
  }

  @GetMapping("/{id}/edit")
  public String edit(@PathVariable int id, Model model) {
    PersonIndicator pi = mainService.findById(id);
    model.addAttribute("pi", pi);
    return "indicators/edit";
  }

  @GetMapping("/new")
  public String newIndicator(@ModelAttribute("pi") PersonIndicator indicator, Model model) {
    Optional<Person> person = peopleService.findOne();
    if (person.isPresent()) {
      List<Indicator> all = indicatorService.findAllCorrect(person.get());
      model.addAttribute("all", all);
      System.out.println("КОРРЕКТНЫЕЕ==" + all);
    }
    return "indicators/new";
  }

  @PostMapping("{id}")
  public String create(@PathVariable int id, PersonIndicator pi, Model model) {
    Optional<Person> person = peopleService.findOne();
    if (person.isPresent()) {
      pi.setPerson(person.get());
      mainService.save(pi);
    }
    return "redirect:/indicators";
  }

  @PatchMapping("{id}")
  public String update(@ModelAttribute("pi") PersonIndicator pi, @PathVariable int id, Model model) {
    mainService.updateValue(id, pi);
    return "redirect:/indicators";
  }

  @DeleteMapping("{id}")
  public String delete(@PathVariable int id) {
    mainService.delete(id);
    return "redirect:/indicators";
  }

  @GetMapping("/decrypt")
  public String decrypt(Model model) {
    Optional<Person> person = peopleService.findOne();
    if (person.isPresent()) {
      List<PersonIndicator> personIndicators = mainService.findPI(person.get().getId());
      Map<String, List<String>> map = transcriptService.getDecrypt(personIndicators);
      if (!map.isEmpty()) {
        model.addAttribute("map", map);
      }

    }
    return "indicators/decrypt";
  }
}
