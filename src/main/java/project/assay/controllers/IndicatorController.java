package project.assay.controllers;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import project.assay.models.Indicator;
import project.assay.models.IndicatorWithCurrentValue;
import project.assay.models.Person;
import project.assay.services.IndicatorService;
import project.assay.services.PeopleService;

@Controller
@RequestMapping("/indicator")
public class IndicatorController {
  private final IndicatorService indicatorService;
  private final PeopleService peopleService;

  @Autowired
  public IndicatorController(IndicatorService indicatorService, PeopleService peopleService) {
    this.indicatorService = indicatorService;
    this.peopleService = peopleService;
  }

  @GetMapping
  public String index(Model model) {
    Person person = peopleService.findOne();
    model.addAttribute("person", person);

    List<IndicatorWithCurrentValue> fullIndicators = peopleService.getIndicatorsById(person.getId());
    model.addAttribute("fullIndicators", fullIndicators);
    return "indicators/index";
  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Integer id, Model model) {
    Person person = peopleService.findById(id);

  }
}
