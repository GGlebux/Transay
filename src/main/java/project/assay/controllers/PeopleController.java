package project.assay.controllers;


import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.assay.models.Person;
import project.assay.services.PeopleService;

@Controller
@RequestMapping("/people")
public class PeopleController {
  private final PeopleService peopleService;

  @Autowired
  public PeopleController(PeopleService peopleService) {
    this.peopleService = peopleService;
  }

  @GetMapping()
  public String index(Model model) {
    model.addAttribute("person", peopleService.findOne());
    return "people/index";
  }


  @GetMapping("/new")
  public String newPerson(@ModelAttribute("person") Person person) {
    return "people/new";
  }

  @PostMapping()
  public String create(@ModelAttribute("person") @Valid Person person,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors())
      return "people/new";

    peopleService.save(person);
    return "redirect:/people";
  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable("id") int id, Model model) {
    model.addAttribute("person", peopleService.findById(id));
    return "people/edit";
  }

  @PatchMapping("{id}")
  public String update(@ModelAttribute("person") @Valid Person person, @PathVariable("id") int id,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors())
      return "people/edit";

    peopleService.update(id, person);
    return "redirect:/people";
  }

  @DeleteMapping("{id}")
  public String delete(@PathVariable("id") int id) {
    peopleService.delete(id);
    return "redirect:/people";
  }


}
