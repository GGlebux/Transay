package project.assay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.assay.models.Indicator;

@Repository
public interface IndicatorRepository extends JpaRepository<Indicator, Integer> {

  public Indicator getIndicatorById(int Id);
}
