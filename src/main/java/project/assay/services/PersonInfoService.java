package project.assay.services;

import java.util.List;
import org.springframework.stereotype.Service;
import project.assay.models.PersonInfo;
import project.assay.repositories.PersonInfoRepository;

@Service
public class PersonInfoService {

  private final PersonInfoRepository personInfoRepository;

  public PersonInfoService(PersonInfoRepository personInfoRepository) {
    this.personInfoRepository = personInfoRepository;
  }

  public List<PersonInfo> findAllById(int personId) {
    return personInfoRepository.findByPersonId(personId);
  }

  public void deleteByIds(int personId, int indicatorId) {
    PersonInfo personInfo = personInfoRepository.findByPersonIdAndIndicatorId(personId, indicatorId);
    System.out.println();
    System.out.println();
    System.out.println("_______________________________________________");
    System.out.println(personInfo);
    personInfoRepository.delete(personInfo);
    System.out.println("_______________________________________________");
    System.out.println();
    System.out.println();
  }

  public void save(PersonInfo personInfo) {
    personInfoRepository.save(personInfo);
  }
}
