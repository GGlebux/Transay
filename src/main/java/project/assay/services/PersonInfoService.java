package project.assay.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.models.PersonInfo;
import project.assay.repositories.PersonInfoRepository;

@Service
public class PersonInfoService {

  private final PersonInfoRepository personInfoRepository;
  private final ReferentService referentService;

  public PersonInfoService(PersonInfoRepository personInfoRepository,
      ReferentService referentService) {
    this.personInfoRepository = personInfoRepository;
    this.referentService = referentService;
  }

  public List<PersonInfo> findAllById(int personId) {
    return personInfoRepository.findByPersonId(personId);
  }

  @Transactional
  public void delete(PersonInfo personInfo) {
    personInfoRepository.delete(personInfo);
  }

  @Transactional
  public void deleteById(int measureId) {
    personInfoRepository.deleteById(measureId);
  }

  @Transactional
  public int save(PersonInfo personInfo) {
    PersonInfo personInfoId = personInfoRepository.save(personInfo);
    return personInfoId.getId();
  }
}
