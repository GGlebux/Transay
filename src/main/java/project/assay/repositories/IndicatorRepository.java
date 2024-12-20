//package project.assay.repositories;
//
//import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import project.assay.models.Indicator;
//
//@Repository
//public interface IndicatorRepository extends JpaRepository<Indicator, Integer> {
//
//  @Query("SELECT i FROM Indicator i WHERE i.name = :name and (i.gender = :gender or i.gender = 'both') and :age BETWEEN i.minAge and i.maxAge")
//  public Indicator findOneCorrect(@Param("name") String name,
//      @Param("gender") String gender,
//      @Param("age") Integer age);
//
//  public Indicator getIndicatorById(int Id);
//
//  @Query("SELECT i FROM Indicator i WHERE (i.gender = :gender or i.gender = 'both') and :age BETWEEN i.minAge and i.maxAge")
//  public List<Indicator> findAllCorrect(@Param("gender") String gender,
//      @Param("age") Integer age);
//}
