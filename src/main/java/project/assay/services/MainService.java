package project.assay.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.assay.models.Indicator;
import project.assay.repositories.PersonIndicatorRepository;

@Service
public class MainService {
  private final PersonIndicatorRepository bindingRepository;
  private final PeopleService peopleService;
  private final IndicatorService indicatorService;

  @Autowired
  public MainService(PersonIndicatorRepository bindingRepository, PeopleService peopleService,
      IndicatorService indicatorService) {
    this.bindingRepository = bindingRepository;
    this.peopleService = peopleService;
    this.indicatorService = indicatorService;
  }

//  public List<Indicator> getIndicatorsByOwner(int ownerId) {
//    return
//  }
}
