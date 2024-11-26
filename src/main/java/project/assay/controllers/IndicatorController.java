package project.assay.controllers;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import project.assay.models.Indicator;
import project.assay.models.IndicatorWithCurrentValue;
import project.assay.models.Person;
import project.assay.services.IndicatorService;
import project.assay.services.MainService;
import project.assay.services.PeopleService;

@Controller
@RequestMapping("/indicators")
public class IndicatorController {
  private final IndicatorService indicatorService;
  private final PeopleService peopleService;
  private final MainService mainService;

  @Autowired
  public IndicatorController(IndicatorService indicatorService, PeopleService peopleService,
      MainService mainService) {
    this.indicatorService = indicatorService;
    this.peopleService = peopleService;
    this.mainService = mainService;
  }

  @GetMapping
  public String index(Model model) {
    Person person = peopleService.findOne();
    model.addAttribute("person", person);

    List<IndicatorWithCurrentValue> fullIndicators = mainService.getIndicatorsById(person.getId());
    model.addAttribute("fullIndicators", fullIndicators);
    return "indicators/index";
  }

  @GetMapping("/{id}")
  public String show(@PathVariable int id, Model model) {
    Person person = peopleService.findOne();
    model.addAttribute("person", person);

    Indicator indicator = indicatorService.findById(id);
    model.addAttribute("indicator", indicator);

    Double value = mainService.getCurrentValue(person.getId(), id);
    model.addAttribute("value", value);
    return "indicators/show";
  }

  @GetMapping("/{id}/edit")
  public String edit(@PathVariable int id, Model model) {
    Person person = peopleService.findById(id);
    model.addAttribute("person", person);

    List<Indicator> indicators = indicatorService.findAllCorrect(person);
    System.out.println("Все индикаторы");
    System.out.println(indicators);
    model.addAttribute("indicators", indicators);

    List<IndicatorWithCurrentValue> personIndicators = mainService.getIndicatorsById(id);
    System.out.println("Имеющиеся");
    System.out.println(personIndicators);
    model.addAttribute("personIndicators", personIndicators);
    return "indicators/edit";
  }




}
