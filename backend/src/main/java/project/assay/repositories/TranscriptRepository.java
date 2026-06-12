package project.assay.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.assay.models.Transcript;
import project.assay.models.enums.PersonGender;

import java.util.Set;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Integer> {
    @EntityGraph(attributePaths = {"falls", "raises"})
    @Query("SELECT t FROM Transcript t " +
            "WHERE t.name = :name " +
            "AND (t.gender = :gender or t.gender = project.assay.models.enums.IndicatorGender.BOTH)")
    Set<Transcript> findByNameAndGender(@Param("name") String name,
                                             @Param("gender") PersonGender gender);


    @EntityGraph(attributePaths = {"falls", "raises"})
    @Query("SELECT t FROM Transcript t" +
            " WHERE t.name IN :names " +
            "AND (t.gender = :gender or t.gender = project.assay.models.enums.IndicatorGender.BOTH)")
    Set<Transcript> findAllByNameIn(@Param("names") Set<String> names,
                                        @Param("gender") PersonGender gender);
}
