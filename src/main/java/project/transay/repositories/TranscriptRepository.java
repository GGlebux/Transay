package project.transay.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.transay.models.GenderEnum;
import project.transay.models.Transcript;

import java.util.Set;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Integer> {
    @EntityGraph(attributePaths = {"falls", "raises"})
    @Query("SELECT t FROM Transcript t " +
            "WHERE t.name = :name " +
            "AND (t.gender = :gender or t.gender = project.transay.models.GenderEnum.BOTH)")
    Set<Transcript> findByNameAndGender(@Param("name") String name,
                                             @Param("gender") GenderEnum gender);


    @EntityGraph(attributePaths = {"falls", "raises"})
    @Query("SELECT t FROM Transcript t" +
            " WHERE t.name IN :names " +
            "AND (t.gender = :gender or t.gender = project.transay.models.GenderEnum.BOTH)")
    Set<Transcript> findAllByNameIn(@Param("names") Set<String> names,
                                        @Param("gender") GenderEnum gender);
}
